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
import com.twosigma.beaker.jupyter.CommNamesEnum;
import com.twosigma.beaker.jupyter.Utils;
import org.lappsgrid.jupyter.groovy.handler.IHandler;
import org.lappsgrid.jupyter.groovy.msg.Message;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static com.twosigma.beaker.widgets.Layout.IPY_MODEL;
import static com.twosigma.beaker.widgets.Layout.LAYOUT;

public class ToggleButton extends Widget {

  protected static final String MSG_THROTTLE = "msg_throttle";

  public static final String TOOLTIP = "tooltip";

  private final String _view_name = "ToggleButtonView";
  private final String _model_name = "ToggleButtonModel";
  private final String _model_module = "jupyter-js-widgets";
  private final String _view_module = "jupyter-js-widgets";

  private Comm comm;
  private Layout layout;
  private Boolean value = false;

  private Integer msg_throttle = 3;
  private String tooltip = "";

  public ToggleButton() throws NoSuchAlgorithmException {
    comm = new Comm(Utils.uuid(), CommNamesEnum.JUPYTER_WIDGET);
    layout = new Layout();
    openComm(comm);
  }

  private void openComm(final Comm comm) throws NoSuchAlgorithmException {
    comm.setData(content());
    addValueChangeMsgCallback(comm);
    comm.open();
  }

  private void addValueChangeMsgCallback(final Comm comm) {
    comm.addMsgCallbackList(new IHandler<Message>() {
      @Override
      public void handle(Message message) throws NoSuchAlgorithmException {
        Map data = (Map) message.getContent().get("data");
        Map sync_data = (Map) data.get("sync_data");
        Boolean value = (Boolean) sync_data.get(VALUE);
        updateValue(value);
      }
    });
  }

  private void updateValue(Boolean value) {
    this.value = value;
  }

  @Override
  public Comm getComm() {
    return this.comm;
  }

  private HashMap<String, Serializable> content() {
    HashMap<String, Serializable> content = new HashMap<>();
    content.put("_model_module", _model_module);
    content.put("_model_name", _model_name);
    content.put("_view_module", _view_module);
    content.put("_view_name", _view_name);
    content.put(LAYOUT, IPY_MODEL + layout.getComm().getCommId());
    content.put(VALUE, this.value);

    content.put(DESCRIPTION, this.getDescription());
    content.put(DISABLED, this.getDisabled());
    content.put(VISIBLE, this.getVisible());
    content.put(MSG_THROTTLE, this.msg_throttle);

    content.put("background_color", null);
    content.put("font_family", "");
    content.put("font_size", "");
    content.put("font_style", "");
    content.put("font_weight", "");

    content.put(TOOLTIP, this.tooltip);
    content.put("color", null);
    content.put("button_style", "");
    content.put("icon", "");

    return content;
  }


  public Boolean getValue() {
    return value;
  }

  public void setValue(Boolean value) {
    this.value = value;
    sendUpdate(VALUE, value);
  }

  public Integer getMsg_throttle() {
    return msg_throttle;
  }

  public void setMsg_throttle(Integer msg_throttle) {
    this.msg_throttle = msg_throttle;
    sendUpdate(MSG_THROTTLE, msg_throttle);
  }

  public String getTooltip() {
    return tooltip;
  }

  public void setTooltip(String tooltip) {
    this.tooltip = tooltip;
    sendUpdate(TOOLTIP, tooltip);
  }
}
