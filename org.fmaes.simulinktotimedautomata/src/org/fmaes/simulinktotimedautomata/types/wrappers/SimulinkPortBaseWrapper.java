package org.fmaes.simulinktotimedautomata.types.wrappers;

import org.conqat.lib.simulink.model.SimulinkBlock;
import org.conqat.lib.simulink.model.SimulinkPortBase;
import org.conqat.lib.simulink.model.datahandler.LabelLayoutData;
import org.fmaes.simulinktotimedautomata.util.Util;

public class SimulinkPortBaseWrapper {

  private SimulinkPortBase simulinkPortBase;

  @SuppressWarnings("unused")
  private SimulinkPortBaseWrapper() {

  }

  public SimulinkPortBaseWrapper(SimulinkPortBase _simulinkPortBase) {
    simulinkPortBase = _simulinkPortBase;
  }

  public String getPortName() {
    SimulinkBlock portParentBlock = simulinkPortBase.getBlock();
    String portName = "";
    LabelLayoutData layoutData = simulinkPortBase.obtainLabelData();
    if (layoutData != null) {
      portName = layoutData.getText();
    } else {
      String longName = simulinkPortBase.toString();
      String[] longNameArray = longName.split("/");
      portName = longNameArray[longNameArray.length - 1];
    }
    return portName;
  }

  public String getPortIndex() {
    return simulinkPortBase.getIndex();
  }

  public SimulinkBlockWrapper getBlockRepresentation() {
    SimulinkBlockWrapper wrappedContainerBlock =
        new SimulinkBlockWrapper(simulinkPortBase.getBlock());
    return wrappedContainerBlock;
  }

  public String getType() {
    String portType = "";
    SimulinkBlockWrapper portBlockRepresentation = getBlockRepresentation();
    if (portBlockRepresentation.getType().toLowerCase().equals("outport")) {
      portType = "outport";
    } else if (portBlockRepresentation.getType().toLowerCase().equals("inport")) {
      portType = "inport";
    }
    return portType;
  }

  public Boolean matches(SimulinkPortBaseWrapper portToMatch) {
    String localPortName = this.getPortName();
    String portToMatchName = portToMatch.getPortName();
    Boolean doPortsMatch = false;
    if (!Util.stringNullOrEmpty(localPortName) && !Util.stringNullOrEmpty(portToMatchName)) {
      doPortsMatch = Util.matchStringsIgnoreCase(localPortName, portToMatchName);
    } else {
      doPortsMatch = Util.matchStringsIgnoreCase(this.getPortIndex(), portToMatch.getPortIndex());
    }
    return doPortsMatch;
  }

  public SimulinkPortBase getPortBase() {
    return simulinkPortBase;
  }

  public String toString() {
    return simulinkPortBase.toString();
  }
}
