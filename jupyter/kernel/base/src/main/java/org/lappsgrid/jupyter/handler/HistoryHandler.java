package org.lappsgrid.jupyter.handler;

import org.lappsgrid.jupyter.KernelFunctionality;
import org.lappsgrid.jupyter.msg.Message;

public class HistoryHandler extends KernelHandler<Message> {
  public HistoryHandler(KernelFunctionality kernel) {
    super(kernel);
  }

  @Override
  public void handle(Message message) {
    //TODO Handle history messages.
  }

}