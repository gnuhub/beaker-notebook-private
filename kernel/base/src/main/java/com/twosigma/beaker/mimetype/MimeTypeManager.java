package com.twosigma.beaker.mimetype;

import java.util.HashMap;
import java.util.Map;

public class MimeTypeManager {
  public static final String TEXT_PLAIN = "text/plain";
  public static final String TEXT_HTML = "text/html";
  public static final String TEXT_LATEX = "text/latex";

  public static Map<String, String> html(String code) {
    return assignMimeToCode(TEXT_HTML, code);
  }

  public static Map<String, String> latex(String code) {
    return assignMimeToCode(TEXT_LATEX, code);
  }

  public static Map<String, String> text(String code) {
    return assignMimeToCode(TEXT_PLAIN, code);
  }

  private static Map<String, String> assignMimeToCode(String mime, String code) {
    Map<String, String> mimeToData = new HashMap<>();
    mimeToData.put(mime, code);
    return mimeToData;
  }

}
