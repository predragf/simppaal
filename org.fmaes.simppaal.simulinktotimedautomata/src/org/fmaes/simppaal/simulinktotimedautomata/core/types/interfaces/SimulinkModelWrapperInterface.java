/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.types.interfaces;

import java.util.Collection;

import org.conqat.lib.simulink.model.SimulinkBlock;
import org.conqat.lib.simulink.model.SimulinkModel;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.SimulinkBlockWrapper;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
public interface SimulinkModelWrapperInterface {

  SimulinkBlockWrapper getBlockById(String blockId);

  SimulinkModel getSimulinkModel();

  boolean exists();

  void addParameter(String parameterName, String parameterValue);

  String getParameter(String parameterName);

  String getModelName();

  Collection<SimulinkBlock> getSubBlocksRecursively();

}
