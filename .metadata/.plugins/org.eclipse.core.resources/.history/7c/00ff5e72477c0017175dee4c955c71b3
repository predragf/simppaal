package org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces;

import java.util.Collection;

public interface UppaalAutomatonInterface {

  public String getName();

  public void setName(String automatonName);

  public String getDeclaration();

  public void setDeclaration(String declaration);

  public UppaalLocationInterface getLocationById(String locationId);

  public UppaalLocationInterface getLocationByName(String locationName);

  public void addOrReplaceLocation(UppaalLocationInterface location);

  public Collection<UppaalLocationInterface> getAllLocations();

  public UppaalTransitionInterface getTransition(String sourceLocationId, String targetLocationId);

  public void addOrReplaceTransition(UppaalTransitionInterface transition);

  public Collection<UppaalTransitionInterface> getAllTransitions();

  public void setInitialLocation(String locationId);

  public Collection<UppaalTransitionInterface> getTransitionsFromLocationByName(
      String locationName);

  public Collection<UppaalTransitionInterface> getTransitionsToLocationByName(String locationName);

  public void setSignalName(String _signalName);

  public String getSignalName();

  public void setInstanceName(String _instanceName);

  public String getInstanceName();
  
  public void setSignalDeclaration(String _signalDec);
  
  public String getSignalDeclaration();



}
