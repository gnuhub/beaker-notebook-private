package org.lappsgrid.jupyter.groovy;

import com.twosigma.beaker.groovy.NamespaceClient;
import com.twosigma.beaker.groovy.evaluator.GroovyEvaluatorManager;
import com.twosigma.beaker.jupyter.Comm;
import com.twosigma.beaker.jupyter.CommNamesEnum;
import com.twosigma.beaker.jupyter.GroovyKernelManager;
import com.twosigma.beaker.jupyter.handler.CommCloseHandler;
import com.twosigma.beaker.jupyter.handler.CommInfoHandler;
import com.twosigma.beaker.jupyter.handler.CommMsgHandler;
import com.twosigma.beaker.jupyter.handler.CommOpenHandler;
import com.twosigma.beaker.jupyter.handler.ExecuteRequestHandler;
import com.twosigma.beaker.jupyter.msg.JupyterMessages;
import com.twosigma.beaker.jupyter.msg.MessageCreator;
import com.twosigma.beaker.jupyter.threads.AbstractMessageReaderThread;
import com.twosigma.beaker.jupyter.threads.ExecutionResultSender;
import org.lappsgrid.jupyter.groovy.handler.AbstractHandler;
import org.lappsgrid.jupyter.groovy.handler.CompleteHandler;
import org.lappsgrid.jupyter.groovy.handler.HistoryHandler;
import org.lappsgrid.jupyter.groovy.handler.IHandler;
import org.lappsgrid.jupyter.groovy.handler.KernelInfoHandler;
import org.lappsgrid.jupyter.groovy.json.Serializer;
import org.lappsgrid.jupyter.groovy.msg.Header;
import org.lappsgrid.jupyter.groovy.msg.Message;
import org.lappsgrid.jupyter.groovy.security.HmacSigner;
import org.lappsgrid.jupyter.groovy.threads.ControlThread;
import org.lappsgrid.jupyter.groovy.threads.HeartbeatThread;
import org.lappsgrid.jupyter.groovy.threads.ShellThread;
import org.lappsgrid.jupyter.groovy.threads.StdinThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.twosigma.beaker.jupyter.Utils.uuid;

/**
 * The entry point for the Jupyter kernel.
 *
 * @author Keith Suderman
 */
public class GroovyKernel implements GroovyKernelFunctionality{

  private static final Logger logger = LoggerFactory.getLogger(GroovyKernel.class);

  private volatile boolean running = false;
  private static final String DELIM = "<IDS|MSG>";
  /**
   * Used to generate the HMAC signatures for messages
   */
  private HmacSigner hmac;
  /**
   * The UUID for this session.
   */
  private String id;
  /**
   * Information from the connection file from Jupyter.
   */
  private File connectionFile;
  private Config configuration;
  /**
   * Message handlers. All sockets listeners will dispatch to these handlers.
   */
  private Map<JupyterMessages, AbstractHandler<Message>> handlers;
  private Map<String, AbstractMessageReaderThread> threads = new HashMap<>();
  private Map<String, Comm> commMap;
  private ExecutionResultSender executionResultSender;
  private GroovyEvaluatorManager groovyEvaluatorManager;
  
  private ZMQ.Socket hearbeatSocket;
  private ZMQ.Socket controlSocket;
  private ZMQ.Socket shellSocket;
  private ZMQ.Socket iopubSocket;
  private ZMQ.Socket stdinSocket;

  public GroovyKernel() {
    id = uuid();
    commMap = new ConcurrentHashMap<>();
    executionResultSender = new ExecutionResultSender(this);
    groovyEvaluatorManager = new GroovyEvaluatorManager(this);
    installHandlers();
  }

  public void shutdown() {
    running = false;
    for (Comm comm : commMap.values()) {
      try {
        comm.close();
      } catch (NoSuchAlgorithmException e) {
        logger.info("Comm close error, Comm info = " + comm );
      }
    }
  }

  private void installHandlers() {
    handlers = new HashMap<>();
    handlers.put(JupyterMessages.EXECUTE_REQUEST, new ExecuteRequestHandler(this, groovyEvaluatorManager));
    handlers.put(JupyterMessages.KERNEL_INFO_REQUEST, new KernelInfoHandler(this));
    handlers.put(JupyterMessages.COMPLETE_REQUEST, new CompleteHandler(this));
    handlers.put(JupyterMessages.HISTORY_REQUEST, new HistoryHandler(this));
    handlers.put(JupyterMessages.COMM_OPEN, new CommOpenHandler(this));
    handlers.put(JupyterMessages.COMM_INFO_REQUEST, new CommInfoHandler(this));
    handlers.put(JupyterMessages.COMM_CLOSE, new CommCloseHandler(this));
    handlers.put(JupyterMessages.COMM_MSG, new CommMsgHandler(this, new MessageCreator(this)));
  }

