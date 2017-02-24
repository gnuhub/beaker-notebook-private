package org.lappsgrid.jupyter.groovy.handler;

import static com.twosigma.beaker.jupyter.msg.JupyterMessages.COMPLETE_REPLY;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.twosigma.beaker.groovy.evaluator.GroovyEvaluatorManager;
import org.lappsgrid.jupyter.groovy.GroovyKernel;
import org.lappsgrid.jupyter.groovy.msg.Header;
import org.lappsgrid.jupyter.groovy.msg.Message;
import org.slf4j.LoggerFactory;

import groovy.lang.GroovyClassLoader;

/**
 * The code completion handler. The CompleteHandler is called by Jupyter to
 * determine if the line of code just entered should be executed immediately or
 * if more input is required. It also compiles the code to ensure that it is
 * valid.
 *
 * @author Keith Suderman
 */
public class CompleteHandler extends AbstractHandler<Message> {

  private GroovyClassLoader compiler;
  private static final String COMPLETE_CHARS = "\"\'};])";
  private static final String INCOMPLETE_CHARS = "([:=";
  private String waitingFor = null;
  protected GroovyEvaluatorManager evaluatorManager;

  public CompleteHandler(GroovyKernel kernel) {
    super(kernel);
    logger = LoggerFactory.getLogger(CompleteHandler.class);
    compiler = new GroovyClassLoader();
    evaluatorManager = new GroovyEvaluatorManager(kernel);
  }

  @Override
  public void handle(Message message) throws NoSuchAlgorithmException {
    String code = ((String) message.getContent().get("code")).trim();
    int cursorPos = ((int) message.getContent().get("cursor_pos"));

    List<String> autocomplete = evaluatorManager.autocomplete(code, cursorPos);
    Message reply = new Message();
    reply.setHeader(new Header(COMPLETE_REPLY, message.getHeader().getSession()));
    reply.setIdentities(message.getIdentities());
    reply.setParentHeader(message.getHeader());
    Map<String, Serializable> content = new HashMap<String, Serializable>();
    content.put("status", "ok");
    content.put("matches", autocomplete.toArray());
    content.put("cursor_end", cursorPos);
    content.put("cursor_start", 21);// calculate start point ????

    reply.setContent(content);
    send(reply);
  }

}