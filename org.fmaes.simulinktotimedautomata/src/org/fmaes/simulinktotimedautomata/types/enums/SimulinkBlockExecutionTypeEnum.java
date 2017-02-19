package org.fmaes.simulinktotimedautomata.types.enums;

public enum SimulinkBlockExecutionTypeEnum {
  CONTINUOUS {
    @Override
    public String toString() {
      return "continuous";
    }
  },
  DISCRETE {
    @Override
    public String toString() {
      return "discrete";
    }
  },
}
