package org.fmaes.j2uppaal.datastructures.uppaalstructures;

public enum UppaalTransitionEnum {
  SOURCE {
    @Override
    public String toString() {
      return "source";
    }
  },
  TARGET {
    @Override
    public String toString() {
      return "target";
    }
  },
  LABEL {
    @Override
    public String toString() {
      return "label";
    }
  },

}
