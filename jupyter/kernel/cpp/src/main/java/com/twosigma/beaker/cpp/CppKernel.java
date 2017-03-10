package com.twosigma.beaker.cpp;

import com.twosigma.beaker.cpp.evaluator.CppEvaluator;
import com.twosigma.beaker.evaluator.Evaluator;
import com.twosigma.beaker.jupyter.handler.CommOpenHandler;
import org.lappsgrid.jupyter.Kernel;

import java.io.File;
import java.io.IOException;

public class CppKernel extends Kernel {

  @Override
  public Evaluator getEvaluator(Kernel kernel) {
    return new CppEvaluator(kernel.getId(), kernel.getId());
  }

  public CommOpenHandler getCommOpenHandler(Kernel kernel){
    return null;
  }

  public static void main(final String[] args) throws InterruptedException, IOException {
    File config = getConfig(args);
    CppKernel kernel = new CppKernel();
    runKernel(config, kernel);
  }

}