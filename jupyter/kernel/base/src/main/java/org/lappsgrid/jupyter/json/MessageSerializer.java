package org.lappsgrid.jupyter.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class MessageSerializer {

  private static ObjectMapper mapper;

  static {
    mapper = new ObjectMapper();
    mapper.disable(SerializationFeature.INDENT_OUTPUT);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  private MessageSerializer() {
  }

  public static <T> T parse(String json, Class<T> theClass) {
    T result = null;
    try {
      result = mapper.readValue(json, theClass);
    } catch (Exception e) {
      // Ignored. We return null to indicate an error.
    }

    return result;
  }

  public static String toJson(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (Exception e) {
      return null;
    }

  }
}