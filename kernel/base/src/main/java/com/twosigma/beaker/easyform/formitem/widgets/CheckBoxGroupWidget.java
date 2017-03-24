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
package com.twosigma.beaker.easyform.formitem.widgets;

import com.twosigma.beaker.easyform.formitem.CheckBoxGroup;
import com.twosigma.beaker.jupyter.Comm;
import com.twosigma.beaker.widgets.CommFunctionality;
import com.twosigma.beaker.widgets.Widget;
import com.twosigma.beaker.widgets.bools.BoolWidget;
import com.twosigma.beaker.widgets.bools.Checkbox;
import com.twosigma.beaker.widgets.box.Box;
import com.twosigma.beaker.widgets.box.HBox;
import com.twosigma.beaker.widgets.box.VBox;
import com.twosigma.beaker.widgets.strings.Label;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class CheckBoxGroupWidget extends CheckBoxGroup implements CommFunctionality {

  private List<Checkbox> checkboxes;
  private Label label;
  private HBox widget;
  private Box rightSide;

  public CheckBoxGroupWidget() {
    this.checkboxes = new ArrayList<>();
    this.label = new Label();
  }

  @Override
  public String getLabel() {
    return this.label.getValue();
  }

  @Override
  public void setLabel(String label) {
    this.label.setValue(label);
  }

  @Override
  public String getValue() {
    return String.join(",", getValues());
  }

  @Override
  public void setValue(String value) {
  }

  @Override
  public Collection<String> getValues() {
    return this.checkboxes.stream().filter(BoolWidget::getValue).map(Widget::getDescription).collect(Collectors.toList());
  }

  @Override
  public void setValues(Collection<String> values) {
    values.forEach(item -> {
      Checkbox checkbox = new Checkbox();
      checkbox.setDescription(item);
      checkboxes.add(checkbox);
    });
  }

  public void createWidget() {
    if (getHorizontal()) {
      this.rightSide = new HBox(checkboxes.stream().map(x -> (CommFunctionality) x).collect(Collectors.toList()));
    } else {
      this.rightSide = new VBox(checkboxes.stream().map(x -> (CommFunctionality) x).collect(Collectors.toList()));
    }
    this.widget = new HBox(asList(label, rightSide));
  }

  @Override
  public Comm getComm() {
    return this.widget.getComm();
  }
}
