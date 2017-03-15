package com.twosigma.beaker.cpp;

import com.twosigma.beaker.cpp.evaluator.CppEvaluator;
import com.twosigma.beaker.evaluator.Evaluator;
import com.twosigma.beaker.jupyter.handler.CommOpenHandler;
import com.twosigma.jupyter.ConfigurationFile;
import com.twosigma.jupyter.Kernel;
import com.twosigma.jupyter.KernelConfigurationFile;
import com.twosigma.jupyter.handler.KernelHandler;
import com.twosigma.jupyter.message.Message;


import java.io.IOException;

import static com.twosigma.beaker.jupyter.Utils.uuid;

public class CppKernel extends Kernel {

  public CppKernel(final String id, final Evaluator evaluator, ConfigurationFile config) {
    super(id, evaluator, config);
  }

  public CommOpenHandler getCommOpenHandler(Kernel kernel){
    return null;
  }

  public KernelHandler<Message> getKernelInfoHandler(Kernel kernel) {
    return null;
  }

  public static void main(final String[] args) throws InterruptedException, IOException {
    String id = uuid();
    CppKernel kernel = new CppKernel(id, new CppEvaluator(id, id), new KernelConfigurationFile(args));
    runKernel(kernel);
  }

}