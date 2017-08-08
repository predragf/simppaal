/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.types;

import java.util.ArrayList;
import java.util.Collection;

import org.conqat.lib.simulink.model.SimulinkBlock;
import org.conqat.lib.simulink.model.SimulinkModel;
import org.conqat.lib.simulink.model.SimulinkPortBase;
import org.fmaes.simppaal.simulinktotimedautomata.core.enums.SimulinkBlockTypesEnum;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.interfaces.SimulinkModelWrapperInterface;
import org.fmaes.simppaal.simulinktotimedautomata.utils.SimulinkUtils;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
public class SimulinkModelWrapper implements SimulinkModelWrapperInterface {

  private SimulinkModel simulinkModel;

  @SuppressWarnings("unused")
  private SimulinkModelWrapper() {}

  public SimulinkModelWrapper(SimulinkModel _simulinkModel) {
    simulinkModel = _simulinkModel;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.fmaes.simppaal.simulinktotimedautomata.core.types.interfaces.SimulinkModelWrapperInterface#
   * getBlockById(java.lang.String)
   */
  @Override
  public SimulinkBlockWrapper getBlockById(String blockId) {
    SimulinkBlock matchingSimulinkBlock = simulinkModel.getBlock(blockId);
    return new SimulinkBlockWrapper(matchingSimulinkBlock);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.fmaes.simppaal.simulinktotimedautomata.core.types.interfaces.SimulinkModelWrapperInterface#
   * getSimulinkModel()
   */
  @Override
  public SimulinkModel getSimulinkModel() {
    return simulinkModel;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.fmaes.simppaal.simulinktotimedautomata.core.types.interfaces.SimulinkModelWrapperInterface#
   * exists()
   */
  @Override
  public boolean exists() {
    return simulinkModel != null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.fmaes.simppaal.simulinktotimedautomata.core.types.interfaces.SimulinkModelWrapperInterface#
   * addParameter(java.lang.String, java.lang.String)
   */
  @Override
  public void addParameter(String parameterName, String parameterValue) {
    simulinkModel.setParameter(parameterName, parameterValue);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.fmaes.simppaal.simulinktotimedautomata.core.types.interfaces.SimulinkModelWrapperInterface#
   * getParameter(java.lang.String)
   */
  @Override
  public String getParameter(String parameterName) {
    return simulinkModel.getParameter(parameterName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.fmaes.simppaal.simulinktotimedautomata.core.types.interfaces.SimulinkModelWrapperInterface#
   * getModelName()
   */
  @Override
  public String getModelName() {
    return simulinkModel.getName();
  }

  private Collection<SimulinkBlock> extractSubBlocks(SimulinkBlock subSystem) {
    Collection<SimulinkBlock> internalBlocks = new ArrayList<>();
    for (SimulinkBlock simulinkBlock : subSystem.getSubBlocks()) {
      internalBlocks.add(simulinkBlock);
      if (SimulinkUtils.compareStringsIgnoreCase(SimulinkBlockTypesEnum.SUBSYSTEM.toString(),
          simulinkBlock.getType())) {
        internalBlocks.addAll(extractSubBlocks(simulinkBlock));
      }
    }
    return internalBlocks;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.fmaes.simppaal.simulinktotimedautomata.core.types.interfaces.SimulinkModelWrapperInterface#
   * getSubBlocksRecursively()
   */
  @Override
  public Collection<SimulinkBlock> getSubBlocksRecursively() {
    Collection<SimulinkBlock> allBlocks = new ArrayList<>();
    for (SimulinkBlock simulinkBlock : simulinkModel.getSubBlocks()) {
      allBlocks.add(simulinkBlock);
      if (SimulinkUtils.compareStringsIgnoreCase(SimulinkBlockTypesEnum.SUBSYSTEM.toString(),
          simulinkBlock.getType())) {
        allBlocks.addAll(extractSubBlocks(simulinkBlock));
      }
    }
    return allBlocks;
  }

  public Collection<SimulinkBlockWrapper> getSubBlocksRecursivelyByType(String _type) {
    Collection<SimulinkBlockWrapper> result = new ArrayList<>();
    String blkType;

    for (SimulinkBlock subBlock : getSubBlocksRecursively()) {
      blkType = subBlock.getType();
      if (SimulinkUtils.compareStringsIgnoreCase(_type, blkType)) {
        result.add(new SimulinkBlockWrapper(subBlock));
      }
    }

    return result;
  }
}
