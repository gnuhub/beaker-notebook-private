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

import com.twosigma.beaker.jupyter.handler.CommCloseHandler;
import com.twosigma.beaker.jupyter.handler.CommInfoHandler;
import com.twosigma.beaker.jupyter.handler.CommMsgHandler;
import com.twosigma.beaker.jupyter.handler.CommOpenHandler;
import com.twosigma.beaker.jupyter.handler.ExecuteRequestHandler;
import com.twosigma.beaker.jupyter.msg.JupyterMessages;
import com.twosigma.beaker.jupyter.msg.MessageCreator;
import org.lappsgrid.jupyter.handler.AbstractHandler;
import org.lappsgrid.jupyter.handler.CompleteHandler;
import org.lappsgrid.jupyter.handler.HistoryHandler;
import org.lappsgrid.jupyter.handler.IHandler;
import org.lappsgrid.jupyter.handler.KernelInfoHandler;
import org.lappsgrid.jupyter.msg.Message;

import java.util.HashMap;
import java.util.Map;
/**
 * Message handlers. All sockets listeners will dispatch to these handlers.
 */
public class KernelHandlers {

  private Map<JupyterMessages, AbstractHandler<Message>> handlers;
  private KernelFunctionality kernel;

  public KernelHandlers(KernelFunctionality kernel, final CommOpenHandler commOpenHandler) {
    this.kernel = kernel;
    this.handlers = createHandlers(commOpenHandler);
  }

  private Map<JupyterMessages, AbstractHandler<Message>> createHandlers(final CommOpenHandler commOpenHandler) {
    Map<JupyterMessages, AbstractHandler<Message>> handlers = new HashMap<>();
    handlers.put(JupyterMessages.EXECUTE_REQUEST, new ExecuteRequestHandler(kernel));
    handlers.put(JupyterMessages.KERNEL_INFO_REQUEST, new KernelInfoHandler(kernel));
    handlers.put(JupyterMessages.COMPLETE_REQUEST, new CompleteHandler(kernel));
    handlers.put(JupyterMessages.HISTORY_REQUEST, new HistoryHandler(kernel));
    handlers.put(JupyterMessages.COMM_INFO_REQUEST, new CommInfoHandler(kernel));
    handlers.put(JupyterMessages.COMM_CLOSE, new CommCloseHandler(kernel));
    handlers.put(JupyterMessages.COMM_MSG, new CommMsgHandler(kernel, new MessageCreator(kernel)));
    if (commOpenHandler != null) {
      handlers.put(JupyterMessages.COMM_OPEN, commOpenHandler);
    }
    return handlers;
  }

  public IHandler<Message> get(JupyterMessages type) {
    return handlers.get(type);
  }

  public void exit() {
    for (AbstractHandler<Message> handler : this.handlers.values()) {
      handler.exit();
    }
  }
}
