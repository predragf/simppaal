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

  public Neighbour(SimulinkPortBase _destinationPort, SimulinkPortBase _intermediateDestinationPort,
      SimulinkPortBase _sourcePort) {
    destinationPort = _destinationPort;
    intermediateDestinationPort = _intermediateDestinationPort;
    sourcePort = _sourcePort;
  }

  public Neighbour(SimulinkLine simulinkLine) {
    destinationPort = simulinkLine.getDstPort();
    sourcePort = simulinkLine.getSrcPort();
    intermediateDestinationPort = null;

  }

  public SimulinkBlockWrapper getSourceSimulinkBlock() {
    return new SimulinkBlockWrapper(sourcePort.getBlock());
  }

  public SimulinkBlockWrapper getDestinationSimulinkBlock() {
    return new SimulinkBlockWrapper(destinationPort.getBlock());
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

  public Neighbour clone(SimulinkLine _inLine) {
    SimulinkPortBase srcPort = _inLine.getSrcPort();
    return new Neighbour(this.getDestinationPort(), this.getIntermediateDestinationPort(), srcPort);
  }

}
