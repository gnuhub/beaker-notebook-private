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

package com.twosigma.beaker.jupyter.handler;

import com.twosigma.beaker.groovy.GroovyKernelTest;
import com.twosigma.beaker.jupyter.msg.JupyterMessages;
import com.twosigma.beaker.jupyter.msg.MessageCreator;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lappsgrid.jupyter.msg.Message;

public class CommMsgHandlerTest {

  private GroovyKernelTest groovyKernel;
  private CommMsgHandler commMsgHandler;
  private Message message;

  @Before
  public void setUp() {
    groovyKernel = new GroovyKernelTest();
    commMsgHandler = new CommMsgHandler(groovyKernel, new MessageCreator(groovyKernel));
    message = JupyterHandlerTest.initCommMessage();
    JupyterHandlerTest.initKernelCommMapWithOneComm(groovyKernel);
  }

  @After
  public void tearDown() throws Exception {
    groovyKernel.clearPublishedMessages();
  }

  @Test
  public void handleMessage_shouldPublishTwoMessages() throws Exception {
    //when
    commMsgHandler.handle(message);
    //then
    Assertions.assertThat(groovyKernel.getPublishedMessages()).isNotEmpty();
    Assertions.assertThat(groovyKernel.getPublishedMessages().size()).isEqualTo(2);
  }

  @Test
  public void handleMessage_firstPublishedMessageHasExecutionStateIsBusy() throws Exception {
    //when
    commMsgHandler.handle(message);
    //then
    Assertions.assertThat(groovyKernel.getPublishedMessages()).isNotEmpty();
    Message publishMessage = groovyKernel.getPublishedMessages().get(0);
    Assertions.assertThat(publishMessage.getContent().get("execution_state")).isEqualTo("busy");
  }

  @Test
  public void handleMessage_firstPublishedMessageHasSessionId() throws Exception {
    //given
    String expectedSessionId = message.getHeader().getSession();
    //when
    commMsgHandler.handle(message);
    //then
    Assertions.assertThat(groovyKernel.getPublishedMessages()).isNotEmpty();
    Message publishMessage = groovyKernel.getPublishedMessages().get(0);
    Assertions.assertThat(publishMessage.getHeader().getSession()).isEqualTo(expectedSessionId);
  }

  @Test
  public void handleMessage_firstPublishedMessageHasTypeIsStatus() throws Exception {
    //when
    commMsgHandler.handle(message);
    //then
    Assertions.assertThat(groovyKernel.getPublishedMessages()).isNotEmpty();
    Message publishMessage = groovyKernel.getPublishedMessages().get(0);
    Assertions.assertThat(publishMessage.getHeader().getType())
        .isEqualTo(JupyterMessages.STATUS.getName());
  }

  @Test
  public void handleMessage_firstPublishedMessageHasParentHeader() throws Exception {
    //given
    String expectedHeader = message.getHeader().asJson();
    //when
    commMsgHandler.handle(message);
    //then
    Assertions.assertThat(groovyKernel.getPublishedMessages()).isNotEmpty();
    Message publishMessage = groovyKernel.getPublishedMessages().get(0);
    Assertions.assertThat(publishMessage.getParentHeader().asJson()).isEqualTo(expectedHeader);
  }

  @Test
  public void handleMessage_firstPublishedMessageHasIdentities() throws Exception {
    //given
    String expectedIdentities = new String(message.getIdentities().get(0));
    //when
    commMsgHandler.handle(message);
    //then
    Assertions.assertThat(groovyKernel.getPublishedMessages()).isNotEmpty();
    Message publishMessage = groovyKernel.getPublishedMessages().get(0);
    Assertions.assertThat(new String(publishMessage.getIdentities().get(0)))
        .isEqualTo(expectedIdentities);
  }

  @Test
  public void handleMessage_secondPublishedMessageHasExecutionStateIsIdle() throws Exception {
    //when
    commMsgHandler.handle(message);
    //then
    Assertions.assertThat(groovyKernel.getPublishedMessages()).isNotEmpty();
    Message publishMessage = groovyKernel.getPublishedMessages().get(1);
    Assertions.assertThat(publishMessage.getContent().get("execution_state")).isEqualTo("idle");
  }

  @Test
  public void handleMessage_secondPublishedMessageHasSessionId() throws Exception {
    //given
    String expectedSessionId = message.getHeader().getSession();
    //when
    commMsgHandler.handle(message);
    //then
    Assertions.assertThat(groovyKernel.getPublishedMessages()).isNotEmpty();
    Message publishMessage = groovyKernel.getPublishedMessages().get(1);
    Assertions.assertThat(publishMessage.getHeader().getSession()).isEqualTo(expectedSessionId);
  }

  @Test
  public void handleMessage_secondPublishedMessageHasTypeIsStatus() throws Exception {
    //when
    commMsgHandler.handle(message);
    //then
    Assertions.assertThat(groovyKernel.getPublishedMessages()).isNotEmpty();
    Message publishMessage = groovyKernel.getPublishedMessages().get(1);
    Assertions.assertThat(publishMessage.getHeader().getType())
        .isEqualTo(JupyterMessages.STATUS.getName());
  }

  @Test
  public void handleMessage_secondPublishedMessageHasIdentities() throws Exception {
    //given
    String expectedIdentities = new String(message.getIdentities().get(0));
    //when
    commMsgHandler.handle(message);
    //then
    Assertions.assertThat(groovyKernel.getPublishedMessages()).isNotEmpty();
    Message publishMessage = groovyKernel.getPublishedMessages().get(1);
    Assertions.assertThat(new String(publishMessage.getIdentities().get(0)))
        .isEqualTo(expectedIdentities);
  }
}
