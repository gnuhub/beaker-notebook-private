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

package com.twosigma.beaker.easyform.formitem;

import com.twosigma.beaker.easyform.EasyFormComponent;

public class TextArea extends EasyFormComponent {
  public static final Integer AUTO_HEIGHT = -1;
  public static final Integer AUTO_WIDTH = -1;
  private Integer width;
  private Integer height;

  public void setWidth(final Integer width) {
    this.width = width;
  }

  public Integer getWidth() {
    return width;
  }

  public void setHeight(final Integer height) {
    this.height = height;
  }

  public Integer getHeight() {
    return height;
  }
}
