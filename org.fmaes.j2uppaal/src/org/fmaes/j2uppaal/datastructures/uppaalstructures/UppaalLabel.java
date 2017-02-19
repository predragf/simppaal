package org.fmaes.j2uppaal.datastructures.uppaalstructures;

import org.fmaes.j2uppaal.datastructures.base.SimpleUppaalElement;
import org.fmaes.j2uppaal.datastructures.base.UppaalAttribute;
import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.UppaalLabelInterface;

public class UppaalLabel extends SimpleUppaalElement implements UppaalLabelInterface {

  public UppaalLabel() {
    super();
    this.tagName = UppaalDocumentElementNamesEnum.LABEL.toString().toLowerCase();
  }

  public UppaalLabel(SimpleUppaalElement simpleUppaalElement) {
    super(simpleUppaalElement);
  }

  public UppaalLabel(UppaalLabel existingLabel) {
    super(existingLabel);
  }

  @Override
  public void setKind(String value) {
    // TODO Auto-generated method stub
    if (value == null) {
      return;
    }
    UppaalAttribute labelKind = getAttributeByName(UppaalAttributeTypesEnum.KIND.toString());
    if (labelKind != null) {
      labelKind.value = value;
    } else {
      labelKind = new UppaalAttribute(UppaalAttributeTypesEnum.KIND.toString(), value);
      attributes.add(labelKind);
    }

  }

  @Override
  public String getKind() {
    // TODO Auto-generated method stub
    UppaalAttribute labelKind = getAttributeByName(UppaalAttributeTypesEnum.KIND.toString());
    if (labelKind == null) {
      labelKind = new UppaalAttribute();
    }
    return labelKind.value;
  }

  @Override
  public void setValue(String value) {
    // TODO Auto-generated method stub
    if (value != null) {
      this.value = value;
    }

  }

  @Override
  public String getValue() {
    // TODO Auto-generated method stub
    return this.value;
  }

  @Override
  public void setCoordinate(String coordinateName, String coordinateValue) {
    // TODO Auto-generated method stub
    if (value == null) {
      return;
    }
    UppaalAttribute coordinate = getAttributeByName(coordinateName);
    if (coordinate != null) {
      coordinate.value = coordinateValue;
    } else {
      coordinate = new UppaalAttribute(UppaalAttributeTypesEnum.KIND.toString(), coordinateValue);
      attributes.add(coordinate);
    }

  }

  @Override
  public String getCoordinate(String coordinateName) {
    // TODO Auto-generated method stub
    UppaalAttribute coordinate = getAttributeByName(coordinateName);
    if (coordinate == null) {
      coordinate = new UppaalAttribute();
    }
    return coordinate.value;
  }

}
