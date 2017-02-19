package org.fmaes.simulinktotimedautomata.types.enums;
/*
 * ADDING NEW ENUM RULE: If you want to add new enum, the enum Id must be in capital, while the
 * value must be in lowercase.
 */

public enum SimulinkParametersEnum {
  LOCATION_ON_DISK {
    @Override
    public String toString() {
      return "model_location_on_disk";
    }
  },
  GLOBALID {
    @Override
    public String toString() {
      return "model_global_id";
    }
  },
  PARENT_SUBSYSTEM {
    @Override
    public String toString() {
      return "parent_subsystem";
    }
  },
  ROOT_MODEL {
    @Override
    public String toString() {
      return "root_model_name";
    }
  },
  INHERITANCE_TREE {
    @Override
    public String toString() {
      return "inheritance_tree";
    }
  },
  TRIGGERING_INFO {
    @Override
    public String toString() {
      return "triggering_info";
    }
  }
}
