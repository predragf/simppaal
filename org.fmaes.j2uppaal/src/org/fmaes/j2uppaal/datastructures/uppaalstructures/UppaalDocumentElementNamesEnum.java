package org.fmaes.j2uppaal.datastructures.uppaalstructures;

public enum UppaalDocumentElementNamesEnum {

  ROOT {
    @Override
    public String toString() {
      return "nta";
    }
  },

  AUTOMATON {
    @Override
    public String toString() {
      return "template";
    }
  },

  LOCATION {
    @Override
    public String toString() {
      return "location";
    }
  },

  TRANSITION {
    @Override
    public String toString() {
      return "transition";
    }
  },

  LABEL {
    @Override
    public String toString() {
      return "label";
    }
  },

  DECLARATION {
    @Override
    public String toString() {
      return "declaration";
    }
  },

  NAME {
    @Override
    public String toString() {
      return "name";
    }
  },
  
  ID {
    @Override
    public String toString() {
      return "id";
    }
  },
  
  SYSTEM {
    @Override
    public String toString() {
      return "system";
    }
  },
  
  QUERIES {
    @Override
    public String toString() {
      return "queries";
    }
  },
  LOCATION_INITIAL {
    @Override
    public String toString() {
      return "init";
    }
  },
  LOCATION_COMMITTED {
    @Override
    public String toString() {
      return "committed";
    }
  },
  LOCATION_URGENT {
    @Override
    public String toString() {
      return "urgent";
    }
  },

}
