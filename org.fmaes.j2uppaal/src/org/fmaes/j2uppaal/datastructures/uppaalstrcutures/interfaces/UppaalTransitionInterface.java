package org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces;

import java.util.Collection;

import org.fmaes.j2uppaal.datastructures.base.CompositeUppaalElement;

public interface UppaalTransitionInterface {

  public String getSourceLocationId();

  public void setSourceLocationId(String sourceLocationId);

  public String getTargetLocationId();

  public void setTargetLocationId(String targetLocationId);

  public UppaalLabelInterface getLabelByKind(String labelKind);

  public void addOrReplaceLabel(UppaalLabelInterface label);

  public Collection<UppaalLabelInterface> getAllLabels();

  public CompositeUppaalElement clone();
}
