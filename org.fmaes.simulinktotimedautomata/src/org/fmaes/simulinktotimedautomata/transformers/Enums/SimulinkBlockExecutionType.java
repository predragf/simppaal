package org.fmaes.simulinktotimedautomata.transformers.Enums;

public enum SimulinkBlockExecutionType {
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
  }
}