  public synchronized void setShellOptions(String cp, String in, String od){
    groovyEvaluatorManager.setShellOptions(cp, in, od);
  }

  public synchronized boolean isCommPresent(String hash){
    return commMap.containsKey(hash);
  }
  
  public Set<String> getCommHashSet(){
    return commMap.keySet();
  }
  
  public synchronized void addComm(String hash, Comm commObject){
    if(!isCommPresent(hash)){
      commMap.put(hash, commObject);
    }
  }
  
  public synchronized Comm getComm(String hash){
    return commMap.get(hash);
  }
  
  public synchronized List<Comm> getCommByTargetName(String targetName){
    List<Comm> ret = new ArrayList<>();
    if(targetName != null){
      for (Comm comm : commMap.values()) {
        if(comm.getTargetName().equals(targetName)){
          ret.add(comm);
        }
      }
    }
    return ret;
  }
  
  public synchronized List<Comm> getCommByTargetName(CommNamesEnum targetName){
    return targetName != null ? getCommByTargetName(targetName.getTargetName()) : new ArrayList<>() ;
  }
  
  public synchronized void removeComm(String hash){
    if(isCommPresent(hash)){
      commMap.remove(hash);
    }
  }
  
  /**
   * Sends a Message to the iopub socket.
   * 
   * @throws NoSuchAlgorithmException
   */
  public void publish(Message message) throws NoSuchAlgorithmException {
    send(iopubSocket, message);
  }

  public synchronized void send(Message message) throws NoSuchAlgorithmException {
    send(shellSocket, message);
  }

  public void send(final ZMQ.Socket socket, Message message) throws NoSuchAlgorithmException {
    logger.trace("Sending message: {}", message.asJson());
    // Encode the message parts (blobs) and calculate the signature.
    final List<String> parts = new ArrayList<String>(Arrays.asList(
        Serializer.toJson(message.getHeader()),
        Serializer.toJson(message.getParentHeader()),
        Serializer.toJson(message.getMetadata()),
        Serializer.toJson(message.getContent())));
    String signature = hmac.sign(parts);
    logger.trace("Signature is {}", signature);

    // Now send the message down the wire.
    for (byte[] list : message.getIdentities()) {
      socket.sendMore(list);
    }

    socket.sendMore(DELIM);
    socket.sendMore(signature);

    for (int i = 0; i < 3; i++) {
      socket.sendMore(parts.get(i));
    }

    socket.send(parts.get(3));
    logger.trace("Message sent");
  }

  public String read(ZMQ.Socket socket) {
    return new String(socket.recv());
  }

  public <T> T parse(byte[] bytes, Class<T> theClass) {
    return bytes != null ? Serializer.parse(new String(bytes), theClass) : null;
  }

  /**
   * Reads a Jupyter message from a ZMQ socket.
   * <p>
   * Each message consists of at least six blobs of bytes:
   * <ul>
   * <li>zero or more identities</li>
   * <li>'&lt;IDS|MSG&gt;'</li>
   * <li>HMAC signature</li>
   * <li>header</li>
   * <li>parent header</li>
   * <li>metadata</li>
   * <li>content</li>
   * </ul>
   *
   * @param socket
   *          The ZMQ.Socket object to read from.
   * @return a newly initialized Message object.
   */
  public Message readMessage(ZMQ.Socket socket) {
    Message message = new Message();
    try {
      // Read socket identities until we encounter the delimiter
      String identity = read(socket);
      while (!DELIM.equals(identity)) {
        message.getIdentities().add(identity.getBytes());
        identity = read(socket);
      }

      // Read the signature and the four blobs
      String expectedSig = read(socket);
      byte[] header = socket.recv();
      byte[] parent = socket.recv();
      byte[] metadata = socket.recv();
      byte[] content = socket.recv();

      // Make sure that the signatures match before proceeding.
      String actualSig = hmac.signBytes((List<byte[]>) new ArrayList<byte[]>(Arrays.asList(header, parent, metadata, content)));
      if (!expectedSig.equals(actualSig)) {
        logger.error("Message signatures do not match");
        logger.error("Expected: []", expectedSig);
        logger.error("Actual  : []", actualSig);
        throw new RuntimeException("Signatures do not match.");
      }

      // Parse the byte buffers into the appropriate types
      message.setHeader(parse(header, Header.class));
      message.setParentHeader(parse(parent, Header.class));
      message.setMetadata(parse(metadata, LinkedHashMap.class));
      message.setContent(parse(content, LinkedHashMap.class));

    } catch (Exception e) {
      throw new RuntimeException("Invalid hmac exception while converting to HmacSHA256");
    }

    return message;
  }

