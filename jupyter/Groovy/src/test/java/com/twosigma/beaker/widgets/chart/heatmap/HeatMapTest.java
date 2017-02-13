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

import com.twosigma.beaker.chart.GradientColor;
import com.twosigma.beaker.chart.legend.LegendPosition;
import com.twosigma.beaker.chart.serializer.HeatMapSerializer;
import com.twosigma.beaker.jupyter.GroovyKernelManager;
import com.twosigma.beaker.widgets.GroovyKernelTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static com.twosigma.beaker.chart.serializer.AbstractChartSerializer.DOMAIN_AXIS_LABEL;
import static com.twosigma.beaker.chart.serializer.AbstractChartSerializer.Y_LABEL;
import static com.twosigma.beaker.chart.serializer.CategoryPlotSerializer.GRAPHICS_LIST;
import static com.twosigma.beaker.chart.serializer.ChartSerializer.*;
import static com.twosigma.beaker.chart.serializer.HeatMapSerializer.COLOR;
import static com.twosigma.beaker.widgets.TestWidgetUtils.getValueForProperty;
import static com.twosigma.beaker.widgets.TestWidgetUtils.verifyOpenCommMsgInternalWidgets;
import static org.assertj.core.api.Assertions.assertThat;

public class HeatMapTest {

  private GroovyKernelTest groovyKernel;

  @Before
  public void setUp() throws Exception {
    groovyKernel = new GroovyKernelTest();
    GroovyKernelManager.register(groovyKernel);
  }

  @After
  public void tearDown() throws Exception {
    GroovyKernelManager.register(null);
  }

  @Test
  public void shouldSendCommOpenWhenCreate() throws Exception {
    //given
    //when
    new HeatMap();
    //then
    verifyOpenCommMsgInternalWidgets(groovyKernel.getMessages(), HeatMap.MODEL_NAME_VALUE, HeatMap.VIEW_NAME_VALUE);
  }

  @Test
  public void shouldSendCommMsgWhenDataChange() throws Exception {
    //given
    HeatMap heatmap = heatmap();
    Number[][] data = new Number[][]{{1, 2, 3}, {4, 5, 6}};
    //when
    heatmap.setData(data);
    //then
    String valueForProperty = getValueForProperty(groovyKernel, HeatMapSerializer.GRAPHICS_LIST, String.class);
    assertThat(valueForProperty).contains(GRAPHICS_LIST);
  }

  @Test
  public void shouldSendCommMsgWhenTitleChange() throws Exception {
    //given
    HeatMap heatmap = heatmap();
    //when
    heatmap.setTitle("Title 1");
    //then
    String valueForProperty = getValueForProperty(groovyKernel, CHART_TITLE, String.class);
    assertThat(valueForProperty).contains(CHART_TITLE);
    assertThat(valueForProperty).contains("Title 1");
  }

  @Test
  public void shouldSendCommMsgWhenXLabelChange() throws Exception {
    //given
    HeatMap heatmap = heatmap();
    //when
    heatmap.setXLabel("X label 1");
    //then
    String valueForProperty = getValueForProperty(groovyKernel, DOMAIN_AXIS_LABEL, String.class);
    assertThat(valueForProperty).contains(DOMAIN_AXIS_LABEL);
    assertThat(valueForProperty).contains("X label 1");
  }

  @Test
  public void shouldSendCommMsgWhenYLabelChange() throws Exception {
    //given
    HeatMap heatmap = heatmap();
    //when
    heatmap.setYLabel("Y label 1");
    //then
    String valueForProperty = getValueForProperty(groovyKernel, Y_LABEL, String.class);
    assertThat(valueForProperty).contains(Y_LABEL);
    assertThat(valueForProperty).contains("Y label 1");
  }

  @Test
  public void shouldSendCommMsgWhenLegendPositionChange() throws Exception {
    //given
    HeatMap heatmap = heatmap();
    //when
    heatmap.setLegendPosition(LegendPosition.LEFT);
    //then
    String valueForProperty = getValueForProperty(groovyKernel, LEGEND_POSITION, String.class);
    assertThat(valueForProperty).contains(LEGEND_POSITION);
    assertThat(valueForProperty).contains(LegendPosition.LEFT.getPosition().name());
  }

  @Test
  public void shouldSendCommMsgWhenShowLegendChange() throws Exception {
    //given
    HeatMap heatmap = heatmap();
    //when
    heatmap.setShowLegend(true);
    //then
    String valueForProperty = getValueForProperty(groovyKernel, SHOW_LEGEND, String.class);
    assertThat(valueForProperty).contains(SHOW_LEGEND+"\":"+true);
  }

  @Test
  public void shouldSendCommMsgWhenColorChange() throws Exception {
    //given
    HeatMap heatmap = heatmap();
    //when
    heatmap.setColor(GradientColor.BROWN_RED_YELLOW);
    //then
    String valueForProperty = getValueForProperty(groovyKernel, COLOR, String.class);
    assertThat(valueForProperty).contains(COLOR);
  }

  @Test
  public void shouldSendCommMsgWhenUseToolTipChange() throws Exception {
    //given
    HeatMap heatmap = heatmap();
    //when
    heatmap.setUseToolTip(true);
    //then
    String valueForProperty = getValueForProperty(groovyKernel, USE_TOOL_TIP, String.class);
    assertThat(valueForProperty).contains(USE_TOOL_TIP+"\":"+true);
  }

  private HeatMap heatmap() throws NoSuchAlgorithmException {
    HeatMap widget = new HeatMap();
    groovyKernel.clearMessages();
    return widget;
  }

}