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

package com.twosigma.beaker.chart.xychart.plotitem;

import com.twosigma.beaker.chart.Color;
import com.twosigma.beaker.chart.Graphics;

import java.util.Date;

public class Pimage extends Graphics {
  private Number x = 0;
  private Number y = 0;
  private Number width = 320;
  private Number height = 240;
  // test this format only
  private String format = "png";
  private byte[] value;
  private Color  baseColor;
  

  // getter and setters

  public Number getX() {
    return x;
  }

  public void setX(Object x) {
    if (x instanceof Number) {
      this.x = (Number)x;
    } else if (x instanceof Date) {
      this.x = ((Date)x).getTime();
    } else {
      throw new IllegalArgumentException("x takes Number or Date");
    }
  }

  public Number getY() {
    return y;
  }

  public void setY(Number y) {
    this.y = y;
  }

  public Number getWidth() {
    return width;
  }

  public void setWidth(Number width) {
    this.width = width;
  }

  public Number getHeight() {
    return height;
  }

  public void setHeight(Number height) {
    this.height = height;
  }

  public byte[] getValue() {
    return value;
  }

  public void setValue(byte[] value) {
    this.value = value;
    // do not know how to use this
    //sendUpdate("_b64value", value);
  }

  public void setColor(Object color) {
    if (color instanceof Color) {
      this.baseColor = (Color) color;
    } else if (color instanceof java.awt.Color) {
      this.baseColor = new Color((java.awt.Color) color);
    } else {
      throw new IllegalArgumentException(
        "setColor takes Color or java.awt.Color");
    }
  }

  @Override
  public void setColori(Color color) {
    this.baseColor = color;
  }

  @Override
  public Color getColor() {
    return baseColor;
  }

}