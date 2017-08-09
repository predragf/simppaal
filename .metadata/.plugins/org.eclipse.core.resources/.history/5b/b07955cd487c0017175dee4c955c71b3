package org.fmaes.j2uppaal.datastructures.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.SerializableInterface;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CompositeUppaalElement extends BaseUppaalElement implements SerializableInterface {

  protected List<BaseUppaalElement> childrenUppaalElements;

  private CompositeUppaalElement parseCompositeChild(Element compositeElement) {
    CompositeUppaalElement compositeChild = new CompositeUppaalElement(compositeElement);
    return compositeChild;
  }

  private SimpleUppaalElement parseSimpleChild(Element simpleElement) {
    SimpleUppaalElement simpleChild = new SimpleUppaalElement(simpleElement);

    return simpleChild;
  }

  private Boolean hasNodeXmlChildren(Element element) {
    Boolean hasChildren = false;
    NodeList internalElements = element.getChildNodes();
    for (int i = 0; i < internalElements.getLength(); i++) {
      if (internalElements.item(i).getNodeType() == Node.ELEMENT_NODE) {
        hasChildren = true;
        break;
      }
    }
    return hasChildren;
  }

  private BaseUppaalElement parseChildElementNode(Node childNode) {
    BaseUppaalElement parsedChildNode = new BaseUppaalElement();
    Element childElement = (Element) childNode;
    if (hasNodeXmlChildren(childElement)) {
      parsedChildNode = parseCompositeChild(childElement);
    } else {
      parsedChildNode = parseSimpleChild(childElement);
    }
    return parsedChildNode;
  }

  private List<BaseUppaalElement> parseCompositeElementChildren(NodeList compositeElementChildren) {
    List<BaseUppaalElement> childrenUppaalElements = new ArrayList<BaseUppaalElement>();

    for (int index = 0; index < compositeElementChildren.getLength(); index++) {
      Node childNode = compositeElementChildren.item(index);
      if (childNode != null && childNode.getNodeType() == Node.ELEMENT_NODE) {
        BaseUppaalElement parsedChildNode = parseChildElementNode(childNode);
        childrenUppaalElements.add(parsedChildNode);
      }
    }
    return childrenUppaalElements;
  }

  private void parseCompositeUppaalElement(Element domElement) {
    NodeList compositeElementChildren = domElement.getChildNodes();
    childrenUppaalElements = parseCompositeElementChildren(compositeElementChildren);

  }

  public CompositeUppaalElement(Element domElement) {
    super(domElement);
    // TODO Auto-generated constructor stub
    parseCompositeUppaalElement(domElement);
  }

  public CompositeUppaalElement() {
    super();
    childrenUppaalElements = new ArrayList<BaseUppaalElement>();
  }

  public CompositeUppaalElement(CompositeUppaalElement copyElement) {
    super(copyElement);
    childrenUppaalElements = copyElement.childrenUppaalElements;
  }

  public Collection<BaseUppaalElement> getChildrenElementsByName(String name) {
    Collection<BaseUppaalElement> childrenByName = new ArrayList<BaseUppaalElement>();
    for (BaseUppaalElement childElement : childrenUppaalElements) {
      if (childElement.tagName.toLowerCase().equals(name.toLowerCase())) {
        childrenByName.add(childElement);
      }
    }
    return childrenByName;
  }

  /**
   * This function returns a single child element by name. It is meant to be used for a signle child
   * elements of composite ones (such as name, declaration, system, etc.). If called with name that
   * corresponds to multiple child elements it will return the first one.
   * 
   * @param name
   * @return
   */
  public BaseUppaalElement getChildElementByName(String name) {
    Collection<BaseUppaalElement> childrenByName = getChildrenElementsByName(name);
    Iterator<BaseUppaalElement> childrenCollectionIterator = childrenByName.iterator();
    BaseUppaalElement singleChildByName = null;
    if (childrenCollectionIterator.hasNext()) {
      singleChildByName = childrenCollectionIterator.next();
    }
    return singleChildByName;
  }

  public void setChildElementValueByName(String childName, String childValue) {
    BaseUppaalElement childByName = getChildElementByName(childName);
    if (childByName != null) {
      SimpleUppaalElement childAsSimpleUppaalElement = (SimpleUppaalElement) childByName;
      childAsSimpleUppaalElement.value = childValue;
    }
  }

  public SimpleUppaalElement getSimpleChildElementByName(String name) {
    BaseUppaalElement childElementByName = getChildElementByName(name);
    SimpleUppaalElement simpleChildElementByName = (SimpleUppaalElement) childElementByName;
    return simpleChildElementByName;
  }

  public Boolean existsChildElementByName(String childElementName) {
    Collection<BaseUppaalElement> childrenByName = getChildrenElementsByName(childElementName);
    return !childrenByName.isEmpty();
  }

  public void removeChildElement(BaseUppaalElement childToBeRemoved) {
    childrenUppaalElements.remove(childToBeRemoved);
  }

  public void addOrReplaceChildElement(BaseUppaalElement childToBeAdded) {
    int numberofchildren = this.childrenUppaalElements.size();
    if (!childToBeAdded.tagName.toLowerCase().equals("template")
        && existsChildElementByName(childToBeAdded.tagName)) {
      BaseUppaalElement existinChildFromList = getChildElementByName(childToBeAdded.tagName);
      removeChildElement(existinChildFromList);
    }
    if (childToBeAdded.tagName.toLowerCase().equals("declaration")) {
      /* queries is always the first */
      childrenUppaalElements.add(0, childToBeAdded);
    } else if (childToBeAdded.tagName.toLowerCase().equals("system")) {
      childrenUppaalElements.add(numberofchildren - 2, childToBeAdded);
    } else if (childToBeAdded.tagName.toLowerCase().equals("queries")) {
      /* queries is always the last */
      childrenUppaalElements.add(childToBeAdded);
    }

    else if (childToBeAdded.tagName.toLowerCase().equals("template")) {
      /*
       * This is the same as system, but it is different since in this case the system children
       * exisists in the model.
       */
      childrenUppaalElements.add(numberofchildren - 2, childToBeAdded);
    } else {
      childrenUppaalElements.add(childToBeAdded);
    }

  }

  @Override
  public String serializeToXML() {
    String compositeTemplate = "<#tag# #attributes#>\n#innercontents#</#tag#>\n";
    String elementAsXml = "";
    String childrenAsXml = "";
    for (BaseUppaalElement child : childrenUppaalElements) {
      if (child instanceof SimpleUppaalElement) {
        SimpleUppaalElement simpleChild = (SimpleUppaalElement) child;
        childrenAsXml += simpleChild.serializeToXML();
      }
      if (child instanceof CompositeUppaalElement) {
        CompositeUppaalElement compositeChild = (CompositeUppaalElement) child;
        childrenAsXml += compositeChild.serializeToXML();
      }
    }
    elementAsXml = compositeTemplate.replaceAll("#tag#", this.tagName)
        .replaceAll("#attributes#", serializeAttributes())
        .replaceAll("#innercontents#", childrenAsXml);
    return elementAsXml;
  }
}
