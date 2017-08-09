package org.fmaes.j2uppaal.datastructures.uppaalstructures;

import java.util.ArrayList;
import java.util.Collection;

import org.fmaes.j2uppaal.datastructures.base.BaseUppaalElement;
import org.fmaes.j2uppaal.datastructures.base.CompositeUppaalElement;
import org.fmaes.j2uppaal.datastructures.base.SimpleUppaalElement;
import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.SerializableInterface;
import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.UppaalAutomatonInterface;
import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.UppaalDocumentInterface;
import org.w3c.dom.Element;

public class UppaalDocument extends CompositeUppaalElement
    implements UppaalDocumentInterface, SerializableInterface {

  private final String doctype =
      "nta PUBLIC '-//Uppaal Team//DTD Flat System 1.1//EN' 'http://www.it.uu.se/research/group/darts/uppaal/flat-1_2.dtd'";

  public UppaalDocument() {
    // TODO Auto-generated constructor stub
  }

  public UppaalDocument(Element xmlRootElement) {
    super(xmlRootElement);
  }

  public UppaalDocument(UppaalDocument document) {
    super(document);
    tagName = document.tagName;
  }

  @Override
  public String getDeclaration() {
    SimpleUppaalElement simpleChildElementByName =
        getSimpleChildElementByName(UppaalDocumentElementNamesEnum.DECLARATION.toString());
    return simpleChildElementByName.value;
  }

  @Override
  public void setDeclaration(String declarationValue) {
    // TODO Auto-generated method stub
    if (declarationValue == null) {
      return;
    }
    setChildElementValueByName(UppaalDocumentElementNamesEnum.DECLARATION.toString(),
        declarationValue);
  }

  @Override
  public UppaalAutomatonInterface getAutomatonByName(String automatonName) {
    // TODO Auto-generated method stub
    UppaalAutomaton resultAutomaton = null;
    Collection<BaseUppaalElement> allAutomataInDocumentCollection =
        getChildrenElementsByName(UppaalDocumentElementNamesEnum.AUTOMATON.toString());
    ArrayList<BaseUppaalElement> automatonArray =
        new ArrayList<BaseUppaalElement>(allAutomataInDocumentCollection);

    for (BaseUppaalElement baseUppaalElement : automatonArray) {
      CompositeUppaalElement compositeForCheck = (CompositeUppaalElement) baseUppaalElement;
      UppaalAutomaton currentAutomatonForCheck = new UppaalAutomaton(compositeForCheck);
      if (currentAutomatonForCheck.getName().toLowerCase().equals(automatonName.toLowerCase())) {
        resultAutomaton = currentAutomatonForCheck;
        break;
      }
    }
    return resultAutomaton;
  }

  @Override
  public void addAutomaton(UppaalAutomatonInterface automaton) {
    // TODO Auto-generated method stub
    UppaalAutomaton automatonInstance = (UppaalAutomaton) automaton;
    addOrReplaceChildElement(automatonInstance);
  }

  public void addAndInstantiateAutomaton(UppaalAutomatonInterface automaton) {

    String documentDeclaration = this.getDeclaration();
    documentDeclaration =
        documentDeclaration.concat(String.format("%s \n", automaton.getSignalDeclaration()));
    this.setDeclaration(documentDeclaration);

    String documentSystem = this.getSystem();
    documentSystem = documentSystem
        .concat(String.format("%s = %s(); \n", automaton.getInstanceName(), automaton.getName()));
    this.setSystem(documentSystem);

    UppaalAutomaton automatonInstance = (UppaalAutomaton) automaton;
    addOrReplaceChildElement(automatonInstance);
  }

  @Override
  public Collection<UppaalAutomatonInterface> getAllAutomata() {
    // TODO Auto-generated method stub
    Collection<UppaalAutomatonInterface> allDocumentAutomata =
        new ArrayList<UppaalAutomatonInterface>();
    for (BaseUppaalElement documentChildElement : childrenUppaalElements) {
      if (documentChildElement.tagName.toLowerCase()
          .equals(UppaalDocumentElementNamesEnum.AUTOMATON.toString().toLowerCase())) {
        CompositeUppaalElement automatonAsComposite = (CompositeUppaalElement) documentChildElement;
        UppaalAutomaton automaton = new UppaalAutomaton(automatonAsComposite);
        allDocumentAutomata.add(automaton);
      }
    }
    return allDocumentAutomata;
  }

  @Override
  public void setSystem(String systemValue) {
    // TODO Auto-generated method stub
    if (systemValue == null) {
      return;
    }
    setChildElementValueByName(UppaalDocumentElementNamesEnum.SYSTEM.toString(), systemValue);

  }

  @Override
  public String getSystem() {
    // TODO Auto-generated method stub
    SimpleUppaalElement simpleChildElementByName =
        getSimpleChildElementByName(UppaalDocumentElementNamesEnum.SYSTEM.toString());
    return simpleChildElementByName.value;
  }

  @Override
  public void setQueries(String queriesValue) {
    // TODO Auto-generated method stub
    if (queriesValue == null) {
      return;
    }
    setChildElementValueByName(UppaalDocumentElementNamesEnum.QUERIES.toString(), queriesValue);
  }

  @Override
  public String getQueries() {
    SimpleUppaalElement simpleChildElementByName =
        getSimpleChildElementByName(UppaalDocumentElementNamesEnum.QUERIES.toString());
    return simpleChildElementByName.value;
  }

  @Override
  public String serializeToXML() {
    // TODO Auto-generated method stub
    return super.serializeToXML();
  }

  @Override
  public void saveToFile(String pathToFile) {
    // TODO Auto-generated method stub
    // TODO Auto-generated method stub
    String xmlSerializedString = this.serializeToXML();
    java.io.FileWriter fileWriter = null;
    try {
      fileWriter = new java.io.FileWriter(pathToFile);
      fileWriter.write(xmlSerializedString);
      fileWriter.close();
    } catch (Exception ex) {
      // TODO: Log the error
    }
  }

  @Override
  public CompositeUppaalElement clone() {
    return new UppaalDocument(this);
  }

}
