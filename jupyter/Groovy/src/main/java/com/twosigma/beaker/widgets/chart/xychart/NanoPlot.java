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

import com.twosigma.beaker.chart.xychart.XYChart;
import com.twosigma.beaker.chart.xychart.plotitem.XYGraphics;
import com.twosigma.beaker.jupyter.Comm;
import com.twosigma.beaker.widgets.chart.InternalPlot;
import com.twosigma.beaker.widgets.internal.InternalWidget;
import com.twosigma.beaker.widgets.internal.InternalWidgetUtils;

import java.security.NoSuchAlgorithmException;

public class NanoPlot extends com.twosigma.beaker.chart.xychart.NanoPlot implements InternalWidget, InternalPlot {

  private Comm comm;

  public NanoPlot() throws NoSuchAlgorithmException {
    this.comm = InternalWidgetUtils.createComm(this);
  }

  @Override
  public XYChart add(XYGraphics graphics) {
    super.add(graphics);
    graphics.setPlotType(com.twosigma.beaker.chart.xychart.NanoPlot.class);
    return this;
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
