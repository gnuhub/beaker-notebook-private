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
package com.twosigma.beaker.groovy.evaluator;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.twosigma.beaker.groovy.GroovyDefaultVariables.CLASS_PATH;
import static com.twosigma.beaker.groovy.GroovyDefaultVariables.IMPORTS;
import static com.twosigma.beaker.groovy.GroovyDefaultVariables.OUT_DIR;
import static org.assertj.core.api.Assertions.assertThat;

public class GroovyEvaluatorAutocompleteTest {

  private GroovyEvaluator groovyEvaluator;

  @Before
  public void setUp() throws Exception {
    groovyEvaluator = new GroovyEvaluator("shellId1", "sessionId1");
    groovyEvaluator.setShellOptions(CLASS_PATH, IMPORTS, OUT_DIR);
    groovyEvaluator.newEvaluator();
  }

  @Test
  public void shouldReturnAutocompleteForPrintln1() throws Exception {
    //given

    //when
    List<String> autocomplete = groovyEvaluator.autocomplete(
            //"\n" +
                    "System.out.printl",18);
    //then
    assertThat(autocomplete).isNotEmpty();
  }


  @Test
  public void shouldReturnAutocompleteForPrintln() throws Exception {
    //given

    //when
    List<String> autocomplete = groovyEvaluator.autocomplete(
            "\n" +
            "System.out.printl",18);
    //then
    assertThat(autocomplete).isNotEmpty();
  }
}