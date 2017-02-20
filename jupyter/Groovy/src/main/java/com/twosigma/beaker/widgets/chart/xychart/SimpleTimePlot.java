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
package com.twosigma.beaker.widgets.chart.xychart;

import com.twosigma.beaker.jupyter.Comm;
import com.twosigma.beaker.widgets.CommFunctionality;
import com.twosigma.beaker.widgets.chart.InternalPlot;
import com.twosigma.beaker.widgets.internal.InternalWidget;
import com.twosigma.beaker.widgets.internal.InternalWidgetContent;
import com.twosigma.beaker.widgets.internal.InternalWidgetUtils;
import com.twosigma.beaker.widgets.internal.SerializeToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleTimePlot extends com.twosigma.beaker.chart.xychart.SimpleTimePlot  implements CommFunctionality, InternalWidget, InternalPlot {

  private Comm comm;

  public SimpleTimePlot(List<Map<String, Object>> data, List<String> columns) {
    this(null, data, columns);
  }

  public SimpleTimePlot(Map<String, Object> parameters, List<Map<String, Object>> data, List<String> columns) {
    super(parameters, data, columns);
    this.comm = InternalWidgetUtils.createComm(this, new InternalWidgetContent() {
      @Override
      public void addContent(HashMap<String, Serializable> content) {
        content.put(InternalWidgetUtils.MODEL_NAME, MODEL_NAME_VALUE);
        content.put(InternalWidgetUtils.VIEW_NAME, VIEW_NAME_VALUE);
      }
    });
  }

  @Override
  public Comm getComm() {
    return this.comm;
  }

  private void sendUpdate(final String propertyName, final Object value) {
    if (this.comm != null) {
      this.comm.sendUpdate(propertyName, value);
    }
  }

  @Override
  public void sendModel() {
    sendUpdate(MODEL, SerializeToString.toJson(this));
  }
}
