package org.fmaes.simulinktotimedautomata.types.enums;

public enum SimulinkDeclaredParametersEnum {
  PORT {
    @Override
    public String toString() {
      return "Port";
    }
  },
  GOTOTAG {
    @Override
    public String toString() {
      return "GotoTag";
    }
  },
  SAMPLE_TIME_COMPOSITE {
    @Override
    public String toString() {
      return "sample_time";
    }
  },
  SOURCETYPE {
    @Override
    public String toString() {
      return "SourceType";
    }
  },
  SAMPLE_TIME_ATOMIC {
    @Override
    public String toString() {
      return "SampleTime";
    }
  },  
}
