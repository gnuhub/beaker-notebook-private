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

public class IntSlider extends Widget {

  public static final String DISABLED = "disabled";
  public static final String VALUE = "value";
  public static final String STEP = "step";
  public static final String ORIENTATION = "orientation";
  public static final String MAX = "max";
  public static final String MIN = "min";
  public static final String VISIBLE = "visible";
  public static final String DESCRIPTION = "description";

  private String _view_name = "IntSliderView";
  private String _model_name = "IntSliderModel";
  private String _model_module = "jupyter-js-widgets";
  private String _view_module = "jupyter-js-widgets";

  private Comm comm;
  private Layout layout;
  private Integer value = 0;
  private Boolean disabled = false;
  private Integer step = 1;
  private String orientation = "horizontal";
  private Integer max = 100;
  private Integer min = 0;
  private Boolean visible = true;
  private String description="";

  public IntSlider() throws NoSuchAlgorithmException {
    comm = new Comm(Utils.uuid(), CommNamesEnum.JUPYTER_WIDGET);
    layout = new Layout();
    openComm(comm);
  }

  @Override
  public Comm getComm() {
    return comm;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
    sendUpdate(VALUE, value);
  }

  public boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
    sendUpdate(DISABLED, disabled);
  }

  public Integer getStep() {
    return step;
  }

  public void setStep(Integer step) {
    this.step = step;
    sendUpdate(STEP, step);
  }

  public String getOrientation() {
    return orientation;
  }

  public void setOrientation(String orientation) {
    this.orientation = orientation;
    sendUpdate(ORIENTATION, orientation);
  }

  public Integer getMax() {
    return max;
  }

  public void setMax(Integer max) {
    this.max = max;
    sendUpdate(MAX, max);
  }

  public Integer getMin() {
    return min;
  }

  public void setMin(Integer min) {
    this.min = min;
    sendUpdate(MIN, min);
  }

  public Boolean getVisible() {
    return visible;
  }

  public void setVisible(Boolean visible) {
    this.visible = visible;
    sendUpdate(VISIBLE, visible);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    sendUpdate(DESCRIPTION, description);
    this.description = description;
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
        int value = (int) sync_data.get(VALUE);
        updateValue(value);
      }
    });
  }

  private void updateValue(int value) {
    this.value = value;
  }

  private HashMap<String, Serializable> content() {
    HashMap<String, Serializable> content = new HashMap<>();
    content.put("_model_module", _model_module);
    content.put("_model_name", _model_name);
    content.put("_view_module", _view_module);
    content.put("_view_name", _view_name);
    content.put("_range", false);
    content.put("layout", "IPY_MODEL_" + layout.getComm().getCommId());
    content.put("background_color", null);
    content.put("continuous_update", true);
    content.put(DESCRIPTION, this.description);
    content.put(DISABLED, this.disabled);
    content.put("font_family", "");
    content.put("font_size", "");
    content.put("font_style", "");
    content.put("font_weight", "");
    content.put(MAX, this.max);
    content.put(MIN, this.min);
    content.put("msg_throttle", 3);
    content.put(ORIENTATION, orientation);
    content.put("readout", true);
    content.put("readout_format", "d");
    content.put("slider_color", null);
    content.put(STEP, 1);
    content.put(VALUE, value);
    content.put(VISIBLE, this.visible);
    return content;
  }

}
