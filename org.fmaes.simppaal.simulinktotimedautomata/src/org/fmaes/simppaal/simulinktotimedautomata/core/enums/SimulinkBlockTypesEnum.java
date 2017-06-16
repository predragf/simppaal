package org.fmaes.simppaal.simulinktotimedautomata.core.enums;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */

public enum SimulinkBlockTypesEnum {
  REFERENCE {
    @Override
    public String toString() {
      return "reference";
    }
  },
  MODELREFERENCE {
    @Override
    public String toString() {
      return "modelreference";
    }
  },
  MODEL {
    @Override
    public String toString() {
      return "model";
    }
  },
  SUBSYSTEM {
    @Override
    public String toString() {
      return "subsystem";
    }
  },
  GOTO {
    @Override
    public String toString() {
      return "goto";
    }
  },
  FROM {
    @Override
    public String toString() {
      return "from";
    }
  },
  INPORT {
    @Override
    public String toString() {
      return "inport";
    }
  },
  OUTPORT {
    @Override
    public String toString() {
      return "outport";
    }
  },
  FUNCTION_CALL_GENERATOR {
    @Override
    public String toString() {
      return "function-call generator";
    }
  },
}
