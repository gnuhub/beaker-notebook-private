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
package com.twosigma.beaker.widgets.easyform;

import com.twosigma.beaker.jupyter.Comm;
import com.twosigma.beaker.widgets.internal.InternalWidget;
import com.twosigma.beaker.widgets.internal.InternalWidgetUtils;

public class EasyForm extends com.twosigma.beaker.easyform.EasyForm implements InternalWidget {

  public static final String VIEW_NAME_VALUE = "EasyFormView";
  public static final String MODEL_NAME_VALUE = "EasyFormModel";

  private Comm comm;

  public EasyForm() {
    this("");
  }

  public EasyForm(String caption) {
    super(caption);
    this.comm = InternalWidgetUtils.createComm(this);
  }

  @Override
  public Comm getComm() {
    return this.comm;
  }

  @Override
  public String getModelNameValue() {
    return MODEL_NAME_VALUE;
  }

  @Override
  public String getViewNameValue() {
    return VIEW_NAME_VALUE;
  }
}
