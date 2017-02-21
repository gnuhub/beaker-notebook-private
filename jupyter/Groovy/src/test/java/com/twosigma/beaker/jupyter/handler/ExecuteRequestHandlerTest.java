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

import com.twosigma.beaker.widgets.GroovyKernelTest;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Before;
import org.junit.Test;
import org.lappsgrid.jupyter.groovy.msg.Header;
import org.lappsgrid.jupyter.groovy.msg.Message;

import static com.twosigma.beaker.jupyter.msg.JupyterMessages.EXECUTE_INPUT;
import static org.assertj.core.api.Assertions.assertThat;


public class ExecuteRequestHandlerTest {

  private ExecuteRequestHandler sut;
  private GroovyKernelTest groovyKernel;

  @Before
  public void setUp() throws Exception {
    groovyKernel = new GroovyKernelTest("KernelId1");
    sut = new ExecuteRequestHandler(groovyKernel);
  }

  @Test
  public void shouldHandleMessage() throws Exception {
    //given
    Message message = new Message();
    message.setHeader(new Header(EXECUTE_INPUT,"sessionId1"));
    HashedMap content = new HashedMap();
    content.put("code","1/1");
    message.setContent(content);
    //when
    sut.handle(message);

    //then
    assertThat(groovyKernel.getMessages()).isNotEmpty();
  }
}