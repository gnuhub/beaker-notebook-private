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
package com.twosigma.jupyter.message;

import static com.twosigma.beaker.jupyter.Utils.timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.twosigma.beaker.jupyter.msg.JupyterMessages;

@JsonPropertyOrder({"identities", "header", "parentHeader", "metadata", "content"})
public class Message {

  private List<byte[]> identities = new ArrayList<>();
  private Header header;
  @JsonProperty("parent_header")
  private Header parentHeader;
  private Map<String, Serializable> metadata;
  private Map<String, Serializable> content;

  public Message() {
    header = new Header();
    header.setDate(timestamp());
  }

  public JupyterMessages type() {
    return (header != null && header.getTypeEnum() != null) ? header.getTypeEnum() : null;
  }

  public List<byte[]> getIdentities() {
    return identities;
  }

  public void setIdentities(List<byte[]> identities) {
    this.identities = identities;
  }

  public Header getHeader() {
    return header;
  }

  public void setHeader(Header header) {
    this.header = header;
  }

  public Header getParentHeader() {
    return parentHeader;
  }

  public void setParentHeader(Header parentHeader) {
    this.parentHeader = parentHeader;
  }

  public Map<String, Serializable> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Serializable> metadata) {
    this.metadata = metadata;
  }

  public Map<String, Serializable> getContent() {
    return content;
  }

  public void setContent(Map<String, Serializable> content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "Type = " + header.getType();
  }

}