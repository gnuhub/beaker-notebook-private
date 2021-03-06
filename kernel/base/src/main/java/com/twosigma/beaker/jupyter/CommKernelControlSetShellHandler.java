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
package com.twosigma.beaker.jupyter;

import com.twosigma.jupyter.KernelFunctionality;
import com.twosigma.jupyter.handler.KernelHandler;
import com.twosigma.jupyter.message.Header;
import com.twosigma.jupyter.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.twosigma.beaker.jupyter.Utils.getAsString;
import static com.twosigma.beaker.jupyter.Comm.COMM_ID;
import static com.twosigma.beaker.jupyter.Comm.DATA;
import static com.twosigma.beaker.jupyter.msg.JupyterMessages.COMM_MSG;

/**
 * @author konst
 */
public class CommKernelControlSetShellHandler extends KernelHandler<Message> {

  public static final String IMPORTS = "imports";
  public static final String CLASSPATH = "classpath";

  public static final String KERNEL_CONTROL_RESPONSE = "kernel_control_response";
  public static final String RESPONSE_OK = "OK";
  public static final String RESPONSE_ERROR = "ERROR";

  private static final Logger logger = LoggerFactory.getLogger(CommKernelControlSetShellHandler.class);

  public CommKernelControlSetShellHandler(KernelFunctionality kernel) {
    super(kernel);
  }

  @Override
  public void handle(Message message)  {
    logger.info("Handing comm message content");
    if(message != null){
      Map<String, Serializable> commMap = message.getContent();
      HashMap<?, ?> messageData = (HashMap<?, ?>)commMap.get(DATA);
      if (messageData != null) {
        boolean ok = handleData((Map<String, List<String>>)messageData);
        publish(createReplayMessage(message, ok));
      }
    } else {
      logger.info("Comm message contend is null");
    }
  }

  public boolean handleData(Map<String, List<String>> data) {
    boolean ret = false;
    if(data.containsKey(IMPORTS) &&data.containsKey(CLASSPATH)){
      List<String> imports = data.get(IMPORTS);
      List<String> classPath = data.get(CLASSPATH);
      kernel.setShellOptions(getAsString(classPath), getAsString(imports));
     ret = true;
    }
    return ret;
  }
  ;
  
  private Message createReplayMessage(Message message, boolean ok) {
    Message ret = null;
    if (message != null) {
      ret = new Message();
      Map<String, Serializable> commMap = message.getContent();
      ret.setHeader(new Header(COMM_MSG, message.getHeader().getSession()));
      HashMap<String, Serializable> map = new HashMap<>();
      map.put(COMM_ID, getString(commMap, COMM_ID));
      HashMap<String, String> data = new HashMap<>();
      data.put(KERNEL_CONTROL_RESPONSE, ok ? RESPONSE_OK : RESPONSE_ERROR);
      map.put(DATA, data);
      ret.setContent(map);
    }
    return ret;
  }
  
  public static String getString(Map<String, Serializable> map, String name) {
    String ret = null;
    if (map != null && name != null && map.containsKey(name)) {
      ret = (String) map.get(name);
    }
    return ret;
  }

}