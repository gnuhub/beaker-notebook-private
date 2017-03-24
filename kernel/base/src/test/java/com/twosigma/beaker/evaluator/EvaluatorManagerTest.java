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

package com.twosigma.beaker.evaluator;

import com.twosigma.beaker.KernelTest;
import com.twosigma.jupyter.message.Message;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EvaluatorManagerTest {

  private EvaluatorTest evaluator;
  private EvaluatorManager evaluatorManager;
  private static KernelTest kernel;

  @BeforeClass
  public static void setUpClass() throws Exception {
    kernel = new KernelTest();
  }

  @Before
  public void setUp() throws Exception {
    evaluator = new EvaluatorTest();
    evaluatorManager = new EvaluatorManager(kernel, evaluator);
  }

  @Test
  public void create_callEvaluatorToStartWorker() throws Exception {
    //when
    //then
    Assertions.assertThat(evaluator.isCallStartWorker()).isTrue();
  }

  @Test
  public void setShellOptions_callEvaluatorToStartWorker() throws Exception {
    evaluator.clearStartWorkerFlag();
    //when
    evaluatorManager.setShellOptions("cp", "in");
    //then
    Assertions.assertThat(evaluator.isCallStartWorker()).isTrue();
  }

  @Test
  public void killAllThreads_callEvaluatorToKillAllThreads() throws Exception {
    //when
    evaluatorManager.killAllThreads();
    //then
    Assertions.assertThat(evaluator.isCallKillAllThreads()).isTrue();
  }

  @Test
  public void exit_callEvaluatorToExit() throws Exception {
    //when
    evaluatorManager.exit();
    //then
    Assertions.assertThat(evaluator.isCallExit()).isTrue();
  }

  @Test
  public void executeCode_callEvaluatorToEvaluate(){
    String code = "test code";
    //when
    evaluatorManager.executeCode(code, new Message(), 5);
    //then
    Assertions.assertThat(evaluator.getCode()).isEqualTo(code);
  }

}
