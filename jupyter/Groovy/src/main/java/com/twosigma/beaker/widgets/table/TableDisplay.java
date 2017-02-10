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
package com.twosigma.beaker.widgets.table;

import com.twosigma.beaker.jupyter.Comm;
import com.twosigma.beaker.jupyter.CommNamesEnum;
import com.twosigma.beaker.jupyter.Utils;
import com.twosigma.beaker.widgets.CommFunctionality;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TableDisplay extends com.twosigma.beaker.table.TableDisplay implements CommFunctionality {

  public static final String MODEL_MODULE = "_model_module";
  public static final String MODEL_NAME = "_model_name";
  public static final String VIEW_MODULE = "_view_module";
  public static final String VIEW_NAME = "_view_name";

  public static final String MODEL_MODULE_VALUE = "jupyter-js-widgets";
  public static final String VIEW_MODULE_VALUE = "jupyter-js-widgets";

  public static final String VIEW_NAME_VALUE = "TableDisplayView";
  public static final String MODEL_NAME_VALUE = "TableDisplayModel";

  private Comm comm;

  public TableDisplay(Collection<Map<?, ?>> v) throws NoSuchAlgorithmException {
    super(v);
    init();
  }

  private void init() throws NoSuchAlgorithmException {
    comm = new Comm(Utils.uuid(), CommNamesEnum.JUPYTER_WIDGET);
    openComm(comm);
  }

  private void openComm(final Comm comm) throws NoSuchAlgorithmException {
    comm.setData(createContent());
    comm.open();
  }

  private HashMap<String, Serializable> createContent() {
    HashMap<String, Serializable> result = new HashMap<>();
    result.put(MODEL_MODULE, MODEL_MODULE_VALUE);
    result.put(VIEW_MODULE, VIEW_MODULE_VALUE);
    result.put(MODEL_NAME, MODEL_NAME_VALUE);
    result.put(VIEW_NAME, VIEW_NAME_VALUE);

    result.put("json", SerializeToString.toJson(this));
    return result;
  }

  @Override
  public Comm getComm() {
    return this.comm;
  }

}
