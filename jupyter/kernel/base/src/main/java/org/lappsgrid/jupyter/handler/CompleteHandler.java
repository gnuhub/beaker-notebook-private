package org.lappsgrid.jupyter.handler;

import com.twosigma.beaker.autocomplete.AutocompleteResult;
import com.twosigma.beaker.evaluator.EvaluatorManager;
import org.lappsgrid.jupyter.KernelFunctionality;
import org.lappsgrid.jupyter.msg.Header;
import org.lappsgrid.jupyter.msg.Message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.twosigma.beaker.jupyter.msg.JupyterMessages.COMPLETE_REPLY;

public class CompleteHandler extends KernelHandler<Message> {

  public static final String STATUS = "status";
  public static final String MATCHES = "matches";
  public static final String CURSOR_END = "cursor_end";
  public static final String CURSOR_START = "cursor_start";
  public static final String CODE = "code";
  public static final String CURSOR_POS = "cursor_pos";

  private EvaluatorManager evaluatorManager;

  public CompleteHandler(KernelFunctionality kernel) {
    super(kernel);
    evaluatorManager = kernel.getEvaluatorManager();
  }

  @Override
  public void handle(Message message) {
    String code = ((String) message.getContent().get(CODE)).trim();
    int cursorPos = ((int) message.getContent().get(CURSOR_POS));
    AutocompleteResult autocomplete = evaluatorManager.autocomplete(code, cursorPos);
    Message reply = createMsg(message, cursorPos, autocomplete);
    send(reply);
  }

  private Message createMsg(Message message, int cursorPos, AutocompleteResult autocomplete) {
    Message reply = new Message();
    reply.setHeader(new Header(COMPLETE_REPLY, message.getHeader().getSession()));
    reply.setIdentities(message.getIdentities());
    reply.setParentHeader(message.getHeader());
    Map<String, Serializable> content = new HashMap<>();
    content.put(STATUS, "ok");
    content.put(MATCHES, autocomplete.getMatches().toArray());
    content.put(CURSOR_END, cursorPos);
    content.put(CURSOR_START, autocomplete.getStartIndex());

    reply.setContent(content);
    return reply;
  }
}