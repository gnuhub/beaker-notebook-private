package org.lappsgrid.jupyter.threads;

import org.lappsgrid.jupyter.Kernel;
import org.lappsgrid.jupyter.msg.Header;
import org.lappsgrid.jupyter.msg.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import com.twosigma.beaker.jupyter.msg.JupyterMessages;
import com.twosigma.beaker.jupyter.threads.AbstractMessageReaderThread;

import static com.twosigma.beaker.jupyter.msg.JupyterMessages.SHUTDOWN_REPLY;
import static com.twosigma.beaker.jupyter.msg.JupyterMessages.SHUTDOWN_REQUEST;


/**
 * @author Keith Suderman
 */
public class ControlThread extends AbstractMessageReaderThread {

  public static final Logger logger = LoggerFactory.getLogger(ControlThread.class);

  public ControlThread(ZMQ.Socket socket, Kernel kernel) {
    super(socket, kernel);
  }

  @Override
  public void run() {
    while (getRunning()) {
      Message message = readMessage();
      JupyterMessages type = message.getHeader().getTypeEnum();
      if (type.equals(SHUTDOWN_REQUEST)) {
        logger.info("Control handler received a shutdown request");
        getKernel().shutdown();
        Message reply = new Message();
        reply.setHeader(new Header(SHUTDOWN_REPLY, message.getHeader().getSession()));
        reply.setParentHeader(message.getHeader());
        reply.setContent(message.getContent());
        send(reply);
      } else {
        logger.warn("Unhandled control message: {}", type);
      }
    }
    logger.info("ControlThread shutdown.");
  }

}