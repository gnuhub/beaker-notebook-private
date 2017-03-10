package org.lappsgrid.jupyter.handler;

import static com.twosigma.beaker.jupyter.msg.JupyterMessages.KERNEL_INFO_REPLY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.lappsgrid.jupyter.KernelFunctionality;
import org.lappsgrid.jupyter.msg.Header;
import org.lappsgrid.jupyter.msg.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KernelInfoHandler extends KernelHandler<Message> {

  private final static Logger logger = LoggerFactory.getLogger(KernelInfoHandler.class);

  public KernelInfoHandler(KernelFunctionality kernel) {
    super(kernel);
  }

  @Override
  public void handle(Message message) {
    logger.info("Processing kernel info request");
    Message reply = new Message();
    HashMap<String, Serializable> map = new HashMap<>(6);
    map.put("protocol_version", "5.0");
    map.put("implementation", "groovy");
    map.put("implementation_version", "1.0.0");
    HashMap<String, Serializable> map1 = new HashMap<String, Serializable>(7);
    map1.put("name", "Groovy");
    map1.put("version", "2.4.6");
    map1.put("mimetype", "");
    map1.put("file_extension", ".groovy");
    map1.put("pygments_lexer", "");
    map1.put("codemirror_mode", "groovy");
    map1.put("nbconverter_exporter", "");
    map.put("language_info", map1);
    map.put("banner", "BeakerX kernel for Apache Groovy");
    map.put("help_links", new ArrayList());
    reply.setContent(map);
    reply.setHeader(new Header(KERNEL_INFO_REPLY, message.getHeader().getSession()));
    reply.setParentHeader(message.getHeader());
    reply.setIdentities(message.getIdentities());
    send(reply);
  }

}