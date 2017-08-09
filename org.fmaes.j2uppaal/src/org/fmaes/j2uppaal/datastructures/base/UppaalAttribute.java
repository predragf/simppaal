package org.fmaes.j2uppaal.datastructures.base;

public class UppaalAttribute {
  public String name;
  public String value;

  public UppaalAttribute(String _name, String _value) {
    name = _name;
    value = _value;
  }

  public UppaalAttribute() {
    name = "";
    value = "";
  }

  public UppaalAttribute(UppaalAttribute attr) {
    name = attr.name;
    value = attr.value;
  }

  public UppaalAttribute clone() {
    return new UppaalAttribute(this);
  }
}
