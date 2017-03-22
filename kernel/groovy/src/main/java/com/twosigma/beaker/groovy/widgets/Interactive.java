package com.twosigma.beaker.groovy.widgets;

import org.codehaus.groovy.runtime.MethodClosure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twosigma.beaker.widgets.DOMWidget;
import com.twosigma.beaker.widgets.InteractiveBase;
import static com.twosigma.beaker.widgets.DisplayWidget.display;

public class Interactive extends InteractiveBase{

  private static final Logger logger = LoggerFactory.getLogger(Interactive.class);
  
  public static void interact(MethodClosure function, Object... parameters) {
    //logger.info(function.getClass().getName() + " " + parameters[0].getClass().getName());
    DOMWidget widget = getWidget(parameters);
    display(widget);
    function.call(parameters);
  }

}