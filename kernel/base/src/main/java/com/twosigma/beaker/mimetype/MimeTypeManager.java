package com.twosigma.beaker.mimetype;

import java.util.Hashtable;
import java.util.Map;

public class MimeTypeManager {
  private static Map<String, String> mimeToData;

  public static Map<String, String> html(String code) {
    mimeToData = new Hashtable<>();
    mimeToData.put("text/html", code);
    return mimeToData;
  }

  public static Map<String, String> latex(String code) {
    mimeToData = new Hashtable<>();
    mimeToData.put("text/latex", code);
    return mimeToData;
  }

  public static Map<String, String> text(String code) {
    mimeToData = new Hashtable<>();
    mimeToData.put("text/plain", code);
    return mimeToData;
  }

}
