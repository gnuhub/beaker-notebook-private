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


import java.io.IOException;
import java.util.Base64;

public class VideoContainer extends MIMEContainer {

  public static MIMEContainer Video(Object data) {
    byte[] video;
    String mimeType;
    try {
      if (data instanceof String) {
        video = getBytes(data);
      } else {
        video = (byte[]) data;
      }
      mimeType = guessMimeType(video);
    } catch (IOException e) {
      return addMimeType(TEXT_PLAIN, exceptionToString(e));
    }
    String output = String.format("<video controls> <source src='data:%1$s;base64,%2$s' type='1$s'> Your browser does not support the video tag. </video>",
        mimeType, Base64.getEncoder().encodeToString(video));

    return addMimeType(TEXT_HTML, output);
  }
}
