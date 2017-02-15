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

import com.twosigma.beaker.chart.xychart.plotitem.Line;
import com.twosigma.beaker.jupyter.GroovyKernelManager;
import com.twosigma.beaker.widgets.GroovyKernelTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static com.twosigma.beaker.chart.serializer.ChartSerializer.CHART_TITLE;
import static com.twosigma.beaker.chart.serializer.ChartSerializer.SHOW_LEGEND;
import static com.twosigma.beaker.chart.serializer.XYChartSerializer.GRAPHICS_LIST;
import static com.twosigma.beaker.widgets.InternalWidgetsTestUtils.verifyOpenCommMsgInternalWidgets;
import static com.twosigma.beaker.widgets.TestWidgetUtils.RESULT_JSON_JOINER;
import static com.twosigma.beaker.widgets.TestWidgetUtils.getValueForProperty;
import static org.assertj.core.api.Assertions.assertThat;

public class TimePlotTest {

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
    new TimePlot();
    //then
    verifyOpenCommMsgInternalWidgets(groovyKernel.getMessages(), TimePlot.MODEL_NAME_VALUE, TimePlot.VIEW_NAME_VALUE);
  }

  @Test
  public void shouldSendCommMsgWhenCategoryGraphicsChange() throws Exception {
    //given
    TimePlot timePlot = timePlot();
    //when
    timePlot.leftShift(new Line());
    //then
    String valueForProperty = getValueForProperty(groovyKernel, GRAPHICS_LIST, String.class);
    assertThat(valueForProperty).isNotNull();
    assertThat(valueForProperty).contains(GRAPHICS_LIST);
  }

  @Test
  public void shouldSendCommMsgWhenTitleChange() throws Exception {
    //given
    TimePlot timePlot = timePlot();
    //Histogram
    timePlot.setTitle("Title 1");
    //then
    String valueForProperty = getValueForProperty(groovyKernel, CHART_TITLE, String.class);
    assertThat(valueForProperty).contains(CHART_TITLE+RESULT_JSON_JOINER+"\"Title 1");
  }

  @Test
  public void shouldSendCommMsgWhenShowLegendChange() throws Exception {
    //given
    TimePlot timePlot = timePlot();
    //when
    timePlot.setShowLegend(true);
    //then
    String valueForProperty = getValueForProperty(groovyKernel, SHOW_LEGEND, String.class);
    assertThat(valueForProperty).contains(SHOW_LEGEND + RESULT_JSON_JOINER + true);
  }

  private TimePlot timePlot() throws NoSuchAlgorithmException {
    TimePlot widget = new TimePlot();
    groovyKernel.clearMessages();
    return widget;
  }

}