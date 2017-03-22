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
package com.twosigma.beaker.mimetype;

public class MimeTypeManager {

  private static final String TEXT_PLAIN = "text/plain";
  private static final String TEXT_HTML = "text/html";
  private static final String TEXT_LATEX = "text/latex";

  private String mime;
  private String code;

  public MimeTypeManager(String mime, String code) {
    this.mime = mime;
    this.code = code;
  }

  public String getMime() {
    return mime;
  }

  public String getCode() {
    return code;
  }

  public static MimeTypeManager HTML(Object code) {
    return addMimeType(TEXT_HTML, code);
  }

  public static MimeTypeManager Latex(Object code) {
    return addMimeType(TEXT_LATEX, code);
  }

  public static MimeTypeManager Text(Object code) {
     return addMimeType(TEXT_PLAIN, code);
  }

  public static MimeTypeManager addMimeType(String mime, Object code) {
    return new MimeTypeManager(mime,code.toString());
  }

}
