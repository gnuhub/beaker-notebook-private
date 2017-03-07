/*
 *  Copyright 2015 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.beaker.easyform.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.twosigma.beaker.easyform.formitem.ComboBox;

import java.io.IOException;

public class ComboBoxSerializer extends AbstractEasyFormComponentSerializer<ComboBox> {

  @Override
  protected void writeSubclassFields(final JsonGenerator jgen,
                                     final ComboBox component) throws IOException {
    if (component.getEditable() != null) {
      jgen.writeObjectField("editable", component.getEditable().toString());
    }
    if (component.getWidth() != null) {
      jgen.writeObjectField("width", component.getWidth().toString());
    }
    if (component.getValues() != null && component.getValues().size() > 0) {
      jgen.writeArrayFieldStart("values");
      for (String value : component.getValues()) {
        jgen.writeString(value);
      }
      jgen.writeEndArray();
    }
  }
}
