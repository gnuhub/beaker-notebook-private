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
package com.twosigma.beaker;

import com.twosigma.beaker.jupyter.Comm;
import com.twosigma.beaker.widgets.InternalWidgetsTestRunner;
import com.twosigma.beaker.widgets.TestWidgetUtils;
import com.twosigma.beaker.widgets.internal.InternalWidget;
import org.junit.Test;
import org.lappsgrid.jupyter.groovy.msg.Message;

import java.util.Map;

import static com.twosigma.beaker.jupyter.msg.JupyterMessages.COMM_OPEN;
import static com.twosigma.beaker.widgets.DisplayWidget.DISPLAY;
import static com.twosigma.beaker.widgets.TestWidgetUtils.getValueForProperty;
import static com.twosigma.beaker.widgets.internal.InternalWidget.MODEL;
import static org.assertj.core.api.Assertions.assertThat;

public class SerializeToStringInternalWidgetsTest {

  @Test
  public void shouldSend3MessagesForAllClassesWhichImplementInternalWidgetInterface() throws Exception {
    new InternalWidgetsTestRunner().test((clazz, groovyKernel) -> {
      //give
      InternalWidget internalWidget = clazz.newInstance();
      //when
      SerializeToString.doit(internalWidget);
      //then
      assertThat(groovyKernel.getPublishedMessages().size()).isEqualTo(3);
      verifyOpenMsg(groovyKernel.getPublishedMessages().get(0));
      verifyModelMsg(groovyKernel.getPublishedMessages().get(1));
      verifyDisplayMsg(groovyKernel.getPublishedMessages().get(2));
    });
  }

  private void verifyOpenMsg(Message message) {
    assertThat(message.getHeader().getType()).isEqualTo(COMM_OPEN.getName());
  }

  private void verifyModelMsg(Message message) {
    String model = getValueForProperty(message, MODEL, String.class);
    assertThat(model).isNotNull();
  }

  private void verifyDisplayMsg(Message message) {
    Map data = TestWidgetUtils.getData(message);
    assertThat(data.get(Comm.METHOD)).isEqualTo(DISPLAY);
  }

}