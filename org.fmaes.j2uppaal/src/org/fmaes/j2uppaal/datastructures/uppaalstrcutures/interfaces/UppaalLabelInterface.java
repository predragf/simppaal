package org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces;

import org.fmaes.j2uppaal.datastructures.base.SimpleUppaalElement;

public interface UppaalLabelInterface {

  public void setKind(String kind);

  public String getKind();

  public void setValue(String value);

  public String getValue();

  public void setCoordinate(String coordinateName, String coordinateValue);

  public String getCoordinate(String coordinateName);

  public SimpleUppaalElement clone();

}
