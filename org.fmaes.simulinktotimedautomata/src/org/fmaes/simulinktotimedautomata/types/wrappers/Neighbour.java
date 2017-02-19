package org.fmaes.simulinktotimedautomata.types.wrappers;

public class Neighbour {

  private SimulinkPortBaseWrapper fromPort;
  private SimulinkPortBaseWrapper toPort;
  private SimulinkBlockWrapper simulinkBlock;

  public Neighbour() {
    fromPort = toPort = null;
    simulinkBlock = new SimulinkBlockWrapper(null);
  }

  public Neighbour(SimulinkPortBaseWrapper _fromPort, SimulinkBlockWrapper _simulinkBlock) {
    setFromPort(_fromPort);
    setSimulinkBlock(_simulinkBlock);
  }

  public SimulinkPortBaseWrapper getFromPort() {
    return fromPort;
  }

  public void setFromPort(SimulinkPortBaseWrapper _fromPort) {
    fromPort = _fromPort;
  }

  public SimulinkBlockWrapper getSimulinkBlock() {
    return simulinkBlock;
  }

  public void setSimulinkBlock(SimulinkBlockWrapper _simulinkBlock) {
    simulinkBlock = _simulinkBlock;
  }

  public void setToPort(SimulinkPortBaseWrapper _toPort) {
    this.toPort = _toPort;
  }

  public SimulinkPortBaseWrapper getToPort() {
    return this.toPort;
  }

}
