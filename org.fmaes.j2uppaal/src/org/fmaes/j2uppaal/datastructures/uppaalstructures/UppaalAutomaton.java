package org.fmaes.j2uppaal.datastructures.uppaalstructures;

import java.util.ArrayList;
import java.util.Collection;

import org.fmaes.j2uppaal.datastructures.base.BaseUppaalElement;
import org.fmaes.j2uppaal.datastructures.base.CompositeUppaalElement;
import org.fmaes.j2uppaal.datastructures.base.SimpleUppaalElement;
import org.fmaes.j2uppaal.datastructures.base.UppaalAttribute;
import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.SerializableInterface;
import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.UppaalAutomatonInterface;
import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.UppaalLocationInterface;
import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.UppaalTransitionInterface;

public class UppaalAutomaton extends CompositeUppaalElement
    implements UppaalAutomatonInterface, SerializableInterface {

  private String signalName;
  private String instanceName;
  private String signalDeclaration;

  @Override
  public String getName() {
    SimpleUppaalElement simpleChildElementByName =
        getSimpleChildElementByName(UppaalDocumentElementNamesEnum.NAME.toString());
    return simpleChildElementByName.value;
  }

  @Override
  public void setName(String automatonName) {
    // TODO Auto-generated method stub
    if (automatonName == null) {
      return;
    }
    setChildElementValueByName(UppaalDocumentElementNamesEnum.NAME.toString(), automatonName);
  }

  @Override
  public String getDeclaration() {
    // TODO Auto-generated method stub
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
  public UppaalLocationInterface getLocationById(String locationId) {
    // TODO Auto-generated method stub
    UppaalLocation locationToReturn = null;
    Collection<UppaalLocationInterface> allLocations = getAllLocations();
    for (UppaalLocationInterface uppaalLocation : allLocations) {
      UppaalLocation location = (UppaalLocation) uppaalLocation;
      String _id = location.getId();
      if (_id.toLowerCase().equals(locationId.toLowerCase())) {
        locationToReturn = location;
        break;
      }
    }
    return locationToReturn;
  }

  @Override
  public void addOrReplaceLocation(UppaalLocationInterface location) {
    // TODO Auto-generated method stub
    UppaalLocation locationInstance = (UppaalLocation) location;
    UppaalAttribute idAttribute =
        locationInstance.getAttributeByName(UppaalAttributeTypesEnum.ID.toString());
    UppaalLocationInterface existinLocationById = getLocationById(idAttribute.value);
    if (existinLocationById != null) {
      UppaalLocation locationToBeRemoved = (UppaalLocation) existinLocationById;
      removeChildElement(locationToBeRemoved);
    }
    childrenUppaalElements.add(locationInstance);

  }

  @Override
  public Collection<UppaalLocationInterface> getAllLocations() {
    // TODO Auto-generated method stub
    Collection<UppaalLocationInterface> automatonLocations =
        new ArrayList<UppaalLocationInterface>();
    Collection<BaseUppaalElement> locationsAsBasicElements =
        getChildrenElementsByName(UppaalDocumentElementNamesEnum.LOCATION.toString());
    for (BaseUppaalElement baseUppaalElement : locationsAsBasicElements) {
      CompositeUppaalElement locationAsComposite = (CompositeUppaalElement) baseUppaalElement;
      UppaalLocation uppaalLocation = new UppaalLocation(locationAsComposite);
      automatonLocations.add(uppaalLocation);
    }
    return automatonLocations;
  }

  @Override
  public UppaalTransitionInterface getTransition(String sourceLocationId, String targetLocationId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void addOrReplaceTransition(UppaalTransitionInterface transition) {
    // TODO Auto-generated method stub
    UppaalTransition transitionInstance = (UppaalTransition) transition;
    Collection<UppaalTransitionInterface> allTransitionsInAutomaton = getAllTransitions();
    for (UppaalTransitionInterface uppaalTransition : allTransitionsInAutomaton) {
      if (uppaalTransition.getSourceLocationId().toLowerCase()
          .equals(transition.getSourceLocationId().toLowerCase())
          && uppaalTransition.getTargetLocationId().toLowerCase()
              .equals(transition.getTargetLocationId().toLowerCase())) {
        UppaalTransition transitionToBeRemoved = (UppaalTransition) uppaalTransition;
        removeChildElement(transitionToBeRemoved);
      }
    }
    addOrReplaceChildElement(transitionInstance);
  }

  @Override
  public Collection<UppaalTransitionInterface> getAllTransitions() {
    // TODO Auto-generated method stub
    Collection<UppaalTransitionInterface> automatonTransitions =
        new ArrayList<UppaalTransitionInterface>();
    Collection<BaseUppaalElement> transitionsAsBasicElements =
        getChildrenElementsByName(UppaalDocumentElementNamesEnum.TRANSITION.toString());
    for (BaseUppaalElement baseUppaalElement : transitionsAsBasicElements) {
      UppaalTransitionInterface trnasitionAsComposite = (UppaalTransition) baseUppaalElement;
      automatonTransitions.add(trnasitionAsComposite);
    }
    return automatonTransitions;
  }

  @Override
  public void setInitialLocation(String locationId) {
    // TODO Auto-generated method stub
    BaseUppaalElement initialBaseElement = new SimpleUppaalElement();
    initialBaseElement.addOrReplaceAttribute(UppaalAttributeTypesEnum.REFERENCE.toString(),
        locationId);
    addOrReplaceChildElement(initialBaseElement);

  }

  public UppaalAutomaton() {
    super();
    tagName = UppaalDocumentElementNamesEnum.AUTOMATON.toString().toLowerCase();
    instanceName = signalName = "";
  }

  public UppaalAutomaton(CompositeUppaalElement compositeUppaalElement) {
    super(compositeUppaalElement);
    tagName = UppaalDocumentElementNamesEnum.AUTOMATON.toString().toLowerCase();
    signalName = "";
    instanceName = "";
    signalDeclaration = "";
  }

  public UppaalAutomaton(UppaalAutomaton existingAutomaton) {
    super(existingAutomaton);
    tagName = existingAutomaton.tagName;
    signalName = existingAutomaton.signalDeclaration;
    instanceName = existingAutomaton.instanceName;
    signalDeclaration = existingAutomaton.signalDeclaration;
  }

  @Override
  public UppaalLocationInterface getLocationByName(String locationName) {
    // TODO Auto-generated method stub
    UppaalLocationInterface resultLocation = null;
    Collection<UppaalLocationInterface> allLocations = getAllLocations();
    for (UppaalLocationInterface uppaalLocation : allLocations) {
      String _locationName = uppaalLocation.getName();
      if (_locationName.toLowerCase().equals(locationName.toLowerCase())) {
        resultLocation = uppaalLocation;
        break;
      }
    }
    return resultLocation;
  }

  @Override
  public Collection<UppaalTransitionInterface> getTransitionsFromLocationByName(
      String locationName) {
    // TODO Auto-generated method stub
    Collection<UppaalTransitionInterface> allTransitions = this.getAllTransitions();
    Collection<UppaalTransitionInterface> resultTransitions =
        new ArrayList<UppaalTransitionInterface>();
    UppaalLocationInterface uppaalLocationByName = this.getLocationByName(locationName);
    if (uppaalLocationByName == null) {
      return resultTransitions;
    }
    String locationId = uppaalLocationByName.getId();
    for (UppaalTransitionInterface transition : allTransitions) {
      if (transition.getSourceLocationId().equals(locationId)) {
        resultTransitions.add(transition);
      }
    }
    return resultTransitions;
  }

  @Override
  public Collection<UppaalTransitionInterface> getTransitionsToLocationByName(String locationName) {
    // TODO Auto-generated method stub
    Collection<UppaalTransitionInterface> allTransitions = this.getAllTransitions();
    Collection<UppaalTransitionInterface> resultTransitions =
        new ArrayList<UppaalTransitionInterface>();
    UppaalLocationInterface uppaalLocationByName = this.getLocationByName(locationName);
    if (uppaalLocationByName == null) {
      return resultTransitions;
    }
    String locationId = uppaalLocationByName.getId();
    for (UppaalTransitionInterface transition : allTransitions) {
      if (transition.getTargetLocationId().equals(locationId)) {
        resultTransitions.add(transition);
      }
    }
    return resultTransitions;
  }

  @Override
  public String serializeToXML() {
    // TODO Auto-generated method stub
    return super.serializeToXML();
  }

  @Override
  public void setSignalName(String _signalName) {
    // TODO Auto-generated method stub
    this.signalName = _signalName;
  }

  @Override
  public String getSignalName() {
    // TODO Auto-generated method stub
    return this.signalName;
  }

  @Override
  public void setInstanceName(String _instanceName) {
    // TODO Auto-generated method stub
    this.instanceName = _instanceName;
  }

  @Override
  public String getInstanceName() {
    // TODO Auto-generated method stub
    return this.instanceName;
  }

  public void setSignalDeclaration(String _signalDec) {
    signalDeclaration = _signalDec;
  }

  public String getSignalDeclaration() {
    return signalDeclaration;
  }

  @Override
  public CompositeUppaalElement clone() {
    return new UppaalAutomaton(this);
  }
}
