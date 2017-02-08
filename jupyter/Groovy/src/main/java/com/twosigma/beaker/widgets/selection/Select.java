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
package com.twosigma.beaker.widgets.selection;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static com.twosigma.beaker.widgets.Layout.IPY_MODEL;
import static com.twosigma.beaker.widgets.Layout.LAYOUT;

public class Select extends SelectionWidget {

  public static String VIEW_NAME_VALUE = "SelectView";
  public static String MODEL_NAME_VALUE = "SelectModel";

  public Select() throws NoSuchAlgorithmException {
  }

  @Override
  protected HashMap<String, Serializable> content() {

    HashMap<String, Serializable> content = new HashMap<>();
    content.put(MODEL_MODULE, MODEL_MODULE_VALUE);
    content.put(MODEL_NAME, MODEL_NAME_VALUE);
    content.put(VIEW_MODULE, VIEW_MODULE_VALUE);
    content.put(VIEW_NAME, VIEW_NAME_VALUE);
    content.put(LAYOUT, IPY_MODEL + getLayout().getComm().getCommId());
    content.put(VALUE, this.getValue());

    content.put(DESCRIPTION, this.getDescription());
    content.put(DISABLED, this.getDisabled());
    content.put(VISIBLE, this.getVisible());
    content.put(MSG_THROTTLE, this.getMsg_throttle());

    content.put(OPTIONS_LABELS, this.getOptions());

    content.put("background_color", null);
    content.put("font_family", "");
    content.put("font_size", "");
    content.put("font_style", "");
    content.put("font_weight", "");
    content.put("color", null);

    return content;
  }

}
