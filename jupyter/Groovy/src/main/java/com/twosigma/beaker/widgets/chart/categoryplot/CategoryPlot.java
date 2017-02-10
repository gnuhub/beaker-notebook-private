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
package com.twosigma.beaker.widgets.chart.categoryplot;

import com.twosigma.beaker.chart.categoryplot.plotitem.CategoryGraphics;
import com.twosigma.beaker.jupyter.Comm;
import com.twosigma.beaker.widgets.CommFunctionality;
import com.twosigma.beaker.widgets.internal.InternalWidget;
import com.twosigma.beaker.widgets.internal.InternalWidgetContent;
import com.twosigma.beaker.widgets.internal.InternalWidgetUtils;
import com.twosigma.beaker.widgets.internal.SerializeToString;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static com.twosigma.beaker.chart.serializer.CategoryPlotSerializer.GRAPHICS_LIST;

public class CategoryPlot extends com.twosigma.beaker.chart.categoryplot.CategoryPlot implements CommFunctionality, InternalWidget {

  public static final String VIEW_NAME_VALUE = "CategoryPlotView";
  public static final String MODEL_NAME_VALUE = "CategoryPlotModel";

  private Comm comm;

  public CategoryPlot() throws NoSuchAlgorithmException {
    this.comm = InternalWidgetUtils.createComm(this, new InternalWidgetContent() {
      @Override
      public void addContent(HashMap<String, Serializable> content) {
        content.put(InternalWidgetUtils.MODEL_NAME, MODEL_NAME_VALUE);
        content.put(InternalWidgetUtils.VIEW_NAME, VIEW_NAME_VALUE);
      }
    });
  }

  @Override
  public com.twosigma.beaker.chart.categoryplot.CategoryPlot leftShift(CategoryGraphics graphics) {
    com.twosigma.beaker.chart.categoryplot.CategoryPlot categoryPlot = super.leftShift(graphics);
    this.comm.sendUpdate(GRAPHICS_LIST, SerializeToString.toJson(categoryPlot));
    return categoryPlot;
  }

  @Override
  public Comm getComm() {
    return this.comm;
  }

}
