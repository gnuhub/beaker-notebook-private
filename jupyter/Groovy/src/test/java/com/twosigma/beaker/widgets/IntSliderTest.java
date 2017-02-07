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
package com.twosigma.beaker.widgets;

import com.twosigma.beaker.jupyter.Comm;
import com.twosigma.beaker.jupyter.GroovyKernelManager;
import com.twosigma.beaker.jupyter.msg.JupyterMessages;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lappsgrid.jupyter.groovy.msg.Message;

import java.io.Serializable;
import java.util.Map;

import static com.twosigma.beaker.jupyter.msg.JupyterMessages.COMM_OPEN;
import static org.assertj.core.api.Assertions.assertThat;

public class IntSliderTest {

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
    new IntSlider();
    //then
    assertThat(groovyKernel.getMessages().size()).isEqualTo(2);
    Message layoutMessage = groovyKernel.getMessages().get(0);
    Message intSliderMessage = groovyKernel.getMessages().get(1);
    verifyIntSliderOpenCommMsg(intSliderMessage, layoutMessage);
  }

  @Test
  public void shouldSendCommMsgWhenChangeValue() throws Exception {
    //given
    IntSlider intSlider = new IntSlider();
    groovyKernel.clearMessages();
    //when
    intSlider.setValue(11);
    //then
    assertThat(groovyKernel.getMessages().size()).isEqualTo(1);
    Map data = getData(groovyKernel.getMessages().get(0));
    assertThat(data.get(Widget.METHOD)).isEqualTo(Widget.UPDATE);
    assertThat(((Map) data.get(Widget.STATE)).get(IntSlider.VALUE)).isEqualTo(11);
  }

  @Test
  public void shouldSendCommMsgWhenChangeDisable() throws Exception {
    //given
    IntSlider intSlider = new IntSlider();
    groovyKernel.clearMessages();
    //when
    intSlider.setDisabled(true);
    //then
    assertThat(groovyKernel.getMessages().size()).isEqualTo(1);
    Map data = getData(groovyKernel.getMessages().get(0));
    assertThat(data.get(Widget.METHOD)).isEqualTo(Widget.UPDATE);
    assertThat(((Map) data.get(Widget.STATE)).get(Widget.DISABLED)).isEqualTo(true);
  }

  @Test
  public void shouldSendCommMsgWhenChangeVisible() throws Exception {
    //given
    IntSlider intSlider = new IntSlider();
    groovyKernel.clearMessages();
    //when
    intSlider.setVisible(false);
    //then
    assertThat(groovyKernel.getMessages().size()).isEqualTo(1);
    Map data = getData(groovyKernel.getMessages().get(0));
    assertThat(data.get(Widget.METHOD)).isEqualTo(Widget.UPDATE);
    assertThat(((Map) data.get(Widget.STATE)).get(Widget.VISIBLE)).isEqualTo(false);
  }


  private void verifyIntSliderOpenCommMsg(Message message, Message layoutMessage) {
    assertThat(message.getHeader().getType()).isEqualTo(COMM_OPEN.getName());
    Map data = getData(message);
    assertThat(data.get(Layout.LAYOUT)).isEqualTo(Layout.IPY_MODEL + layoutMessage.getContent().get(Comm.COMM_ID));
    assertThat(data.get(Widget.MODEL_MODULE)).isEqualTo(Widget.MODEL_MODULE_VALUE);
    assertThat(data.get(Widget.VIEW_MODULE)).isEqualTo(Widget.VIEW_MODULE_VALUE);
    assertThat(data.get(Widget.MODEL_NAME)).isEqualTo(IntSlider.MODEL_NAME_VALUE);
    assertThat(data.get(Widget.VIEW_NAME)).isEqualTo(IntSlider.VIEW_NAME_VALUE);
    assertThat(data.get(IntSlider.VALUE)).isEqualTo(0);
  }

  private Map getData(Message message) {
    Map<String, Serializable> content = message.getContent();
    return (Map) content.get(Comm.DATA);
  }
}