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

package com.twosigma.beaker.groovy;

import com.twosigma.beaker.KernelTest;
import com.twosigma.beaker.evaluator.Evaluator;
import com.twosigma.beaker.evaluator.EvaluatorManager;
import com.twosigma.beaker.evaluator.GroovyEvaluator;
import com.twosigma.beaker.jupyter.threads.ExecutionResultSender;
import com.twosigma.beaker.jvm.object.SimpleEvaluationObject;
import com.twosigma.jupyter.Config;
import com.twosigma.jupyter.message.Message;

import java.util.ArrayList;
import java.util.List;

public class GroovyKernelTest extends KernelTest {

  private String id;
  private SimpleEvaluationObject simpleEvaluationObject;
  private Boolean groovyEvaluatorManagerExit;
  private Boolean commHandleMessage;
  private Boolean setShellOptions;
  private EvaluatorManager evaluatorManager;

  public GroovyKernelTest() {
    this("GroovyKernelTestId1");
  }

  public GroovyKernelTest(String id) {
    this.id = id;
  }

  public GroovyKernelTest(String id, Evaluator evaluator) {
    this.id = id;
    this.evaluatorManager = new EvaluatorManager(this, evaluator);
  }

  @Override
  public EvaluatorManager getEvaluatorManager() {
    return evaluatorManager;
  }
}
