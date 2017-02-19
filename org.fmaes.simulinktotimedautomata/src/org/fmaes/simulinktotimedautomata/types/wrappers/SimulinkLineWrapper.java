package org.fmaes.simulinktotimedautomata.types.wrappers;

import org.conqat.lib.simulink.model.SimulinkLine;
import org.conqat.lib.simulink.model.SimulinkPortBase;

public class SimulinkLineWrapper {

  private SimulinkLine simulinkLine;

  @SuppressWarnings("unused")
  private SimulinkLineWrapper() {

  }

  public SimulinkLineWrapper(SimulinkLine _simulinkLine) {
    simulinkLine = _simulinkLine;
  }

  public SimulinkPortBaseWrapper getSourcePort() {
    return new SimulinkPortBaseWrapper(simulinkLine.getSrcPort());
  }

  public SimulinkPortBaseWrapper getDestinationPort() {
    SimulinkPortBase basePort = simulinkLine.getDstPort();
    return new SimulinkPortBaseWrapper(basePort);
  }

  public SimulinkBlockWrapper getSourceBlock() {
    SimulinkPortBaseWrapper srcPort = getSourcePort();
    return srcPort.getBlockRepresentation();
  }

  public SimulinkBlockWrapper getDestinationBlock() {
    SimulinkPortBaseWrapper dstPort = getDestinationPort();
    return dstPort.getBlockRepresentation();
  }

}
