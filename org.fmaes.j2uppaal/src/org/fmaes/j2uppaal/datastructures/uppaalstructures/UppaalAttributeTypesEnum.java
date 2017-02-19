package org.fmaes.j2uppaal.datastructures.uppaalstructures;

public enum UppaalAttributeTypesEnum {
  KIND {
    @Override
    public String toString() {
      return "kind";
    }
  },
  XCOORDINATE {
    @Override
    public String toString() {
      return "x";
    }
  },
  YCOORDINATE {
    @Override
    public String toString() {
      return "y";
    }
  },
  REFERENCE {
    @Override
    public String toString() {
      return "ref";
    }
  },
  ID {
    @Override
    public String toString() {
      return "id";
    }
  },

}
