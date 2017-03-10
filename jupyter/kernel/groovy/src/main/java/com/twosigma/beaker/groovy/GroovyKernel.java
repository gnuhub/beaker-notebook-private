package com.twosigma.beaker.groovy;

import com.twosigma.beaker.evaluator.Evaluator;
import com.twosigma.beaker.evaluator.GroovyEvaluator;
import com.twosigma.beaker.groovy.comm.GroovyCommOpenHandler;
import com.twosigma.beaker.jupyter.handler.CommOpenHandler;
import org.lappsgrid.jupyter.Kernel;
import org.lappsgrid.jupyter.msg.Message;
import org.zeromq.ZMQ;

import java.io.File;
import java.io.IOException;

import static com.twosigma.beaker.jupyter.Utils.uuid;

public class GroovyKernel extends Kernel {

  public GroovyKernel(final String id, final Evaluator evaluator) {
    super(id, evaluator);
  }

  public CommOpenHandler getCommOpenHandler(Kernel kernel) {
    return new GroovyCommOpenHandler(kernel);
  }

  public static void main(final String[] args) throws InterruptedException, IOException {
    File config = getConfig(args);
    String id = uuid();
    GroovyKernel kernel = new GroovyKernel(id, new GroovyEvaluator(id, id));
    runKernel(config, kernel);
  }
}