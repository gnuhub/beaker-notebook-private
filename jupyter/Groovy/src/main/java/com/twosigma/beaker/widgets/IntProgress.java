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

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static com.twosigma.beaker.widgets.Layout.IPY_MODEL;
import static com.twosigma.beaker.widgets.Layout.LAYOUT;

public class IntProgress extends BoundedIntWidget {

  protected static final String STEP = "step";
  protected static final String MAX = "max";
  protected static final String MIN = "min";

  protected static final String ORIENTATION = "orientation";

  private String _view_name = "ProgressView";
  private String _model_name = "ProgressModel";
  private String _model_module = "jupyter-js-widgets";
  private String _view_module = "jupyter-js-widgets";

  private Comm comm;
  private Layout layout;

  private String orientation = "horizontal";

  public IntProgress() throws NoSuchAlgorithmException {
    comm = new Comm(Utils.uuid(), CommNamesEnum.JUPYTER_WIDGET);
    layout = new Layout();
    openComm(comm);
  }

  private void openComm(final Comm comm) throws NoSuchAlgorithmException {
    comm.setData(content());
    addValueChangeMsgCallback(comm);
    comm.open();
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
    content.put(VALUE, this.getValue());

    content.put(DESCRIPTION, this.getDescription());
    content.put(DISABLED, this.getDisabled());
    content.put(MAX, this.getMax());
    content.put(MIN, this.getMin());
    content.put(ORIENTATION, orientation);
    content.put(STEP, this.getStep());
    content.put(VISIBLE, this.getVisible());
    content.put(MSG_THROTTLE, this.getMsg_throttle());

    content.put("background_color", null);
    content.put("font_family", "");
    content.put("font_size", "");
    content.put("font_style", "");
    content.put("font_weight", "");

    content.put("bar_style", "");
    content.put("color", null);

    return content;
  }

  public String getOrientation() {
    return orientation;
  }

  public void setOrientation(String orientation) {
    this.orientation = orientation;
    sendUpdate(ORIENTATION, orientation);
  }

}
