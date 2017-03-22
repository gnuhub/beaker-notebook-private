/*
 *  Copyright 2014 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.beaker.chart.serializer;

import com.twosigma.beaker.chart.xychart.plotitem.Pimage;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

public class PimageSerializer extends JsonSerializer<Pimage> {
  
  @Override
  public void serialize(Pimage pimage, JsonGenerator jgen, SerializerProvider sp)
    throws IOException, JsonProcessingException {

    jgen.writeStartObject();

    jgen.writeObjectField("type", pimage.getClass().getSimpleName());
    // NanoPlot issue unresloved
    //jgen.writeObjectField("x", isNanoPlot ? processLargeNumber(pimage.getX()) : pimage.getX());
    jgen.writeObjectField("x", pimage.getX());
    jgen.writeObjectField("y", pimage.getY());
    jgen.writeObjectField("visible", pimage.getVisible());
    jgen.writeObjectField("yAxis", pimage.getYAxis());
    if (pimage.getWidth() != null) {
      jgen.writeObjectField("width", pimage.getWidth());
    }
    if (pimage.getHeight() != null) {
      jgen.writeObjectField("height", pimage.getHeight());
    }
    if (pimage.getValue() != null) {
      StringBuilder sb = new StringBuilder();
      sb.append("data:image/png;base64,");
      sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(pimage.getValue(), false)));
      jgen.writeObjectField("value", sb.toString());
    }
    jgen.writeEndObject();
  }

  private String processLargeNumber(Number largeNumber){
    return largeNumber != null ? largeNumber.toString() : "";
  }
}