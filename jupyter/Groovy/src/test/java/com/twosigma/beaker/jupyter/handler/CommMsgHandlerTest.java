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

import com.twosigma.beaker.jupyter.GroovyKernelJupyterTest;
import com.twosigma.beaker.jupyter.msg.MessageCreator;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.lappsgrid.jupyter.groovy.msg.Message;

public class CommMsgHandlerTest {

    private GroovyKernelJupyterTest groovyKernel;
    private CommMsgHandler commMsgHandler;
    private Message message;

    @Before
    public void setUp(){
        groovyKernel = new GroovyKernelJupyterTest();
        commMsgHandler = new CommMsgHandler(groovyKernel, new MessageCreator(groovyKernel));
        message = JupyterHandlerTest.initCommMessage();
    }

    @Test
    public void handleMessage_shouldSendTwoMessages() throws Exception {
        //given
        JupyterHandlerTest.initKernelCommMapWithOneComm(groovyKernel);
        //when
        commMsgHandler.handle(message);
        //then
        Assertions.assertThat(groovyKernel.getPublishMessages()).isNotEmpty();
        Assertions.assertThat(groovyKernel.getPublishMessages().size()).isEqualTo(2);
    }

}
