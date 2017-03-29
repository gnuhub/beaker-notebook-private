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
package com.twosigma.beaker.mimetype;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;


public class SVGContainer extends MIMEContainer {

  public static MIMEContainer SVG(String data) {
    String code = "";
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = validateData(data, builder);
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      code = writer.getBuffer().toString().replaceAll("\n|\r", "");

    } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
      return addMimeType(TEXT_PLAIN, exceptionToString(e));
    }
    return addMimeType(IMAGE_SVG, code);
  }

  private static Document validateData(String data, DocumentBuilder builder) throws SAXException, IOException {
    Document doc;
    if (isValidURL(data)) {
      doc = builder.parse(new URL(data).openStream());
    } else if (exists(data)) {
      doc = builder.parse(data);
    } else {
      doc = builder.parse(new ByteArrayInputStream(data.getBytes()));
    }
    return doc;
  }
}
