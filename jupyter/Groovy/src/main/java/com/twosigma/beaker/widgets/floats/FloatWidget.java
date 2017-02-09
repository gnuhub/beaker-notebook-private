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
package com.twosigma.beaker.widgets.floats;

import com.twosigma.beaker.jupyter.Comm;
import com.twosigma.beaker.widgets.DOMWidget;
import org.lappsgrid.jupyter.groovy.handler.IHandler;
import org.lappsgrid.jupyter.groovy.msg.Message;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class FloatWidget extends DOMWidget {

  private Double value = 0.0;

  public FloatWidget() throws NoSuchAlgorithmException {
    super();
  }

  @Override
  protected HashMap<String, Serializable> content(HashMap<String, Serializable> content) {
    super.content(content);
    content.put(VALUE, this.value);
    return content;
  }

  protected void addValueChangeMsgCallback(final Comm comm) {
    comm.addMsgCallbackList(new IHandler<Message>() {
      @Override
      public void handle(Message message) throws NoSuchAlgorithmException {
        Map data = (Map) message.getContent().get("data");
        Map sync_data = (Map) data.get("sync_data");
        Object o = sync_data.get(VALUE);
        updateValue((o instanceof Integer) ? ((Integer) o).doubleValue() : (double) o);
      }
    });
  }

  private void updateValue(Double value) {
    this.value = value;
  }

  public Double getValue() {
    return this.value;
  }

  public void setValue(Double value) {
    this.value = value;
    sendUpdate(VALUE, value);
  }
}
