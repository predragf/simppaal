package org.fmaes.j2uppaal.builders;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.fmaes.j2uppaal.datastructures.uppaalstructures.UppaalDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class UppaalDocumentBuilder {

  public static UppaalDocument buildUppaalDocument(String uppaalFileRelativePath) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    UppaalDocument uppaalDocument = null;
    try {
      builder = factory.newDocumentBuilder();
      String content = new String(Files.readAllBytes(Paths.get(uppaalFileRelativePath)));
      //System.out.println(content);
      StringReader sr = new StringReader(content);
      InputSource is = new InputSource(sr);
      Document document = builder.parse(is);
      Element root = document.getDocumentElement();
      uppaalDocument = new UppaalDocument(root);
    }
    catch(Exception ex){
      System.err.println(ex.getMessage());
    }
    return uppaalDocument;
  }

}
