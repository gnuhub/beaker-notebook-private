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
package com.twosigma.beaker.widgets;

import com.twosigma.beaker.jupyter.Comm;
import org.lappsgrid.jupyter.groovy.handler.IHandler;
import org.lappsgrid.jupyter.groovy.msg.Message;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

public abstract class IntWidget extends DOMWidget {

  private Integer value = 0;

  public IntWidget() throws NoSuchAlgorithmException {
    super();
  }

  protected void addValueChangeMsgCallback(final Comm comm) {
    comm.addMsgCallbackList(new IHandler<Message>() {
      @Override
      public void handle(Message message) throws NoSuchAlgorithmException {
        Map data = (Map) message.getContent().get("data");
        Map sync_data = (Map) data.get("sync_data");
        int value = (int) sync_data.get(VALUE);
        updateValue(value);
      }
    });
  }

  private void updateValue(int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }

  public void setValue(int value) {
    this.value = value;
    sendUpdate(VALUE, value);
  }

}
