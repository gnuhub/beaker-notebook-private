package com.twosigma.beaker.groovy.widgets;

import static com.twosigma.beaker.widgets.DisplayWidget.display;

import org.codehaus.groovy.runtime.MethodClosure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twosigma.beaker.jupyter.KernelManager;
import com.twosigma.beaker.jupyter.msg.MessageCreator;
import com.twosigma.beaker.widgets.InteractiveBase;
import com.twosigma.beaker.widgets.ValueWidget;
import com.twosigma.jupyter.message.Message;

public class Interactive extends InteractiveBase{

  private static final Logger logger = LoggerFactory.getLogger(Interactive.class);
  
  public static void interact(MethodClosure function, Object... parameters) {
    final MessageCreator mc = new MessageCreator(KernelManager.get());

    for (ValueWidget<?> widget : widgetsFromAbbreviations(parameters)) {
      widget.getComm().addMsgCallbackList(widget.new ValueChangeMsgCallbackHandler() {
        
        @Override
        public void updateValue(Object value, Message message) {
          Object result = function.call(widget.getValue());
          KernelManager.get().publish(mc.buildClearOutput(message, true));
          KernelManager.get().publish(mc.buildDisplayData(message, result.toString()));
          
        }
      });
      logger.info("interact Widget: " + widget.getClass().getName());
      display(widget);
    }
  }

}