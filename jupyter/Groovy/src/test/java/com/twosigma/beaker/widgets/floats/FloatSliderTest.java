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
package com.twosigma.beaker.widgets.floats;

import com.twosigma.beaker.jupyter.GroovyKernelManager;
import com.twosigma.beaker.widgets.GroovyKernelTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static com.twosigma.beaker.widgets.TestWidgetUtils.verifyMsgForProperty;
import static com.twosigma.beaker.widgets.TestWidgetUtils.verifyOpenCommMsg;

public class FloatSliderTest {

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
    new FloatSlider();
    //then
    verifyOpenCommMsg(groovyKernel.getMessages(), FloatSlider.MODEL_NAME_VALUE, FloatSlider.VIEW_NAME_VALUE);
  }

  @Test
  public void shouldSendCommMsgWhenValueChange() throws Exception {
    //given
    FloatSlider floatSlider = floatSlider();
    //when
    floatSlider.setValue(11.1);
    //then
    verifyMsgForProperty(groovyKernel, FloatSlider.VALUE, 11.1);
  }

  @Test
  public void shouldSendCommMsgWhenStepChange() throws Exception {
    //given
    FloatSlider floatSlider = floatSlider();
    //when
    floatSlider.setStep(12.1);
    //then
    verifyMsgForProperty(groovyKernel, BoundedFloatWidget.STEP, 12.1);
  }

  @Test
  public void shouldSendCommMsgWhenMaxChange() throws Exception {
    //given
    FloatSlider floatSlider = floatSlider();
    //when
    floatSlider.setMax(122.3);
    //then
    verifyMsgForProperty(groovyKernel, BoundedFloatWidget.MAX, 122.3);
  }

  @Test
  public void shouldSendCommMsgWhenMinChange() throws Exception {
    //given
    FloatSlider floatSlider = floatSlider();
    //when
    floatSlider.setMin(10.2);
    //then
    verifyMsgForProperty(groovyKernel, BoundedFloatWidget.MIN, 10.2);
  }

  @Test
  public void shouldSendCommMsgWhenOrientationChange() throws Exception {
    //given
    FloatSlider floatSlider = floatSlider();
    //when
    floatSlider.setOrientation("vertical");
    //then
    verifyMsgForProperty(groovyKernel, FloatSlider.ORIENTATION, "vertical");
  }

  @Test
  public void shouldSendCommMsgWhenSliderColorChange() throws Exception {
    //given
    FloatSlider floatSlider = floatSlider();
    //when
    floatSlider.setSlider_color("#456789");
    //then
    verifyMsgForProperty(groovyKernel, FloatSlider.SLIDER_COLOR, "#456789");
  }

  @Test
  public void shouldSendCommMsgWhenReadOutChange() throws Exception {
    //given
    FloatSlider floatSlider = floatSlider();
    //when
    floatSlider.setReadOut(false);
    //then
    verifyMsgForProperty(groovyKernel, FloatSlider.READOUT, false);
  }

  @Test
  public void shouldSendCommMsgWhenChangeContinuous_update() throws Exception {
    //given
    FloatSlider floatSlider = floatSlider();
    //when
    floatSlider.setContinuous_update(false);
    //then
    verifyMsgForProperty(groovyKernel, FloatSlider.CONTINUOUS_UPDATE, false);
  }

  private FloatSlider floatSlider() throws NoSuchAlgorithmException {
    FloatSlider widget = new FloatSlider();
    groovyKernel.clearMessages();
    return widget;
  }

}