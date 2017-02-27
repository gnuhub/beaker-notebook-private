package com.twosigma.beaker.jupyter.commands;

import org.lappsgrid.jupyter.groovy.msg.Message;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


public interface MagicCommandFunctionality {
  void process(String code, Message message, int executionCount) throws IOException, InterruptedException, NoSuchAlgorithmException;
}
