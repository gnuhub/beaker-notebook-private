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

package com.twosigma.beaker.jupyter;

import com.twosigma.beaker.evaluator.Evaluator;
import com.twosigma.beaker.jvm.object.SimpleEvaluationObject;
import org.lappsgrid.jupyter.Kernel;
import org.lappsgrid.jupyter.KernelFunctionality;
import org.lappsgrid.jupyter.json.Serializer;
import org.lappsgrid.jupyter.msg.Header;
import org.lappsgrid.jupyter.msg.Message;
import org.zeromq.ZMQ;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Observer;
import java.util.Set;

public class GroovyKernelJupyterTest implements KernelFunctionality {

  private List<Message> publishMessages = new ArrayList<>();
  private List<Message> sendMessages = new ArrayList<>();
  private SimpleEvaluationObject simpleEvaluationObject;
  private Boolean groovyEvaluatorManagerExit;
  private Boolean commHandleMessage;
  private Boolean setShellOptions;

  @Override
  public void publish(Message message) throws NoSuchAlgorithmException {
    this.publishMessages.add(copyMessage(message));
  }

  @Override
  public void addComm(String commId, Comm comm) {

  }

  @Override
  public void removeComm(String commId) {

  }

  @Override
  public void send(Message message) throws NoSuchAlgorithmException {
    this.sendMessages.add(copyMessage(message));
  }

  @Override
  public void send(ZMQ.Socket socket, Message message) throws NoSuchAlgorithmException {
    this.sendMessages.add(copyMessage(message));
  }

  @Override
  public String getId() {
    return null;
  }

  @Override
  public Observer getExecutionResultSender() {
    return null;
  }

  @Override
  public Comm getComm(String string) {
    return null;
  }

  @Override
  public Evaluator getEvaluator(Kernel kernel) {
    return null;
  }

  @Override
  public boolean isCommPresent(String string) {
    return false;
  }

  @Override
  public Set<String> getCommHashSet() {
    return null;
  }

  public List<Message> getPublishMessages() {
    return publishMessages;
  }

  public List<Message> getSendMessages() {
    return sendMessages;
  }

  @Override
  public synchronized void setShellOptions(String cp, String in, String od) {
    setShellOptions = Boolean.TRUE;
  }

  @Override
  public void cancelExecution() {

  }

  public Boolean isSetShellOptions() {
    return setShellOptions;
  }

  public SimpleEvaluationObject getSimpleEvaluationObject() {
    return simpleEvaluationObject;
  }

  public void groovyEvaluatorManagerExecuteCode(String code, Message message, int executionCount) {
    simpleEvaluationObject = new SimpleEvaluationObject(code);
    simpleEvaluationObject.setJupyterMessage(message);
    simpleEvaluationObject.setExecutionCount(executionCount);
  }

  public void groovyEvaluatorManagerExit() {
    groovyEvaluatorManagerExit = Boolean.TRUE;
  }

  public Boolean getGroovyEvaluatorManagerExit() {
    return groovyEvaluatorManagerExit;
  }

  public Boolean getCommHandleMessage() {
    return commHandleMessage;
  }

  public void commHandleMessage() {
    this.commHandleMessage = Boolean.TRUE;
  }

  private Message copyMessage(Message origin) {
    Message copy = new Message();
    for (byte[] list : origin.getIdentities()) {
      copy.getIdentities().add(list.clone());
    }
    String header = Serializer.toJson(origin.getHeader());
    String parent = Serializer.toJson(origin.getParentHeader());
    String metadata = Serializer.toJson(origin.getMetadata());
    String content = Serializer.toJson(origin.getContent());
    copy.setHeader(Serializer.parse(header, Header.class));
    copy.setParentHeader(Serializer.parse(parent, Header.class));
    copy.setMetadata(Serializer.parse(metadata, LinkedHashMap.class));
    copy.setContent(Serializer.parse(content, LinkedHashMap.class));
    return copy;
  }
}
