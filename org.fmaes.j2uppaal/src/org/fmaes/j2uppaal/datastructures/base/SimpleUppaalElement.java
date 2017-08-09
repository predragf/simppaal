package org.fmaes.j2uppaal.datastructures.base;

import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.SerializableInterface;
import org.w3c.dom.Element;

public class SimpleUppaalElement extends BaseUppaalElement implements SerializableInterface {

  public String value;

  private void parseSimpleUppaalElement(Element domElement) {
    value = domElement.getTextContent();
  }

  public SimpleUppaalElement(Element domElement) {
    super(domElement);
    /* initialize the value */
    value = "";
    /* call the parsing function */
    parseSimpleUppaalElement(domElement);
  }

  public SimpleUppaalElement() {
    super();
    value = "";
  }

  public SimpleUppaalElement(SimpleUppaalElement copyElement) {
    super(copyElement);
    value = copyElement.value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
  }

  private String getTemplate() {
    String nonValueTemplate = "<#tag# #attributes# />\n";
    String withValueTemplate = "<#tag# #attributes#>#value#</#tag#>\n";

    String elementTemplate = nonValueTemplate;
    if (this.value != null && this.value != "") {
      elementTemplate = withValueTemplate;
    }
    return elementTemplate;
  }

  @Override
  public String serializeToXML() {
    // TODO Auto-generated method stub
    String elementTemplate = getTemplate();
    String serializedElement = elementTemplate.replaceAll("#tag#", this.tagName)
        .replaceAll("#attributes#", serializeAttributes())
        .replaceAll("#value#", this.value.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
    return serializedElement;
  }

  public SimpleUppaalElement clone() {
    return new SimpleUppaalElement(this);
  }

}
