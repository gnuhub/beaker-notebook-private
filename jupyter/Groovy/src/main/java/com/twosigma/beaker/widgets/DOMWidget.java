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

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public abstract class DOMWidget extends Widget {

  private Layout layout;

  public DOMWidget() throws NoSuchAlgorithmException {
    super();
    layout = new Layout();
  }

  @Override
  protected HashMap<String, Serializable> content(HashMap<String, Serializable> content) {
    content.put(Layout.LAYOUT, Layout.IPY_MODEL + layout.getComm().getCommId());
    content.put("font_family", "");
    content.put("font_size", "");
    content.put("font_style", "");
    content.put("font_weight", "");
    content.put("background_color", null);
    content.put("color", null);
    return content;
  }

  public Layout getLayout() {
    return layout;
  }
}
