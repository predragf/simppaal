/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.types;

import java.util.ArrayList;
import java.util.Collection;

import org.conqat.lib.simulink.model.SimulinkLine;
import org.conqat.lib.simulink.model.SimulinkPortBase;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
public class Neighbour {

  private SimulinkPortBase destinationPort;

  private SimulinkPortBase sourcePort;

  private SimulinkPortBase intermediateDestinationPort;

  private SimulinkBlockWrapper sourceBlock;

  private SimulinkBlockWrapper destinationBlock;

  public Neighbour(SimulinkPortBase _destinationPort, SimulinkPortBase _intermediateDestinationPort,
      SimulinkPortBase _sourcePort) {
    destinationPort = _destinationPort;
    intermediateDestinationPort = _intermediateDestinationPort;
    sourcePort = _sourcePort;
    sourceBlock = new SimulinkBlockWrapper(_sourcePort.getBlock());
    destinationBlock = new SimulinkBlockWrapper(_destinationPort.getBlock());
  }

  public Neighbour(SimulinkLine simulinkLine) {
    destinationPort = simulinkLine.getDstPort();
    sourcePort = simulinkLine.getSrcPort();
    sourceBlock = new SimulinkBlockWrapper(sourcePort.getBlock());
    destinationBlock = new SimulinkBlockWrapper(destinationPort.getBlock());
    intermediateDestinationPort = null;

  }

  public SimulinkBlockWrapper getSourceSimulinkBlock() {
    return sourceBlock;
  }

  public SimulinkBlockWrapper getDestinationSimulinkBlock() {
    return destinationBlock;
  }

  public SimulinkPortBase getDestinationPort() {
    return destinationPort;
  }

  public SimulinkPortBase getSourcePort() {
    return sourcePort;
  }

  public SimulinkPortBase getIntermediateDestinationPort() {
    return intermediateDestinationPort;
  }

  public void setIntermediateDestinationPort(SimulinkPortBase _idp) {
    intermediateDestinationPort = _idp;
  }

  public Neighbour clone(SimulinkLine _inLine) {
    SimulinkPortBase srcPort = _inLine.getSrcPort();
    return new Neighbour(this.getDestinationPort(), this.getIntermediateDestinationPort(), srcPort);
  }

}
