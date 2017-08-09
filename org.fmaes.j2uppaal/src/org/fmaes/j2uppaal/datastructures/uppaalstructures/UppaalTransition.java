package org.fmaes.j2uppaal.datastructures.uppaalstructures;

import java.util.ArrayList;
import java.util.Collection;

import org.fmaes.j2uppaal.datastructures.base.BaseUppaalElement;
import org.fmaes.j2uppaal.datastructures.base.CompositeUppaalElement;
import org.fmaes.j2uppaal.datastructures.base.UppaalAttribute;
import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.UppaalLabelInterface;
import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.UppaalLocationInterface;
import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.UppaalTransitionInterface;

public class UppaalTransition extends CompositeUppaalElement implements UppaalTransitionInterface {

  public UppaalTransition() {
    super();
    tagName = UppaalDocumentElementNamesEnum.TRANSITION.toString().toLowerCase();
  }

  public UppaalTransition(CompositeUppaalElement compositeElement) {
    super(compositeElement);
    tagName = compositeElement.tagName;
  }

  public UppaalTransition(UppaalTransition existingTransition) {
    super(existingTransition);
  }

  @Override
  public String getSourceLocationId() {
    // TODO Auto-generated method stub
    String sourceLocationId = "";
    if (existsChildElementByName(UppaalTransitionEnum.SOURCE.toString())) {
      BaseUppaalElement sourceElement =
          getChildElementByName(UppaalTransitionEnum.SOURCE.toString());
      if (sourceElement.existsAttributeByName(UppaalAttributeTypesEnum.REFERENCE.toString())) {
        UppaalAttribute referenceAttribute =
            sourceElement.getAttributeByName(UppaalAttributeTypesEnum.REFERENCE.toString());
        sourceLocationId = referenceAttribute.value;
      }
    }
    return sourceLocationId;
  }

  @Override
  public void setSourceLocationId(String sourceLocationId) {
    // TODO Auto-generated method stub
    if (sourceLocationId == null) {
      return;
    }
    if (existsChildElementByName(UppaalTransitionEnum.SOURCE.toString())) {
      BaseUppaalElement sourceElement =
          getChildElementByName(UppaalTransitionEnum.SOURCE.toString());
      sourceElement.addOrReplaceAttribute(UppaalTransitionEnum.SOURCE.toString(), sourceLocationId);
    }
  }

  @Override
  public String getTargetLocationId() {
    // TODO Auto-generated method stub
    String targetLocationId = "";
    if (existsChildElementByName(UppaalTransitionEnum.TARGET.toString())) {
      BaseUppaalElement sourceElement =
          getChildElementByName(UppaalTransitionEnum.TARGET.toString());
      if (sourceElement.existsAttributeByName(UppaalAttributeTypesEnum.REFERENCE.toString())) {
        UppaalAttribute referenceAttribute =
            sourceElement.getAttributeByName(UppaalAttributeTypesEnum.REFERENCE.toString());
        targetLocationId = referenceAttribute.value;
      }
    }
    return targetLocationId;
  }

  @Override
  public void setTargetLocationId(String targetLocationId) {
    if (targetLocationId == null) {
      return;
    }
    if (existsChildElementByName(UppaalTransitionEnum.TARGET.toString())) {
      BaseUppaalElement sourceElement =
          getChildElementByName(UppaalTransitionEnum.TARGET.toString());
      sourceElement.addOrReplaceAttribute(UppaalTransitionEnum.TARGET.toString(), targetLocationId);
    }

  }

  @Override
  public UppaalLabelInterface getLabelByKind(String labelKind) {
    // TODO Auto-generated method stub
    UppaalLabel locationLabel = null;
    Collection<BaseUppaalElement> allLabels =
        getChildrenElementsByName(UppaalDocumentElementNamesEnum.LABEL.toString());
    for (BaseUppaalElement labelAsBaseElement : allLabels) {
      UppaalAttribute attributeKind =
          labelAsBaseElement.getAttributeByName(UppaalAttributeTypesEnum.KIND.toString());
      if (attributeKind.value.toLowerCase().equals(labelKind.toLowerCase())) {

      }
    }
    return locationLabel;
  }

  @Override
  public void addOrReplaceLabel(UppaalLabelInterface label) {
    // TODO Auto-generated method stub
    UppaalLabelInterface existingLabel = getLabelByKind(label.getKind());
    if (existingLabel != null) {
      UppaalLabel existingLabelInstance = (UppaalLabel) existingLabel;
      childrenUppaalElements.remove(existingLabelInstance);
    }
    UppaalLabel newLabelInstance = (UppaalLabel) label;
    childrenUppaalElements.add(newLabelInstance);
  }

  @Override
  public Collection<UppaalLabelInterface> getAllLabels() {
    Collection<BaseUppaalElement> allTransitionLabelsasBasicElements =
        getChildrenElementsByName(UppaalDocumentElementNamesEnum.LABEL.toString());
    Collection<UppaalLabelInterface> allTransitionLabels = new ArrayList<UppaalLabelInterface>();
    for (BaseUppaalElement baseUppaalTransition : allTransitionLabelsasBasicElements) {
      UppaalLabel label = (UppaalLabel) baseUppaalTransition;
      allTransitionLabels.add(label);
    }
    return allTransitionLabels;
  }

  @Override
  public CompositeUppaalElement clone() {
    return new UppaalTransition(this);

  }

}
