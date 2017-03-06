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
package com.twosigma.beaker.widgets.integers;

import com.twosigma.beaker.jupyter.GroovyKernelManager;
import org.lappsgrid.jupyter.groovy.GroovyKernelTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static com.twosigma.beaker.widgets.TestWidgetUtils.verifyMsgForProperty;
import static com.twosigma.beaker.widgets.TestWidgetUtils.verifyOpenCommMsg;

public class IntProgressTest {

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
    new IntProgress();
    //then
    verifyOpenCommMsg(groovyKernel.getPublishedMessages(), IntProgress.MODEL_NAME_VALUE, IntProgress.VIEW_NAME_VALUE);
  }

  @Test
  public void shouldSendCommMsgWhenOrientationChange() throws Exception {
    //given
    IntProgress intProgress = intProgress();
    //when
    intProgress.setOrientation("vertical");
    //then
    verifyMsgForProperty(groovyKernel, IntSlider.ORIENTATION, "vertical");
  }

  private IntProgress intProgress() throws NoSuchAlgorithmException {
    IntProgress progress = new IntProgress();
    groovyKernel.clearPublishedMessages();
    return progress;
  }

}