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
package com.twosigma.beaker.widgets.chart.histogram;

import com.twosigma.beaker.chart.AbstractChart;
import com.twosigma.beaker.chart.Chart;
import com.twosigma.beaker.chart.Color;
import com.twosigma.beaker.chart.serializer.HistogramSerializer;
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
import java.util.List;

public class Histogram extends com.twosigma.beaker.chart.histogram.Histogram implements CommFunctionality, InternalWidget, InternalPlot {

  private Comm comm;

  public Histogram() throws NoSuchAlgorithmException {
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
  public void setData(Object data) {
    super.setData(data);
    sendUpdate(HistogramSerializer.GRAPHICS_LIST, SerializeToString.toJson(this));
  }

  @Override
  public void setBinCount(int binCount) {
    super.setBinCount(binCount);
    sendUpdate(HistogramSerializer.BIN_COUNT, SerializeToString.toJson(this));
  }

  @Override
  public Chart setTitle(String title) {
    Chart chart = super.setTitle(title);
    sendUpdate(HistogramSerializer.CHART_TITLE, SerializeToString.toJson(this));
    return chart;
  }

  @Override
  public AbstractChart setXLabel(String xLabel) {
    AbstractChart abstractChart = super.setXLabel(xLabel);
    sendUpdate(HistogramSerializer.DOMAIN_AXIS_LABEL, SerializeToString.toJson(this));
    return abstractChart;
  }

  @Override
  public AbstractChart setxLabel(String xLabel) {
    return setXLabel(xLabel);
  }

  @Override
  public AbstractChart setYLabel(String yLabel) {
    AbstractChart abstractChart = super.setYLabel(yLabel);
    sendUpdate(HistogramSerializer.Y_LABEL, SerializeToString.toJson(this));
    return abstractChart;
  }

  @Override
  public void setColor(Object color) {
    super.setColor(color);
    sendUpdate((color instanceof Color) ? HistogramSerializer.COLOR : HistogramSerializer.COLORS, SerializeToString.toJson(this));
  }

  @Override
  public Chart setInitWidth(int w) {
    Chart chart = super.setInitWidth(w);
    sendUpdate(HistogramSerializer.INIT_WIDTH, SerializeToString.toJson(this));
    return chart;
  }

  @Override
  public Chart setInitHeight(int h) {
    Chart chart = super.setInitHeight(h);
    sendUpdate(HistogramSerializer.INIT_HEIGHT, SerializeToString.toJson(this));
    return chart;
  }

  @Override
  public void setNames(List<String> names) {
    super.setNames(names);
    sendUpdate(HistogramSerializer.NAMES, SerializeToString.toJson(this));
  }

  @Override
  public void setDisplayMode(DisplayMode displayMode) {
    super.setDisplayMode(displayMode);
    sendUpdate(HistogramSerializer.DISPLAY_MODE, SerializeToString.toJson(this));
  }

  @Override
  public void setCumulative(boolean cumulative) {
    super.setCumulative(cumulative);
    sendUpdate(HistogramSerializer.CUMULATIVE, SerializeToString.toJson(this));
  }

  @Override
  public void setNormed(boolean normed) {
    super.setNormed(normed);
    sendUpdate(HistogramSerializer.NORMED, SerializeToString.toJson(this));
  }

  @Override
  public void setLog(boolean log) {
    super.setLog(log);
    sendUpdate(HistogramSerializer.LOG, SerializeToString.toJson(this));
  }

  private void sendUpdate(final String propertyName, final Object value) {
    if (this.comm != null) {
      this.comm.sendUpdate(propertyName, value);
    }
  }
}
