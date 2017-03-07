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
package com.twosigma.beaker.groovy.evaluator;

import java.io.IOException;

import com.twosigma.beaker.groovy.autocomplete.AutocompleteResult;
import com.twosigma.beaker.jvm.object.SimpleEvaluationObject;
import org.lappsgrid.jupyter.groovy.KernelFunctionality;
import org.lappsgrid.jupyter.groovy.msg.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvaluatorManager {
  
  public static Logger logger = LoggerFactory.getLogger(EvaluatorManager.class);
  
  protected Evaluator evaluator = null;
  protected KernelFunctionality kernel;
  
  public EvaluatorManager(KernelFunctionality kernel, Evaluator evaluator) {
    this.kernel = kernel;
    this.evaluator = evaluator;
    evaluator.startWorker();
  }

  public synchronized void setShellOptions(String cp, String in, String od) {
    try {
      evaluator.setShellOptions(cp, in, od);
    } catch (IOException e) {
      logger.error("Error while setting Shell Options", e);
    }
    evaluator.startWorker();
  }

  public AutocompleteResult autocomplete(String code, int caretPosition) {
    return evaluator.autocomplete(code,caretPosition);
  }

  public synchronized void killAllThreads() {
    evaluator.killAllThreads();
  }

  public synchronized void executeCode(String code, Message message, int executionCount) {
    SimpleEvaluationObject seo = new SimpleEvaluationObject(code);
    seo.setJupyterMessage(message);
    seo.setExecutionCount(executionCount);
    seo.addObserver(kernel.getExecutionResultSender());
    evaluator.evaluate(seo, code);
  }
  
  public void exit() {
    evaluator.exit();
  }
  
}