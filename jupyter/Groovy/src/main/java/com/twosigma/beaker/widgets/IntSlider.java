/*
 *  Copyright 2014 TWO SIGMA OPEN SOURCE, LLC
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
package com.twosigma.beaker.widgets;

import com.twosigma.beaker.jupyter.Comm;
import com.twosigma.beaker.jupyter.Utils;
import org.lappsgrid.jupyter.groovy.GroovyKernel;
import org.lappsgrid.jupyter.groovy.GroovyKernelManager;
import org.lappsgrid.jupyter.groovy.msg.Header;
import org.lappsgrid.jupyter.groovy.msg.Message;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static com.twosigma.beaker.jupyter.msg.JupyterMessages.COMM_MSG;
import static com.twosigma.beaker.jupyter.msg.JupyterMessages.COMM_OPEN;


public class IntSlider {

  private static final String TARGET_NAME = "jupyter.widget";
  public static final String COMMS = "comms";
  String _view_name = "IntSliderView";
  String _model_name = "IntSliderModel";
  String _model_module = "jupyter-js-widgets";
  private Comm comm;

  public IntSlider() throws NoSuchAlgorithmException {
    open();
  }

  private void open() throws NoSuchAlgorithmException {
    if(comm==null){
     // comm= new Comm(Utils.uuid(), TARGET_NAME);
      GroovyKernel kernel = GroovyKernelManager.get();
      Message message = new Message();
      Header header = new Header();
      header.setTypeEnum(COMM_MSG);
      message.setHeader(header);
      HashMap<String, Serializable> content = new HashMap<>();
      content.put("myXXX","Ok");
      message.setContent(content);
      kernel.send(message);
    }
  }

}
