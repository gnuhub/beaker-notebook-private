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
package com.twosigma.beaker.widgets.chart.categoryplot;

import com.twosigma.beaker.chart.categoryplot.plotitem.CategoryBars;
import com.twosigma.beaker.jupyter.GroovyKernelManager;
import com.twosigma.beaker.widgets.GroovyKernelTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static com.twosigma.beaker.chart.serializer.CategoryPlotSerializer.GRAPHICS_LIST;
import static com.twosigma.beaker.widgets.TestWidgetUtils.getValueForProperty;
import static com.twosigma.beaker.widgets.TestWidgetUtils.verifyOpenCommMsgInternalWidgets;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryPlotTest {

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
    new CategoryPlot();
    //then
    verifyOpenCommMsgInternalWidgets(groovyKernel.getMessages(), CategoryPlot.MODEL_NAME_VALUE, CategoryPlot.VIEW_NAME_VALUE);
  }

  @Test
  public void shouldSendCommMsgWhenCategoryGraphicsChange() throws Exception {
    //given
    CategoryPlot categoryPlot = categoryPlot();
    //when
    categoryPlot.leftShift(new CategoryBars());
    //then
    String valueForProperty = getValueForProperty(groovyKernel, GRAPHICS_LIST, String.class);
    assertThat(valueForProperty).isNotNull();
    assertThat(valueForProperty).contains(GRAPHICS_LIST);
  }

  private CategoryPlot categoryPlot() throws NoSuchAlgorithmException {
    CategoryPlot categoryPlot = new CategoryPlot();
    groovyKernel.clearMessages();
    return categoryPlot;
  }

}