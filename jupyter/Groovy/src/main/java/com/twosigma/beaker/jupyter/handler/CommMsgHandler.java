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
package com.twosigma.beaker.jupyter.handler;

import static com.twosigma.beaker.jupyter.Comm.COMM_ID;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.lappsgrid.jupyter.groovy.GroovyKernel;
import org.lappsgrid.jupyter.groovy.handler.AbstractHandler;
import org.lappsgrid.jupyter.groovy.msg.Message;
import org.slf4j.LoggerFactory;

import com.twosigma.beaker.jupyter.Comm;

public class CommMsgHandler extends AbstractHandler<Message> {

  public CommMsgHandler(GroovyKernel kernel) {
    super(kernel);
    logger = LoggerFactory.getLogger(CommMsgHandler.class);
  }

  public void handle(Message message) throws NoSuchAlgorithmException {
    Map<String, Serializable> commMap = message.getContent();
    Comm comm = kernel.getComm(getString(commMap, COMM_ID));
    comm.handleMsg(message);
  }

  public static String getString(Map<String, Serializable> map, String name) {
    String ret = null;
    if (map != null && name != null && map.containsKey(name)) {
      ret = (String) map.get(name);
    }
    return ret;
  }

}