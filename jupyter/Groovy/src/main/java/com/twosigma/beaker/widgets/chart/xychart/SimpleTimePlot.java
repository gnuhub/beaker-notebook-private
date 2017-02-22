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
import com.twosigma.beaker.widgets.chart.InternalPlot;
import com.twosigma.beaker.widgets.internal.InternalWidget;
import com.twosigma.beaker.widgets.internal.InternalWidgetUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleTimePlot extends com.twosigma.beaker.chart.xychart.SimpleTimePlot implements InternalWidget, InternalPlot {

  private Comm comm;

  public SimpleTimePlot() {
    this(null, new ArrayList<Map<String, Object>>(), new ArrayList<String>());
  }

  public SimpleTimePlot(List<Map<String, Object>> data, List<String> columns) {
    this(null, data, columns);
  }

  public SimpleTimePlot(Map<String, Object> parameters, List<Map<String, Object>> data, List<String> columns) {
    super(parameters, data, columns);
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
