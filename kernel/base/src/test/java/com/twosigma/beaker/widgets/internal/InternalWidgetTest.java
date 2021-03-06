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
package com.twosigma.beaker.widgets.internal;

import com.twosigma.beaker.widgets.InternalWidgetsTestRunner;
import org.junit.Test;

import static com.twosigma.beaker.widgets.InternalWidgetsTestUtils.verifyOpenCommMsgInternalWidgets;
import static com.twosigma.beaker.widgets.TestWidgetUtils.getValueForProperty;
import static com.twosigma.beaker.widgets.internal.InternalWidget.MODEL;
import static org.assertj.core.api.Assertions.assertThat;

public class InternalWidgetTest {

  @Test
  public void shouldSendCommOpenWhenCreateForAllClassesWhichImplementInternalWidgetInterface() throws Exception {
    new InternalWidgetsTestRunner().test((clazz, groovyKernel) -> {
      //given
      //when
      InternalWidget widget = clazz.newInstance();
      //then
      verifyOpenCommMsgInternalWidgets(groovyKernel.getPublishedMessages(), widget.getModelNameValue(), widget.getViewNameValue());
    });
  }

  @Test
  public void shouldSendCommMsgForSendModelForAllClassesWhichImplementInternalWidgetInterface() throws Exception {
    new InternalWidgetsTestRunner().test((clazz, groovyKernel) -> {
      InternalWidget widget = clazz.newInstance();
      //when
      widget.sendModel();
      //then
      String valueForProperty = getValueForProperty(groovyKernel.getPublishedMessages().get(1), MODEL, String.class);
      assertThat(valueForProperty).isNotNull();
    });
  }

}