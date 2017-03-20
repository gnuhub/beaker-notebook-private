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
package com.twosigma.beaker.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.twosigma.beaker.widgets.bools.Checkbox;
import com.twosigma.beaker.widgets.floats.FloatSlider;
import com.twosigma.beaker.widgets.integers.IntSlider;
import com.twosigma.beaker.widgets.selections.Dropdown;
import com.twosigma.beaker.widgets.strings.Text;

public class Interactive {

  
  public static void interact(){
    
  }
  
  /**
   * Make widgets from single values, which can be used as parameter defaults.
   * @param o
   * @return
   */
  protected static DOMWidget widgetFromSingleValue(Object o){
    DOMWidget ret = null;
    if (o instanceof String){
      Text witget = new Text();
      witget.setValue((String)o);
      ret = witget;
    }else if (o instanceof Boolean){
      Checkbox witget = new Checkbox();
      witget.setValue((Boolean)o);
      ret = witget;
    }else if (o instanceof Integer || o instanceof Short || o instanceof Byte){
      Integer value = (Integer)o;
      Integer[] result = getMinMaxValue(null, null, value);
      IntSlider witget = new IntSlider();
      witget.setMin(result[0]);
      witget.setMax(result[1]);
      witget.setValue((Integer)o);
      ret = witget;
    }else if (o instanceof Double || o instanceof Float){
      Double value = (Double)o;
      Double[] result = getMinMaxValue(null, null, value);
      FloatSlider witget = new FloatSlider();
      witget.setMin(result[0]);
      witget.setMax(result[1]);
      witget.setValue((Double)o);
      ret = witget;
    }
    return ret;
  }
  
  /**
   * Make widgets from an iterable. This should not be done for a string or tuple.
   * @param o
   * @return
   */
  protected static Dropdown widgetFromIterable(Object o){
    Dropdown ret = null;
    if (o instanceof Collection<?>){
      Collection<Object> value = (Collection<Object>)o;
      ret = new Dropdown();
      ret.setOptions(value);
    }if (o instanceof Object[]){
      Object[] value = (Object[])o;
      ret = new Dropdown();
      ret.setOptions(value);
    }else if (o instanceof Map<?,?>){
      Map<Object,Object> value = (Map<Object,Object>)o;
      ret = new Dropdown();
      ret.setOptions(value.values());
    }else{
      List<Object> value = new ArrayList<>(1);
      value.add(o);
      ret = new Dropdown();
      ret.setOptions(value);
    }
    return ret;
  }
  
  
  /**
   * Return min, max, value given input values with possible None.
   * 
   * @param min
   * @param max
   * @param value
   * @return min max value
   */
  //TODO
  protected static Integer[] getMinMaxValue(Integer min, Integer max, Integer value){
    Integer[] ret = new Integer[3];
    ret[0] = 0;
    ret[1] = 100;
    ret[2] = value != null ? value :50;
    return ret;
  }
  
  /**
   * Return min, max, value given input values with possible None.
   * 
   * @param min
   * @param max
   * @param value
   * @return min max value
   */
  //TODO
  protected static Double[] getMinMaxValue(Double min, Double max, Double value){
    Double[] ret = new Double[3];
    ret[0] = 0d;
    ret[1] = 100d;
    ret[2] = value != null ? value :50d;
    return ret;
  }
  
}