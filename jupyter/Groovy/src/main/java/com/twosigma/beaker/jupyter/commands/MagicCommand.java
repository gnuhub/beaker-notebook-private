package com.twosigma.beaker.jupyter.commands;

import com.twosigma.beaker.jupyter.msg.MessageCreator;
import org.lappsgrid.jupyter.groovy.GroovyKernel;
import org.lappsgrid.jupyter.groovy.msg.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * executes magic commands and sends message
 *
 * @author lasha
 */
public class MagicCommand {
  public Map<String, MagicCommandFunctionality> commands = new HashMap<String, MagicCommandFunctionality>();
  private MessageCreator messageCreator;

  public MagicCommand(GroovyKernel kernel) {
    messageCreator = new MessageCreator(kernel);
    buildCommands();
  }


  public void processUnknownCommand(String command, Message message, int executionCount) throws InterruptedException, NoSuchAlgorithmException, IOException {
    String result = "Cell magic " + command + " not found";
    messageCreator.createMagicMessage(messageCreator.buildOutputMessage(message,result,true),executionCount,message);
  }

  private void buildCommands() {
    commands.put("%%javascript", (code, message,executionCount) -> {
      code = "<html><script>" + code.replace("%%javascript", "") + "</script></html>";
      messageCreator.createMagicMessage(messageCreator.buildMessage(message, code, executionCount),executionCount,message);
    });
    commands.put("%%html", (code, message,executionCount) -> {
      code = "<html>" + code.replace("%%html", "") + "</html>";
      messageCreator.createMagicMessage(messageCreator.buildMessage(message, code, executionCount),executionCount,message);
    });
    commands.put("%%bash", (code, message,executionCount) -> {
      String result = executeBashCode(code.replace("%%bash", ""));
      messageCreator.createMagicMessage(messageCreator.buildOutputMessage(message,result,false),executionCount,message);
    });
    commands.put("%%cd", (code, message, executionCount) -> {
      code = code.replace("%%cd", "").isEmpty() ? "pwd" : code.replace("%%cd", "cd") + "; pwd";
      String result = executeBashCode(code);
      messageCreator.createMagicMessage(messageCreator.buildOutputMessage(message,result,false),executionCount,message);
    });
    commands.put("%%lsmagic", (code, message,executionCount) -> {
      String result = "Available magic commands:\n";
      for (Map.Entry<String, MagicCommandFunctionality> entry : commands.entrySet()) {
        result += entry.getKey() + " ";
      }
      messageCreator.createMagicMessage(messageCreator.buildOutputMessage(message,result,false),executionCount,message);
    });
  }

  private String executeBashCode(String code) throws IOException, InterruptedException {
    String[] cmd = {"/bin/bash", "-c", code};
    ProcessBuilder pb = new ProcessBuilder(cmd);
    pb.redirectErrorStream(true);
    Process process = pb.start();
    process.waitFor();
    String line;
    StringBuffer output = new StringBuffer();
    BufferedReader reader = new BufferedReader(new InputStreamReader(
        process.getInputStream()));
    while ((line = reader.readLine()) != null) {
      output.append(line + "\n");
    }
    process.destroy();
    return output.toString();
  }

}
