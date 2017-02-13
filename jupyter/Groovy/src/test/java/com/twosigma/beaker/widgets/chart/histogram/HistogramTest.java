
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

import com.twosigma.beaker.chart.Color;
import com.twosigma.beaker.chart.serializer.HistogramSerializer;
import com.twosigma.beaker.jupyter.GroovyKernelManager;
import com.twosigma.beaker.widgets.GroovyKernelTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import static com.twosigma.beaker.chart.serializer.AbstractChartSerializer.DOMAIN_AXIS_LABEL;
import static com.twosigma.beaker.chart.serializer.AbstractChartSerializer.Y_LABEL;
import static com.twosigma.beaker.chart.serializer.ChartSerializer.CHART_TITLE;
import static com.twosigma.beaker.widgets.TestWidgetUtils.RESULT_JSON_JOINER;
import static com.twosigma.beaker.widgets.TestWidgetUtils.getValueForProperty;
import static com.twosigma.beaker.widgets.TestWidgetUtils.verifyOpenCommMsgInternalWidgets;
import static org.assertj.core.api.Assertions.assertThat;

public class HistogramTest {

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
    new Histogram();
    //then
    verifyOpenCommMsgInternalWidgets(groovyKernel.getMessages(), Histogram.MODEL_NAME_VALUE, Histogram.VIEW_NAME_VALUE);
  }

  @Test
  public void shouldSendCommMsgWhenDataChange() throws Exception {
    //given
    Histogram histogram = histogram();
    List<Number> data = Arrays.asList(new Number[]{1, 2, 34, 5, 6});
    //when
    histogram.setData(data);
    //then
    String valueForProperty = getValueForProperty(groovyKernel, HistogramSerializer.GRAPHICS_LIST, String.class);
    assertThat(valueForProperty).contains(HistogramSerializer.GRAPHICS_LIST);
  }

  @Test
  public void shouldSendCommMsgWhenBinCountChange() throws Exception {
    //given
    Histogram histogram = histogram();
    //when
    histogram.setBinCount(33);
    //then
    String valueForProperty = getValueForProperty(groovyKernel, HistogramSerializer.BIN_COUNT, String.class);
    assertThat(valueForProperty).contains(HistogramSerializer.BIN_COUNT + RESULT_JSON_JOINER + 33);
  }

  @Test
  public void shouldSendCommMsgWhenTitleChange() throws Exception {
    //given
    Histogram histogram = histogram();
    //Histogram
    histogram.setTitle("Title 1");
    //then
    String valueForProperty = getValueForProperty(groovyKernel, CHART_TITLE, String.class);
    assertThat(valueForProperty).contains(CHART_TITLE);
    assertThat(valueForProperty).contains("Title 1");
  }

  @Test
  public void shouldSendCommMsgWhenXLabelChange() throws Exception {
    //given
    Histogram histogram = histogram();
    //when
    histogram.setXLabel("X label 1");
    //then
    String valueForProperty = getValueForProperty(groovyKernel, DOMAIN_AXIS_LABEL, String.class);
    assertThat(valueForProperty).contains(DOMAIN_AXIS_LABEL);
    assertThat(valueForProperty).contains("X label 1");
  }

  @Test
  public void shouldSendCommMsgWhenYLabelChange() throws Exception {
    //given
    Histogram histogram = histogram();
    //when
    histogram.setYLabel("Y label 1");
    //then
    String valueForProperty = getValueForProperty(groovyKernel, Y_LABEL, String.class);
    assertThat(valueForProperty).contains(Y_LABEL);
    assertThat(valueForProperty).contains("Y label 1");
  }

  @Test
  public void shouldSendCommMsgWhenColorChange() throws Exception {
    //given
    Histogram histogram = histogram();
    //when
    histogram.setColor(new Color(0, 154, 166));
    //then
    String valueForProperty = getValueForProperty(groovyKernel, HistogramSerializer.COLOR, String.class);
    assertThat(valueForProperty).contains(HistogramSerializer.COLOR);
  }

  @Test
  public void shouldSendCommMsgWhenInitWidthChange() throws Exception {
    //given
    Histogram histogram = histogram();
    //when
    histogram.setInitWidth(123);
    //then
    String valueForProperty = getValueForProperty(groovyKernel, HistogramSerializer.INIT_WIDTH, String.class);
    assertThat(valueForProperty).contains(HistogramSerializer.INIT_WIDTH + RESULT_JSON_JOINER + 123);
  }

  @Test
  public void shouldSendCommMsgWhenInitHeightChange() throws Exception {
    //given
    Histogram histogram = histogram();
    //when
    histogram.setInitHeight(321);
    //then
    String valueForProperty = getValueForProperty(groovyKernel, HistogramSerializer.INIT_HEIGHT, String.class);
    assertThat(valueForProperty).contains(HistogramSerializer.INIT_HEIGHT + RESULT_JSON_JOINER + 321);
  }

  private Histogram histogram() throws NoSuchAlgorithmException {
    Histogram widget = new Histogram();
    groovyKernel.clearMessages();
    return widget;
  }


}