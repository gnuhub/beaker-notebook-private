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
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lappsgrid.jupyter.msg.Message;

public class CommCloseHandlerTest {

  private GroovyKernelTest groovyKernel;
  private CommCloseHandler commCloseHandler;
  private Message message;

  @Before
  public void setUp() {
    groovyKernel = new GroovyKernelTest();
    commCloseHandler = new CommCloseHandler(groovyKernel);
    message = JupyterHandlerTest.initCloseMessage();
    JupyterHandlerTest.initKernelCommMapWithOneComm(groovyKernel);
  }

  @After
  public void tearDown() throws Exception {
    groovyKernel.clearSentMessages();
  }

  @Test
  public void handleMessage_shouldSendShellSocketMessage() throws Exception {
    //when
    commCloseHandler.handle(message);
    //then
    Assertions.assertThat(groovyKernel.getSentMessages()).isNotEmpty();
  }

  @Test
  public void handleMessage_sentMessageHasTypeIsCommClose() throws Exception {
    //when
    commCloseHandler.handle(message);
    //then
    Assertions.assertThat(groovyKernel.getSentMessages()).isNotEmpty();
    Message sendMessage = groovyKernel.getSentMessages().get(0);
    Assertions.assertThat(sendMessage.getHeader().getType())
        .isEqualTo(JupyterMessages.COMM_CLOSE.getName());
  }

  @Test
  public void handleMessage_sentMessageHasSessionId() throws Exception {
    //given
    String expectedSessionId = message.getHeader().getSession();
    //when
    commCloseHandler.handle(message);
    //then
    Assertions.assertThat(groovyKernel.getSentMessages()).isNotEmpty();
    Message sendMessage = groovyKernel.getSentMessages().get(0);
    Assertions.assertThat(sendMessage.getHeader().getSession()).isEqualTo(expectedSessionId);
  }

  @Test
  public void handleMessage_sentMessageHasParentHeader() throws Exception {
    //given
    String expectedHeader = message.getHeader().asJson();
    //when
    commCloseHandler.handle(message);
    //then
    Assertions.assertThat(groovyKernel.getSentMessages()).isNotEmpty();
    Message sendMessage = groovyKernel.getSentMessages().get(0);
    Assertions.assertThat(sendMessage.getParentHeader().asJson()).isEqualTo(expectedHeader);
  }

  @Test
  public void handleMessage_sentMessageHasIdentities() throws Exception {
    //given
    String expectedIdentities = new String(message.getIdentities().get(0));
    //when
    commCloseHandler.handle(message);
    //then
    Assertions.assertThat(groovyKernel.getSentMessages()).isNotEmpty();
    Message sendMessage = groovyKernel.getSentMessages().get(0);
    Assertions.assertThat(new String(sendMessage.getIdentities().get(0)))
        .isEqualTo(expectedIdentities);
  }
}
