
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

import com.twosigma.beaker.chart.serializer.XYGraphicsSerializer;
import com.twosigma.beaker.jupyter.GroovyKernelManager;
import com.twosigma.beaker.widgets.GroovyKernelTest;
import org.apache.commons.collections.map.HashedMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.twosigma.beaker.chart.serializer.AbstractChartSerializer.Y_LABEL;
import static com.twosigma.beaker.widgets.InternalWidgetsTestUtils.verifyOpenCommMsgInternalWidgets;
import static com.twosigma.beaker.widgets.TestWidgetUtils.RESULT_JSON_JOINER;
import static com.twosigma.beaker.widgets.TestWidgetUtils.getValueForProperty;
import static org.assertj.core.api.Assertions.assertThat;

public class SimpleTimePlotTest {

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
    List<Map<String, Object>> data = new ArrayList<>();
    List<String> columns = new ArrayList<>();
    //when
    new SimpleTimePlot(data, columns);
    //then
    verifyOpenCommMsgInternalWidgets(groovyKernel.getMessages(), SimpleTimePlot.MODEL_NAME_VALUE, SimpleTimePlot.VIEW_NAME_VALUE);
  }

  @Test
  public void shouldSendCommMsgWhenYLabelChange() throws Exception {
    //given
    SimpleTimePlot plot = simpleTimePlot();
    //when
    plot.setYLabel("Y label 1");
    //then
    String valueForProperty = getValueForProperty(groovyKernel, Y_LABEL, String.class);
    assertThat(valueForProperty).contains(Y_LABEL);
    assertThat(valueForProperty).contains("Y label 1");
  }

  @Test
  public void shouldSendCommMsgWhenDisplayNamesChange() throws Exception {
    //given
    SimpleTimePlot widget = simpleTimePlot();
    //when
    widget.setDisplayNames(Arrays.asList("All","DN1", "DN2"));
    //then
    String valueForProperty = getValueForProperty(groovyKernel, XYGraphicsSerializer.DISPLAY_NAME, String.class);
    assertThat(valueForProperty).contains(XYGraphicsSerializer.DISPLAY_NAME + RESULT_JSON_JOINER + "\"DN1\"");
    assertThat(valueForProperty).contains(XYGraphicsSerializer.DISPLAY_NAME + RESULT_JSON_JOINER + "\"DN1\"");
  }


  private SimpleTimePlot simpleTimePlot() throws NoSuchAlgorithmException {
    List<Map<String, Object>> data = new ArrayList<>();
    data.add(new HashedMap(){{
      put("c1",1);
      put("c2",2);
      put("time",new Date());
    }});
    List<String> columns = Arrays.asList("c1", "c2");
    SimpleTimePlot widget = new SimpleTimePlot(data, columns);
    groovyKernel.clearMessages();
    return widget;
  }

}