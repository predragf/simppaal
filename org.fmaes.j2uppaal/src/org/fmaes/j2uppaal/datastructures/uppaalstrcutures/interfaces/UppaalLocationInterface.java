package org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces;

import java.util.Collection;

public interface UppaalLocationInterface {
  
  public String getName();
  
  public void setName(String name);
  
  public String getId();
  
  public void setId(String id);
  
  public UppaalLabelInterface getLabelByKind(String labelKind);
  
  public void addOrReplaceLabel(UppaalLabelInterface label);
  
  public Collection<UppaalLabelInterface> getAllLabels();
  
  public Boolean isUrgent();
  
  public Boolean isCommitted();
  
  public void setUrgent();
  
  public void setCommitted();

}
