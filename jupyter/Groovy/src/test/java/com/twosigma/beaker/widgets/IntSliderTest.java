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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lappsgrid.jupyter.groovy.msg.Message;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
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
  public void shouldSendCommMsgWhenValueChange() throws Exception {
    //given
    IntSlider intSlider = intSlider();
    //when
    intSlider.setValue(11);
    //then
    verifyMsgForProperty(IntSlider.VALUE, 11);
  }

  @Test
  public void shouldSendCommMsgWhenDisableChange() throws Exception {
    //given
    IntSlider intSlider = intSlider();
    //when
    intSlider.setDisabled(true);
    //then
    verifyMsgForProperty(Widget.DISABLED, true);
  }

  @Test
  public void shouldSendCommMsgWhenVisibleChange() throws Exception {
    //given
    IntSlider intSlider = intSlider();
    //when
    intSlider.setVisible(false);
    //then
    verifyMsgForProperty(Widget.VISIBLE, false);
  }

  @Test
  public void shouldSendCommMsgWhenDescriptionChange() throws Exception {
    //given
    IntSlider intSlider = intSlider();
    //when
    intSlider.setDescription("Description 2");
    //then
    verifyMsgForProperty(Widget.DESCRIPTION, "Description 2");
  }

  @Test
  public void shouldSendCommMsgWhenMsg_throttleChange() throws Exception {
    //given
    IntSlider intSlider = intSlider();
    //when
    intSlider.setMsg_throttle(12);
    //then
    verifyMsgForProperty(Widget.MSG_THROTTLE, 12);
  }

  @Test
  public void shouldSendCommMsgWhenStepChange() throws Exception {
    //given
    IntSlider intSlider = intSlider();
    //when
    intSlider.setStep(12);
    //then
    verifyMsgForProperty(BoundedIntWidget.STEP, 12);
  }

  @Test
  public void shouldSendCommMsgWhenMaxChange() throws Exception {
    //given
    IntSlider intSlider = intSlider();
    //when
    intSlider.setMax(122);
    //then
    verifyMsgForProperty(BoundedIntWidget.MAX, 122);
  }

  @Test
  public void shouldSendCommMsgWhenMinChange() throws Exception {
    //given
    IntSlider intSlider = intSlider();
    //when
    intSlider.setMin(10);
    //then
    verifyMsgForProperty(BoundedIntWidget.MIN, 10);
  }

  @Test
  public void shouldSendCommMsgWhenOrientationChange() throws Exception {
    //given
    IntSlider intSlider = intSlider();
    //when
    intSlider.setOrientation("vertical");
    //then
    verifyMsgForProperty(IntSlider.ORIENTATION, "vertical");
  }

  @Test
  public void shouldSendCommMsgWhenSliderColorChange() throws Exception {
    //given
    IntSlider intSlider = intSlider();
    //when
    intSlider.setSlider_color("#456789");
    //then
    verifyMsgForProperty(IntSlider.SLIDER_COLOR, "#456789");
  }

  @Test
  public void shouldSendCommMsgWhenReadOutChange() throws Exception {
    //given
    IntSlider intSlider = intSlider();
    //when
    intSlider.setReadOut(false);
    //then
    verifyMsgForProperty(IntSlider.READOUT, false);
  }

  @Test
  public void shouldSendCommMsgWhenChangeContinuous_update() throws Exception {
    //given
    IntSlider intSlider = intSlider();
    //when
    intSlider.setContinuous_update(false);
    //then
    verifyMsgForProperty(IntSlider.CONTINUOUS_UPDATE, false);
  }


  private void verifyMsgForProperty(String propertyName, Object expected) {
    assertThat(groovyKernel.getMessages().size()).isEqualTo(1);
    Map data = getData(groovyKernel.getMessages().get(0));
    assertThat(data.get(Widget.METHOD)).isEqualTo(Widget.UPDATE);
    assertThat(((Map) data.get(Widget.STATE)).get(propertyName)).isEqualTo(expected);
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

  private IntSlider intSlider() throws NoSuchAlgorithmException {
    IntSlider intSlider = new IntSlider();
    groovyKernel.clearMessages();
    return intSlider;
  }

  private Map getData(Message message) {
    Map<String, Serializable> content = message.getContent();
    return (Map) content.get(Comm.DATA);
  }
}