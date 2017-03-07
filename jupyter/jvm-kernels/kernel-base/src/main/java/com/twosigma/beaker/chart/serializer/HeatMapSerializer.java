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

import com.twosigma.beaker.chart.heatmap.HeatMap;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public class HeatMapSerializer extends AbstractChartSerializer<HeatMap> {

  public static final String GRAPHICS_LIST = "graphics_list";
  public static final String COLOR = "color";

  @Override
  public void serialize(HeatMap heatmap, JsonGenerator jgen, SerializerProvider sp)
    throws IOException, JsonProcessingException {

    jgen.writeStartObject();

    serialize(heatmap, jgen);

    jgen.writeObjectField(GRAPHICS_LIST, heatmap.getData());
    jgen.writeObjectField(COLOR, heatmap.getColor());

    jgen.writeEndObject();
  }

}