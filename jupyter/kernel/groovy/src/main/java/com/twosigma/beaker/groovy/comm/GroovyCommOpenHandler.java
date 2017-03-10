package com.twosigma.beaker.groovy.comm;

import org.lappsgrid.jupyter.KernelFunctionality;
import org.lappsgrid.jupyter.handler.Handler;
import org.lappsgrid.jupyter.msg.Message;

import com.twosigma.beaker.jupyter.CommKernelControlInterrupt;
import com.twosigma.beaker.jupyter.CommKernelControlSetShellHandler;
import com.twosigma.beaker.jupyter.CommNamesEnum;
import com.twosigma.beaker.jupyter.handler.CommOpenHandler;

public class GroovyCommOpenHandler extends CommOpenHandler{

 private Handler<?>[] KERNEL_CONTROL_CHANNEL_HANDLERS = {
     new CommKernelControlSetShellHandler(kernel),
     new GroovyCommKernelControlSetShellHandler(kernel),
     new CommKernelControlInterrupt(kernel)};
  
  public GroovyCommOpenHandler(KernelFunctionality kernel) {
    super(kernel);
  }

  public Handler<Message>[] getKernelControlChanelHandlers(String targetName){
    if(CommNamesEnum.KERNEL_CONTROL_CHANNEL.getTargetName().equalsIgnoreCase(targetName)){
      return (Handler<Message>[]) KERNEL_CONTROL_CHANNEL_HANDLERS;
    }else{
      return (Handler<Message>[]) new Handler<?>[0];
    }
  }

}