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
import com.twosigma.beaker.jupyter.CommNamesEnum;
import com.twosigma.beaker.jupyter.Utils;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


public class IntSlider {

  String _view_name = "IntSliderView";
  String _model_name = "IntSliderModel";
  String _model_module = "jupyter-js-widgets";

  private Comm comm;

  public IntSlider() {
    try {
      comm = getComm();
      comm.setData(openContent());
      comm.open();

//    HashMap<String, Serializable> content = new HashMap<>();
//    content.put("method","display");
//    comm.setData(content);
//    comm.send();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  public Comm getComm() {
    if (comm == null) {
      comm = new Comm(Utils.uuid(), CommNamesEnum.JUPYTER_WIDGET);
    }
    return comm;
  }

  private HashMap<String, Serializable> openContent() {
    HashMap<String, Serializable> content = new HashMap<>();
    content.put("_model_module", _model_module);
    content.put("_model_name", _model_name);
    content.put("_range", false);
    content.put("_view_module", "jupyter-js-widgets");
    content.put("_view_name", _view_name);
    content.put("background_color", null);
    content.put("continuous_update", true);
    content.put("description", "");
    content.put("disabled", false);
    content.put("font_family", "");
    content.put("font_size", "");
    content.put("font_style", "");
    content.put("font_weight", "");
    content.put("layout", "???");
    content.put("max", 100);
    content.put("min", 0);
    content.put("msg_throttle", 3);
    content.put("orientation", "horizontal");
    content.put("readout", true);
    content.put("readout_format", "d");
    content.put("slider_color", null);
    content.put("step", 1);
    content.put("value", 0);
    content.put("visible", true);
    return content;
  }

}
