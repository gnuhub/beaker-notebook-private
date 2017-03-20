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
package com.twosigma.beaker.javash.comm;

import com.twosigma.jupyter.KernelFunctionality;
import com.twosigma.jupyter.handler.Handler;
import com.twosigma.jupyter.message.Message;

import com.twosigma.beaker.jupyter.CommKernelControlInterrupt;
import com.twosigma.beaker.jupyter.CommKernelControlSetShellHandler;
import com.twosigma.beaker.jupyter.CommNamesEnum;
import com.twosigma.beaker.jupyter.handler.CommOpenHandler;

public class JavaCommOpenHandler extends CommOpenHandler{

 private Handler<?>[] KERNEL_CONTROL_CHANNEL_HANDLERS = {
     new CommKernelControlSetShellHandler(kernel),
     new JavaCommKernelControlSetShellHandler(kernel),
     new CommKernelControlInterrupt(kernel)};

  public JavaCommOpenHandler(KernelFunctionality kernel) {
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