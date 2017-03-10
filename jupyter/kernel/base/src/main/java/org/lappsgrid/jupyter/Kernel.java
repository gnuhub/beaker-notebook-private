package org.lappsgrid.jupyter;

import com.twosigma.beaker.evaluator.Evaluator;
import com.twosigma.beaker.evaluator.EvaluatorManager;
import com.twosigma.beaker.jupyter.Comm;
import com.twosigma.beaker.jupyter.KernelManager;
import com.twosigma.beaker.jupyter.handler.CommCloseHandler;
import com.twosigma.beaker.jupyter.handler.CommInfoHandler;
import com.twosigma.beaker.jupyter.handler.CommMsgHandler;
import com.twosigma.beaker.jupyter.handler.CommOpenHandler;
import com.twosigma.beaker.jupyter.handler.ExecuteRequestHandler;
import com.twosigma.beaker.jupyter.msg.JupyterMessages;
import com.twosigma.beaker.jupyter.msg.MessageCreator;
import com.twosigma.beaker.jupyter.threads.ExecutionResultSender;
import org.lappsgrid.jupyter.handler.AbstractHandler;
import org.lappsgrid.jupyter.handler.CompleteHandler;
import org.lappsgrid.jupyter.handler.HistoryHandler;
import org.lappsgrid.jupyter.handler.IHandler;
import org.lappsgrid.jupyter.handler.KernelInfoHandler;
import org.lappsgrid.jupyter.json.Serializer;
import org.lappsgrid.jupyter.msg.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The entry point for the Jupyter kernel.
 *
 * @author Keith Suderman
 */
public abstract class Kernel implements KernelFunctionality {

  public static String OS = System.getProperty("os.name").toLowerCase();
  private static final Logger logger = LoggerFactory.getLogger(Kernel.class);

  private volatile boolean running = false;

  /**
   * The UUID for this session.
   */
  private String id;
  private Evaluator evaluator;
  /**
   * Information from the connection file from Jupyter.
   */
  protected File connectionFile;
  protected Config configuration;
  /**
   * Message handlers. All sockets listeners will dispatch to these handlers.
   */
  private Map<JupyterMessages, AbstractHandler<Message>> handlers;
  private Map<String, Comm> commMap;
  private ExecutionResultSender executionResultSender;
  private EvaluatorManager evaluatorManager;

  private KernelSockets kernelSockets;

  public Kernel(final String id, final Evaluator evaluator) {
    this.id = id;
    this.evaluator = evaluator;
    this.commMap = new ConcurrentHashMap<>();
    this.executionResultSender = new ExecutionResultSender(this);
    this.evaluatorManager = new EvaluatorManager(this, this.evaluator);
    this.handlers = createHandlers();

    SignalHandler handler = new SignalHandler() {
      public void handle(Signal sig) {
        logger.info("Got " + sig.getName() + " signal, canceling cell execution");
        cancelExecution();
      }
    };
    if (!isWindows()) {
      Signal.handle(new Signal("INT"), handler);
    }
  }

  public static boolean isWindows() {
    return (OS.indexOf("win") >= 0);
  }

  public void shutdown() {
    running = false;
    commMap.values().forEach(Comm::close);
  }

  private Map<JupyterMessages, AbstractHandler<Message>> createHandlers() {
    Map<JupyterMessages, AbstractHandler<Message>> handlers = new HashMap<>();
    handlers.put(JupyterMessages.EXECUTE_REQUEST, new ExecuteRequestHandler(this, evaluatorManager));
    handlers.put(JupyterMessages.KERNEL_INFO_REQUEST, new KernelInfoHandler(this));
    handlers.put(JupyterMessages.COMPLETE_REQUEST, new CompleteHandler(this, this.evaluator));
    handlers.put(JupyterMessages.HISTORY_REQUEST, new HistoryHandler(this));
    CommOpenHandler coh = getCommOpenHandler(this);
    if (coh != null) {
      handlers.put(JupyterMessages.COMM_OPEN, coh);
    }
    handlers.put(JupyterMessages.COMM_INFO_REQUEST, new CommInfoHandler(this));
    handlers.put(JupyterMessages.COMM_CLOSE, new CommCloseHandler(this));
    handlers.put(JupyterMessages.COMM_MSG, new CommMsgHandler(this, new MessageCreator(this)));
    return handlers;
  }

  public synchronized void setShellOptions(String cp, String in, String od) {
    evaluatorManager.setShellOptions(cp, in, od);
  }

  @Override
  public synchronized void cancelExecution() {
    evaluatorManager.killAllThreads();
  }

  public synchronized boolean isCommPresent(String hash) {
    return commMap.containsKey(hash);
  }

  public Set<String> getCommHashSet() {
    return commMap.keySet();
  }

  public synchronized void addComm(String hash, Comm commObject) {
    if (!isCommPresent(hash)) {
      commMap.put(hash, commObject);
    }
  }

  public synchronized Comm getComm(String hash) {
    return commMap.get(hash != null ? hash : "");
  }

  public synchronized void removeComm(String hash) {
    if (hash != null && isCommPresent(hash)) {
      commMap.remove(hash);
    }
  }

  /**
   * Sends a Message to the iopub socket.
   */
  public synchronized void publish(Message message) {
    this.kernelSockets.publish(message);
  }

  public synchronized void send(Message message) {
    this.kernelSockets.send(message);
  }

  @Override
  public void send(ZMQ.Socket socket, Message message) {
    this.kernelSockets.send(socket, message);
  }

  public Message readMessage(ZMQ.Socket socket) {
    return this.kernelSockets.readMessage(socket);
  }

  public IHandler<Message> getHandler(JupyterMessages type) {
    return handlers.get(type);
  }

  public void run() throws InterruptedException, IOException {
    logger.info("Groovy Jupyter kernel starting.");
    running = true;

    logger.debug("Parsing the connection file.");
    logger.info("Path to config file : " + connectionFile.getAbsolutePath());
    configuration = Serializer.parse(new String(Files.readAllBytes(connectionFile.toPath())), Config.class);

    logger.debug("Creating signing hmac with: {}", configuration.getKey());

    this.kernelSockets = new KernelSockets(this, configuration);
    this.kernelSockets.start();

    while (running) {
      // Nothing to do but navel gaze until another thread sets
      // running == false
      Thread.sleep(1000);
    }

    for (AbstractHandler<Message> handler : handlers.values()) {
      handler.exit();
    }

    if (executionResultSender != null) {
      executionResultSender.exit();
    }

    this.kernelSockets.haltAndJoin();
    logger.info("Done");
  }

  protected static File getConfig(final String[] args) {
    if (args.length != 1) {
      System.out.println("Invalid parameters passed to the Kernel.");
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
    return config;
  }

  protected static void runKernel(File config, Kernel kernel) throws InterruptedException, IOException {
    KernelManager.register(kernel);
    kernel.connectionFile = config;
    kernel.run();
  }

  public String getId() {
    return id;
  }

  public ExecutionResultSender getExecutionResultSender() {
    return executionResultSender;
  }

  public abstract CommOpenHandler getCommOpenHandler(Kernel kernel);

}