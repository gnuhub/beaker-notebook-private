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
package com.twosigma.beaker.evaluator;

import com.twosigma.beaker.chart.xychart.Plot;
import com.twosigma.beaker.jupyter.KernelManager;
import com.twosigma.beaker.jvm.object.SimpleEvaluationObject;
import com.twosigma.beaker.table.TableDisplay;
import com.twosigma.beaker.widgets.GroovyKernelTest;
import com.twosigma.beaker.widgets.selectioncontainer.Tab;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lappsgrid.jupyter.msg.Message;

import java.util.List;
import java.util.Map;

import static com.twosigma.beaker.evaluator.GroovyEvaluatorResultTestWatcher.waitForResult;
import static com.twosigma.beaker.jvm.object.SimpleEvaluationObject.EvaluationStatus.FINISHED;
import static com.twosigma.beaker.widgets.TestWidgetUtils.*;
import static com.twosigma.beaker.widgets.Widget.VIEW_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class GroovyOutputContainerTest {

  public static final Message HEADER_MESSAGE = new Message();
  private EvaluatorManager groovyEvaluator;
  private GroovyKernelTest groovyKernel;

  @Before
  public void setUp() throws Exception {
    groovyKernel = new GroovyKernelTest();
    KernelManager.register(groovyKernel);
    groovyEvaluator = new EvaluatorManager(groovyKernel,new GroovyEvaluator("id", "sid"));
  }

  @After
  public void tearDown() throws Exception {
    KernelManager.register(null);
  }

  @Test
  public void shouldAddMapToOutputContainerTest() throws Exception {
    //given
    String code =
            "import com.twosigma.beaker.evaluator.ResourceLoaderTest;\n" +
            "import com.twosigma.beaker.jvm.object.OutputContainer;\n" +
            "List<Map<?, ?>> values = ResourceLoaderTest.readAsList(\"tableRowsTest.csv\");\n" +
            "new OutputContainer() << values.get(0)";

    //when
    SimpleEvaluationObject seo = groovyEvaluator.executeCode(code, HEADER_MESSAGE, 1);
    waitForResult(seo);
    //then
    assertTrue(seo.getPayload().toString(),seo.getStatus().equals(FINISHED));
    verifyMap(groovyKernel.getPublishedMessages());
  }

  private void verifyMap(List<Message> messages) {
    Message tableDisplay = messages.get(0);
    verifyInternalOpenCommMsg(tableDisplay, TableDisplay.MODEL_NAME_VALUE, TableDisplay.VIEW_NAME_VALUE);
    Message model = messages.get(1);
    assertThat(getValueForProperty(model, "model", String.class)).isNotEmpty();
    verifyDisplayMsg(messages.get(2));
  }

  @Test
  public void shouldAddPlotToOutputContainerTest() throws Exception {
    //given
    String code =
            "import com.twosigma.beaker.evaluator.ResourceLoaderTest;\n" +
            "import com.twosigma.beaker.jvm.object.OutputContainer;\n" +
            "import com.twosigma.beaker.chart.xychart.SimpleTimePlot;\n" +
            "List<Map<?, ?>> rates = ResourceLoaderTest.readAsList(\"tableRowsTest.csv\");\n" +
            "plot2 = new SimpleTimePlot(rates, [\"m3\", \"y1\"], showLegend:false, initWidth: 300, initHeight: 400)\n" +
            "new OutputContainer() << plot2";

    //when
    SimpleEvaluationObject seo = groovyEvaluator.executeCode(code, HEADER_MESSAGE, 1);
    waitForResult(seo);
    //then
    assertTrue(seo.getPayload().toString(),seo.getStatus().equals(FINISHED));
    verifyPlot(groovyKernel.getPublishedMessages());
  }

  private void verifyPlot(List<Message> messages) {
    Message tableDisplay = messages.get(0);
    verifyInternalOpenCommMsg(tableDisplay, Plot.MODEL_NAME_VALUE, Plot.VIEW_NAME_VALUE);
    Message model = messages.get(1);
    assertThat(getValueForProperty(model, "model", String.class)).isNotEmpty();
    verifyDisplayMsg(messages.get(2));
  }


  @Test
  public void shouldDisplayOutputContainerWithTabLayoutTest() throws Exception {
    //given
    String code =
            "import com.twosigma.beaker.evaluator.ResourceLoaderTest;\n" +
            "import com.twosigma.beaker.jvm.object.OutputContainer;\n" +
            "import com.twosigma.beaker.jvm.object.TabbedOutputContainerLayoutManager;\n" +
            "import com.twosigma.beaker.chart.xychart.SimpleTimePlot;\n" +
            "List<Map<?, ?>> rates = ResourceLoaderTest.readAsList(\"tableRowsTest.csv\");\n" +
            "plot2 = new SimpleTimePlot(rates, [\"m3\", \"y1\"], showLegend:false, initWidth: 300, initHeight: 400)\n" +
            "def l = new TabbedOutputContainerLayoutManager()\n"+
            "def oc = new OutputContainer()\n" +
            "oc.setLayoutManager(l)\n" +
            "oc.addItem(plot2, \"Scatter with History\")\n"+
            "oc";

    //when
    SimpleEvaluationObject seo = groovyEvaluator.executeCode(code, HEADER_MESSAGE, 1);
    waitForResult(seo);
    //then
    assertTrue(seo.getPayload().toString(),seo.getStatus().equals(FINISHED));
    verifyTabLayout(groovyKernel.getPublishedMessages());
  }

  private void verifyTabLayout(List<Message> publishedMessages) {
    Message tab = publishedMessages.get(3);
    Map data = getData(tab);
    assertThat(data.get(VIEW_NAME)).isEqualTo(Tab.VIEW_NAME_VALUE);
  }

}