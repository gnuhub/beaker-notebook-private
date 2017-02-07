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

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public abstract class Widget {

  public static final String MODEL_MODULE = "_model_module";
  public static final String MODEL_NAME = "_model_name";
  public static final String VIEW_MODULE = "_view_module";
  public static final String VIEW_NAME = "_view_name";

  public static final String MODEL_MODULE_VALUE = "jupyter-js-widgets";
  public static final String VIEW_MODULE_VALUE = "jupyter-js-widgets";
  public static final String METHOD = "method";
  public static final String UPDATE = "update";
  public static final String STATE = "state";

  public static final String VALUE = "value";
  public static final String DISABLED = "disabled";
  public static final String VISIBLE = "visible";
  public static final String DESCRIPTION = "description";
  public static final String MSG_THROTTLE = "msg_throttle";

  private Boolean disabled = false;
  private Boolean visible = true;
  private String description = "";
  private Integer msg_throttle = 3;

  public abstract Comm getComm();

  public void sendUpdate(String propertyName, Object value) {
    HashMap<String, Serializable> content = new HashMap<>();
    content.put(METHOD, UPDATE);
    HashMap<Object, Object> state = new HashMap<>();
    state.put(propertyName, value);
    content.put(STATE, state);
    getComm().setData(content);
    try {
      getComm().send();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public Boolean getDisabled() {
    return disabled;
  }

  public void setDisabled(Boolean disabled) {
    this.disabled = disabled;
    sendUpdate(DISABLED, disabled);
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

  public Integer getMsg_throttle() {
    return msg_throttle;
  }

  public void setMsg_throttle(Integer msg_throttle) {
    this.msg_throttle = msg_throttle;
    sendUpdate(MSG_THROTTLE, msg_throttle);
  }

}
