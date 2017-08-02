/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.types;

import java.util.ArrayList;
import java.util.Collection;

import org.conqat.lib.simulink.model.SimulinkBlock;
import org.conqat.lib.simulink.model.SimulinkLine;
import org.conqat.lib.simulink.model.SimulinkPortBase;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
public class SimulinkBlockWrapper {

  private SimulinkBlock simulinkBlock;

  private Collection<Neighbour> predecessors;

  private final String[] compositeBlockTypes =
      {"subsystem", "reference", "modelreference", "model"};

  private final String[] nonComputationalBlockTypes =
      {"mux", "demux", "goto", "from", "inport", "outport", "function-call generator"};

  /**
   * @param simulinkBlock
   */
  public SimulinkBlockWrapper(SimulinkBlock _simulinkBlock) {
    simulinkBlock = _simulinkBlock;
    predecessors = null;
  }

  public String getType() {
    return simulinkBlock.getType();
  }

  public String getParameter(String parameterName) {
    return simulinkBlock.getParameter(parameterName);
  }

  public void setParameter(String parameterName, String parameterValue) {
    simulinkBlock.setParameter(parameterName, parameterValue);
  }

  public String getId() {
    return simulinkBlock.getId();
  }

  public boolean exists() {
    return simulinkBlock != null;
  }

  public boolean isAtomic() {
    boolean isAtomic = true;

    for (String compositeBlockType : compositeBlockTypes) {
      if (this.getType().toLowerCase().equals(compositeBlockType)) {
        isAtomic = false;
        break;
      }
    }

    return isAtomic;
  }

  public boolean isComputational() {
    boolean isComputational = true;

    for (String nonComputationalBlockType : nonComputationalBlockTypes) {
      if (this.getType().toLowerCase().equals(nonComputationalBlockType)) {
        isComputational = false;
        break;
      }
    }

    return isComputational;
  }

  public Collection<SimulinkLine> getInLines() {
    return this.simulinkBlock.getInLines();
  }

  public Collection<SimulinkLine> getInLinesByPortIndex(String portIndex) {
    Collection<SimulinkLine> inLinesByPort = new ArrayList<>();

    for (SimulinkLine inLine : this.getInLines()) {
      if (inLine.getDstPort().getIndex().equals(portIndex)) {
        inLinesByPort.add(inLine);
      }
    }

    return inLinesByPort;
  }

  public Collection<SimulinkLine> getInLinesByPort(SimulinkPortBase inPort) {
    return getInLinesByPortIndex(inPort.getIndex());
  }



  private Collection<Neighbour> computePredecessors() {
    // become own neighbour

    // get adjacent neighbours

    // if neighbour is atomic and computational add to list of predecessors

    // else resume the procedure

    return null;
  }

  public Collection<Neighbour> getPredecessors() {
    // if the cache is null, means that the predecessors have not been computed so far
    if (predecessors == null) {
      // populate the cache
      predecessors = computePredecessors();
    }

    return predecessors;
  }


}
