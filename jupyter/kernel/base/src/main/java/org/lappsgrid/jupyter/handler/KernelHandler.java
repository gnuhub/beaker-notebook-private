package org.lappsgrid.jupyter.handler;

import org.lappsgrid.jupyter.KernelFunctionality;
import org.lappsgrid.jupyter.msg.Message;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class KernelHandler<T> implements Handler<T> {

  protected KernelFunctionality kernel;

  public KernelHandler(KernelFunctionality kernel) {
    this.kernel = checkNotNull(kernel);
  }

  public void send(Message message) {
    kernel.send(message);
  }

  public void publish(Message message) {
    kernel.publish(message);
  }

  public void exit() {
  }

}