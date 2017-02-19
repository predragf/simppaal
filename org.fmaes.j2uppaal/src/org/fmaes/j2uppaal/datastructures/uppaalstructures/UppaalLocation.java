package org.fmaes.j2uppaal.datastructures.uppaalstructures;

import java.util.ArrayList;
import java.util.Collection;

import org.fmaes.j2uppaal.datastructures.base.BaseUppaalElement;
import org.fmaes.j2uppaal.datastructures.base.CompositeUppaalElement;
import org.fmaes.j2uppaal.datastructures.base.SimpleUppaalElement;
import org.fmaes.j2uppaal.datastructures.base.UppaalAttribute;
import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.SerializableInterface;
import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.UppaalLabelInterface;
import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.UppaalLocationInterface;

public class UppaalLocation extends CompositeUppaalElement
    implements UppaalLocationInterface, SerializableInterface {

  public UppaalLocation() {
    // TODO Auto-generated constructor stub
    super();
    this.tagName = UppaalDocumentElementNamesEnum.LOCATION.toString();
  }

  public UppaalLocation(CompositeUppaalElement compositeElement) {
    super(compositeElement);
  }

  public UppaalLocation(UppaalLocation existinLocation) {
    super(existinLocation);
  }

  @Override
  public String getName() {
    SimpleUppaalElement simpleChildElementByName =
        getSimpleChildElementByName(UppaalDocumentElementNamesEnum.NAME.toString());
    return simpleChildElementByName.value;
  }

  @Override
  public void setName(String locationName) {
    // TODO Auto-generated method stub
    if (locationName == null) {
      return;
    }
    setChildElementValueByName(UppaalDocumentElementNamesEnum.NAME.toString(), locationName);

  }

  @Override
  public UppaalLabelInterface getLabelByKind(String labelKind) {
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
    // TODO Auto-generated method stub
    Collection<UppaalLabelInterface> locationLabels = new ArrayList<UppaalLabelInterface>();
    Collection<BaseUppaalElement> labelsAsBaseElements =
        getChildrenElementsByName(UppaalDocumentElementNamesEnum.LABEL.toString());
    for (BaseUppaalElement baseLabel : labelsAsBaseElements) {
      UppaalLabel label = (UppaalLabel) baseLabel;
      locationLabels.add(label);
    }
    return locationLabels;
  }

  @Override
  public Boolean isUrgent() {
    return existsChildElementByName(UppaalDocumentElementNamesEnum.LOCATION_URGENT.toString());
  }

  @Override
  public Boolean isCommitted() {
    // TODO Auto-generated method stub
    return existsChildElementByName(UppaalDocumentElementNamesEnum.LOCATION_COMMITTED.toString());
  }

  @Override
  public void setUrgent() {
    // TODO Auto-generated method stub
    if (isCommitted()) {
      BaseUppaalElement commitedElement =
          getChildElementByName(UppaalDocumentElementNamesEnum.LOCATION_COMMITTED.toString());
      removeChildElement(commitedElement);
    }
    SimpleUppaalElement urgent = new SimpleUppaalElement();
    urgent.tagName = UppaalDocumentElementNamesEnum.LOCATION_URGENT.toString();
    addOrReplaceChildElement(urgent);
  }

  @Override
  public void setCommitted() {
    // TODO Auto-generated method stub
    // TODO Auto-generated method stub
    if (isUrgent()) {
      BaseUppaalElement urgentElement =
          getChildElementByName(UppaalDocumentElementNamesEnum.LOCATION_URGENT.toString());
      removeChildElement(urgentElement);
    }
    SimpleUppaalElement committed = new SimpleUppaalElement();
    committed.tagName = UppaalDocumentElementNamesEnum.LOCATION_COMMITTED.toString();
    addOrReplaceChildElement(committed);

  }

  @Override
  public String serializeToXML() {
    // TODO Auto-generated method stub
    return super.serializeToXML();
  }

  @Override
  public String getId() {
    // TODO Auto-generated method stub
    SimpleUppaalElement simpleChildElementByName =
        getSimpleChildElementByName(UppaalDocumentElementNamesEnum.ID.toString());
    return simpleChildElementByName.value;
  }

  @Override
  public void setId(String id) {
    if (id == null) {
      return;
    }
    setChildElementValueByName(UppaalDocumentElementNamesEnum.ID.toString(), id);
  }
}
