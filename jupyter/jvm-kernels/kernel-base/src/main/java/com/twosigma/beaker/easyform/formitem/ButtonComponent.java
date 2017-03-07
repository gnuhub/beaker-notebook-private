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

import java.util.LinkedList;
import java.util.List;

public class ButtonComponent extends EasyFormComponent {

  private String tag;
  private List<EasyFormListener> actionListeners = new LinkedList<>();
  public EasyFormListener actionPerformed = new EmptyListener();

  public void fireActionPerformed() {
    if (actionPerformed != null) {
      actionPerformed.execute(getLabel());
      for (EasyFormListener listener : actionListeners) {
        listener.execute(getLabel());
      }
    }
  }

  public EasyFormComponent addAction(final EasyFormListener listener) {
    addActionListener(listener);
    return this;
  }

  public void addActionListener(final EasyFormListener listener) {
    if (listener != null) {
      actionListeners.add(listener);
    }
  }

  public void setTag(final String tag) {
    this.tag = tag;
  }

  public String getTag() {
    return tag;
  }

  public boolean isButton() {
    return true;
  }
}
