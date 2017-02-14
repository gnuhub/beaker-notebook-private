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
package com.twosigma.beaker.widgets.chart.heatmap;

import com.twosigma.beaker.chart.AbstractChart;
import com.twosigma.beaker.chart.Chart;
import com.twosigma.beaker.chart.GradientColor;
import com.twosigma.beaker.chart.legend.LegendPosition;
import com.twosigma.beaker.chart.serializer.HeatMapSerializer;
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

import static com.twosigma.beaker.chart.serializer.ChartSerializer.SHOW_LEGEND;
import static com.twosigma.beaker.chart.serializer.ChartSerializer.USE_TOOL_TIP;

public class HeatMap extends com.twosigma.beaker.chart.heatmap.HeatMap implements CommFunctionality, InternalWidget, InternalPlot {

  private Comm comm;

  public HeatMap() throws NoSuchAlgorithmException {
    this.comm = InternalWidgetUtils.createComm(this, new InternalWidgetContent() {
      @Override
      public void addContent(HashMap<String, Serializable> content) {
        content.put(InternalWidgetUtils.MODEL_NAME, MODEL_NAME_VALUE);
        content.put(InternalWidgetUtils.VIEW_NAME, VIEW_NAME_VALUE);
      }
    });
  }

  @Override
  public void setData(Number[][] data) {
    super.setData(data);
    sendUpdate(HeatMapSerializer.GRAPHICS_LIST, SerializeToString.toJson(this));
  }

  @Override
  public Chart setTitle(String title) {
    Chart chart = super.setTitle(title);
    sendUpdate(HeatMapSerializer.CHART_TITLE, SerializeToString.toJson(this));
    return chart;
  }

  @Override
  public AbstractChart setXLabel(String xLabel) {
    AbstractChart abstractChart = super.setXLabel(xLabel);
    sendUpdate(HeatMapSerializer.DOMAIN_AXIS_LABEL, SerializeToString.toJson(this));
    return abstractChart;
  }

  @Override
  public AbstractChart setxLabel(String xLabel) {
    return setXLabel(xLabel);
  }

  @Override
  public AbstractChart setYLabel(String yLabel) {
    AbstractChart abstractChart = super.setYLabel(yLabel);
    sendUpdate(HeatMapSerializer.Y_LABEL, SerializeToString.toJson(this));
    return abstractChart;
  }

  @Override
  public Chart setLegendPosition(LegendPosition legendPosition) {
    Chart chart = super.setLegendPosition(legendPosition);
    sendUpdate(HeatMapSerializer.LEGEND_POSITION, SerializeToString.toJson(this));
    return chart;
  }

  @Override
  public Chart setShowLegend(Boolean showLegend) {
    Chart chart = super.setShowLegend(showLegend);
    sendUpdate(SHOW_LEGEND, SerializeToString.toJson(this));
    return chart;
  }

  @Override
  public AbstractChart setyLabel(String yLabel) {
    return setYLabel(yLabel);
  }

  @Override
  public void setColor(GradientColor color) {
    super.setColor(color);
    sendUpdate(HeatMapSerializer.COLOR, SerializeToString.toJson(this));
  }

  @Override
  public Chart setUseToolTip(boolean useToolTip) {
    Chart chart = super.setUseToolTip(useToolTip);
    sendUpdate(USE_TOOL_TIP, SerializeToString.toJson(this));
    return chart;
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

}