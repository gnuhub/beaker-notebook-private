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
package com.twosigma.beaker.jvm.object;

public class ConsoleOutput {

  private boolean error;
  private String text;
  private boolean printed;

  public ConsoleOutput(boolean error, String text){
    this.error = error;
    this.text = text;
  }
  
  public boolean isError() {
    return error;
  }

  public String getText() {
    return text;
  }

  public boolean isPrinted() {
    return printed;
  }

  public void setPrinted(boolean printed) {
    this.printed = printed;
  }

  @Override
  public String toString() {
    return "Error: " + error + " Text: " + text;
  }
  
}