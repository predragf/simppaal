package org.fmaes.j2uppaal.datastructures.base;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class BaseUppaalElement {
  /* This is actually the tag name of the element */
  public String tagName;
  public List<UppaalAttribute> attributes;

  private void init() {
    tagName = "";
    attributes = new ArrayList<UppaalAttribute>();
  }

  private UppaalAttribute parseAttributeNode(Node attributeNode) {
    UppaalAttribute baseUppaalElementAttribute = new UppaalAttribute();
    if (attributeNode == null) {
      return baseUppaalElementAttribute;
    }
    baseUppaalElementAttribute.name = attributeNode.getNodeName();
    baseUppaalElementAttribute.value = attributeNode.getNodeValue();
    return baseUppaalElementAttribute;
  }

  private List<UppaalAttribute> parseElementAttributes(NamedNodeMap attributesNamedNodeMap) {
    List<UppaalAttribute> nodeAttributes = new ArrayList<UppaalAttribute>();
    UppaalAttribute baseUppaalElementAttribute;
    for (int index = 0; index < attributesNamedNodeMap.getLength(); index++) {
      baseUppaalElementAttribute = parseAttributeNode(attributesNamedNodeMap.item(index));
      nodeAttributes.add(baseUppaalElementAttribute);
    }
    return nodeAttributes;
  }

  private void parseBaseUppaalElement(Element domElement) {
    /*
     * if for some reason domElement is null, just return, do not parse anything.
     */
    if (domElement == null) {
      return;
    }
    tagName = domElement.getTagName();
    NamedNodeMap attributesNodeMap = domElement.getAttributes();
    attributes = parseElementAttributes(attributesNodeMap);
  }

  public BaseUppaalElement(Element domElement) {
    /* Initialize the field */
    init();
    /* call parsing function */
    parseBaseUppaalElement(domElement);
  }

  public BaseUppaalElement() {
    init();
  }

  public BaseUppaalElement(BaseUppaalElement elementCopy) {
    tagName = elementCopy.tagName;
    attributes = new ArrayList<UppaalAttribute>();
    for (UppaalAttribute uppaalAttribute : elementCopy.attributes) {
      attributes.add(uppaalAttribute.clone());
    }
  }

  public UppaalAttribute getAttributeByName(String attributeName) {
    UppaalAttribute attributeByName = null;
    for (UppaalAttribute attribute : attributes) {
      if (attribute.name.equals(attributeName)) {
        attributeByName = attribute;
      }
    }
    return attributeByName;
  }

  public Boolean existsAttributeByName(String attributeName) {
    Boolean existsAttribute = false;
    for (UppaalAttribute attribute : attributes) {
      if (attribute.name.toLowerCase().equals(attributeName.toLowerCase())) {
        existsAttribute = true;
        break;
      }
    }
    return existsAttribute;
  }

  public void addAttribute(String attributeName, String attributeValue) {
    if (!existsAttributeByName(attributeName)) {
      UppaalAttribute attributeToBeAdded = new UppaalAttribute(attributeName, attributeValue);
      attributes.add(attributeToBeAdded);
    }
  }

  public void addOrReplaceAttribute(String attributeName, String attributeValue) {
    if (existsAttributeByName(attributeName)) {
      UppaalAttribute existingAttribute = getAttributeByName(attributeName);
      existingAttribute.value = attributeValue;
    } else {
      addAttribute(attributeName, attributeValue);
    }
  }

  public String serializeAttributes() {
    String attributeSerialization = "";
    for (UppaalAttribute uppaalAttribute : attributes) {
      attributeSerialization +=
          String.format("%s =\"%s\" ", uppaalAttribute.name, uppaalAttribute.value);
    }
    return attributeSerialization.trim();
  }

  public BaseUppaalElement clone() {
    return new BaseUppaalElement(this);
  }
}