  public IHandler<Message> getHandler(JupyterMessages type) {
    return handlers.get(type);
  }

  // A factory "method" for creating sockets.
  private ZMQ.Socket getNewSocket(int type, int port, String connection, ZMQ.Context context) {
    ZMQ.Socket socket = context.socket(type);
    socket.bind(connection + ":" + String.valueOf(port));
    return socket;
  }

  public void run() throws InterruptedException, IOException {
    logger.info("Groovy Jupyter kernel starting.");
    running = true;

    logger.debug("Parsing the connection file.");
    logger.info("Path to config file : " + connectionFile.getAbsolutePath());
    configuration = Serializer.parse(new String(Files.readAllBytes(connectionFile.toPath())), Config.class);

    logger.debug("Creating signing hmac with: {}", configuration.getKey());
    hmac = new HmacSigner(configuration.getKey());

    final String connection = configuration.getTransport() + "://" + configuration.getHost();
    final ZMQ.Context context = ZMQ.context(1);

    // Create all the sockets we need to listen to.
    hearbeatSocket = getNewSocket(ZMQ.REP, configuration.getHeartbeat(), connection, context);
    iopubSocket = getNewSocket(ZMQ.PUB, configuration.getIopub(), connection, context);
    controlSocket = getNewSocket(ZMQ.ROUTER, configuration.getControl(), connection, context);
    stdinSocket = getNewSocket(ZMQ.ROUTER, configuration.getStdin(), connection, context);
    shellSocket = getNewSocket(ZMQ.ROUTER, configuration.getShell(), connection, context);

    // Create all the threads that respond to ZMQ messages.

    threads.put(HeartbeatThread.class.getSimpleName(), new HeartbeatThread(hearbeatSocket, this));
    threads.put(ControlThread.class.getSimpleName(),new ControlThread(controlSocket, this));
    threads.put(StdinThread.class.getSimpleName(),new StdinThread(stdinSocket, this));
    threads.put(ShellThread.class.getSimpleName(),new ShellThread(shellSocket, this));

    // Start all the socket handler threads
    for (AbstractMessageReaderThread thread : threads.values()) {
      thread.start();
    }

    while (running) {
      // Nothing to do but navel gaze until another thread sets
      // running == false
      Thread.sleep(1000);
    }
    
    for (AbstractHandler<Message> handler : handlers.values()) {
      handler.exit();
    }
    
    if(executionResultSender != null){
      executionResultSender.exit();
    }

    // Signal all threads that it is time to stop and then wait for
    // them to finish.
    logger.info("Shutting down");
    for (AbstractMessageReaderThread thread : threads.values()) {
      thread.halt();
    }
    for (AbstractMessageReaderThread thread : threads.values()) {
      thread.join();
    }
    logger.info("Done");
  }

  public static void main(final String[] args) throws InterruptedException, IOException {
    if (args.length != 1) {
      System.out.println("Invalid parameters passed to the Groovy kernel.");
      System.out.println("Expected one parameter, found " + String.valueOf(args.length));
      for (String string : args) {
        System.out.println(string);
      }
      System.exit(1);
    }

    File config = new File(args[0]);
    if (!config.exists()) {
      System.out.println("Kernel configuration not found.");
      System.exit(1);
    }

    GroovyKernel kernel = new GroovyKernel();
    GroovyKernelManager.register(kernel);
    kernel.connectionFile = config;
    kernel.run();
  }

  public String getId() {
    return id;
  }

  public ExecutionResultSender getExecutionResultSender() {
    return executionResultSender;
  }

  public Message getParentMessage(){
    return NamespaceClient.getBeaker() != null && NamespaceClient.getBeaker().getOutputObj() != null ? (Message)NamespaceClient.getBeaker().getOutputObj().getJupyterMessage() : null;
  }

}