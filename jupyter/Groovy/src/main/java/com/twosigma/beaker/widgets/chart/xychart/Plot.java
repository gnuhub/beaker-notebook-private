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

import com.twosigma.beaker.chart.Chart;
import com.twosigma.beaker.chart.xychart.XYChart;
import com.twosigma.beaker.chart.xychart.plotitem.*;
import com.twosigma.beaker.jupyter.Comm;
import com.twosigma.beaker.widgets.CommFunctionality;
import com.twosigma.beaker.widgets.chart.InternalPlot;
import com.twosigma.beaker.widgets.internal.InternalWidget;
import com.twosigma.beaker.widgets.internal.InternalWidgetContent;
import com.twosigma.beaker.widgets.internal.InternalWidgetUtils;
import com.twosigma.beaker.widgets.internal.SerializeToString;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static com.twosigma.beaker.chart.serializer.ChartSerializer.CHART_TITLE;
import static com.twosigma.beaker.chart.serializer.XYChartSerializer.GRAPHICS_LIST;

public class Plot extends com.twosigma.beaker.chart.xychart.Plot implements CommFunctionality, InternalWidget, InternalPlot {

  private Comm comm;

  public Plot() throws NoSuchAlgorithmException {
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

  @Override
  public XYChart add(XYGraphics graphics) {
    XYChart xyChart = super.add(graphics);
    sendUpdate(GRAPHICS_LIST, SerializeToString.toJson(xyChart));
    return xyChart;
  }

  @Override
  public Chart setTitle(String title) {
    Chart chart = super.setTitle(title);
    sendUpdate(CHART_TITLE, SerializeToString.toJson(chart));
    return chart;
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
