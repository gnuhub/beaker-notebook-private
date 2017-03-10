/*
 *  Copyright 2017 TWO SIGMA OPEN SOURCE, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.lappsgrid.jupyter;

import com.twosigma.beaker.jupyter.threads.AbstractMessageReaderThread;
import com.twosigma.beaker.jupyter.threads.AbstractThread;
import org.lappsgrid.jupyter.json.MessageSerializer;
import org.lappsgrid.jupyter.msg.Header;
import org.lappsgrid.jupyter.msg.Message;
import org.lappsgrid.jupyter.security.HmacSigner;
import org.lappsgrid.jupyter.threads.ControlThread;
import org.lappsgrid.jupyter.threads.HeartbeatThread;
import org.lappsgrid.jupyter.threads.ShellThread;
import org.lappsgrid.jupyter.threads.StdinThread;
import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.lappsgrid.jupyter.json.MessageSerializer.toJson;

public class KernelSockets {

  public static final String DELIM = "<IDS|MSG>";

  private Config configuration;
  /**
   * Used to generate the HMAC signatures for messages
   */
  private HmacSigner hmac;

  private Map<String, AbstractMessageReaderThread> threads = new HashMap<>();
  private ZMQ.Socket hearbeatSocket;
  private ZMQ.Socket controlSocket;
  private ZMQ.Socket shellSocket;
  private ZMQ.Socket iopubSocket;
  private ZMQ.Socket stdinSocket;

  public KernelSockets(Kernel kernel, Config configuration) {

    this.configuration = configuration;
    this.hmac = new HmacSigner(configuration.getKey());

    final String connection = configuration.getTransport() + "://" + configuration.getHost();
    final ZMQ.Context context = ZMQ.context(1);

    // Create all the sockets we need to listen to.
    hearbeatSocket = getNewSocket(ZMQ.REP, configuration.getHeartbeat(), connection, context);
    iopubSocket = getNewSocket(ZMQ.PUB, configuration.getIopub(), connection, context);
    controlSocket = getNewSocket(ZMQ.ROUTER, configuration.getControl(), connection, context);
    stdinSocket = getNewSocket(ZMQ.ROUTER, configuration.getStdin(), connection, context);
    shellSocket = getNewSocket(ZMQ.ROUTER, configuration.getShell(), connection, context);

    // Create all the threads that respond to ZMQ messages.

    threads.put(HeartbeatThread.class.getSimpleName(), new HeartbeatThread(hearbeatSocket, kernel));
    threads.put(ControlThread.class.getSimpleName(), new ControlThread(controlSocket, kernel));
    threads.put(StdinThread.class.getSimpleName(), new StdinThread(stdinSocket, kernel));
    threads.put(ShellThread.class.getSimpleName(), new ShellThread(shellSocket, kernel));

  }

  public synchronized void publish(Message message) {
    send(this.getIopubSocket(), message);
  }

  public synchronized void send(Message message) {
    send(this.getShellSocket(), message);
  }

  public void send(final ZMQ.Socket socket, Message message) {
    message.getIdentities().forEach(socket::sendMore);
    socket.sendMore(DELIM);

    final List<String> parts = parts(message);
    socket.sendMore(hmac.sign(parts));

    for (int i = 0; i < 3; i++) {
      socket.sendMore(parts.get(i));
    }
    socket.send(parts.get(3));
  }

  private List<String> parts(Message message) {
    // Encode the message parts (blobs) and calculate the signature.
    return new ArrayList<>(asList(
            toJson(message.getHeader()),
            toJson(message.getParentHeader()),
            toJson(message.getMetadata()),
            toJson(message.getContent())));
  }

  public ZMQ.Socket getShellSocket() {
    return shellSocket;
  }

  public ZMQ.Socket getIopubSocket() {
    return iopubSocket;
  }


  // A factory "method" for creating sockets.
  private ZMQ.Socket getNewSocket(int type, int port, String connection, ZMQ.Context context) {
    ZMQ.Socket socket = context.socket(type);
    socket.bind(connection + ":" + String.valueOf(port));
    return socket;
  }

  public void start() {
    // Start all the socket handler threads
    threads.values().forEach(AbstractThread::start);
  }

  public void haltAndJoin() throws InterruptedException {
    // Signal all threads that it is time to stop and then wait for
    // them to finish.
    for (AbstractMessageReaderThread thread : threads.values()) {
      thread.halt();
    }
    for (AbstractMessageReaderThread thread : threads.values()) {
      thread.join();
    }
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
   * @param socket The ZMQ.Socket object to read from.
   * @return a newly initialized Message object.
   */
  public Message readMessage(ZMQ.Socket socket) {
    Message message = new Message();
    try {
      // Read socket identities until we encounter the delimiter
      String identity = read(socket);
      while (!KernelSockets.DELIM.equals(identity)) {
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
      String actualSig = hmac.signBytes(new ArrayList<>(asList(header, parent, metadata, content)));
      if (!expectedSig.equals(actualSig)) {
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

  public String read(ZMQ.Socket socket) {
    return new String(socket.recv());
  }

  public <T> T parse(byte[] bytes, Class<T> theClass) {
    return bytes != null ? MessageSerializer.parse(new String(bytes), theClass) : null;
  }


}
