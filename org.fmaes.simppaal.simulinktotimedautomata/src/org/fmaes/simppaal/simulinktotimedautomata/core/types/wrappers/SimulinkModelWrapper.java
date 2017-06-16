/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.types.wrappers;

import java.util.ArrayList;
import java.util.Collection;

import org.conqat.lib.simulink.model.SimulinkBlock;
import org.conqat.lib.simulink.model.SimulinkModel;
import org.fmaes.simppaal.simulinktotimedautomata.core.enums.ParameterNamesEnum;
import org.fmaes.simppaal.simulinktotimedautomata.core.enums.SimulinkBlockTypesEnum;
import org.fmaes.simppaal.simulinktotimedautomata.utils.DiskUtils;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
public class SimulinkModelWrapper {

  private SimulinkModel simulinkModel;

  private SimulinkModelWrapper parentSimulinkModel;

  private Collection<SimulinkModelWrapper> referencedModels;

  @SuppressWarnings("unused")
  private SimulinkModelWrapper() {}

  /*
   * This is usually the case for the referenced models as they have parents
   */
  /**
   * This constructor shall be used for referenced blocks which have parent
   * 
   * @param _simulinkModel
   * @param _parentSimulinkModel
   */
  public SimulinkModelWrapper(SimulinkModel _simulinkModel,
      SimulinkModelWrapper _parentSimulinkModel) {
    simulinkModel = _simulinkModel;
    parentSimulinkModel = _parentSimulinkModel;
  }

  /**
   * This constructor shall be used only for the root simulink model that does not have parents
   * 
   * @param _simulinkModel
   */
  public SimulinkModelWrapper(SimulinkModel _simulinkModel) {
    this(_simulinkModel, null);
  }

  public SimulinkBlockWrapper getBlockById(String blockId) {
    SimulinkBlock matchingSimulinkBlock = simulinkModel.getBlock(blockId);
    return new SimulinkBlockWrapper(matchingSimulinkBlock);
  }

  public SimulinkModel getSimulinkModel() {
    return simulinkModel;
  }

  public Collection<SimulinkBlockWrapper> getRereferecedModelBlocks() {
    Collection<SimulinkBlockWrapper> referencedModelBlocks = new ArrayList<SimulinkBlockWrapper>();
    for (SimulinkBlock simulinkBlock : simulinkModel.getSubBlocks()) {
      if (simulinkBlock.getType().equals(SimulinkBlockTypesEnum.REFERENCE)) {
        SimulinkBlockWrapper wrappedBlock = new SimulinkBlockWrapper(simulinkBlock);
        referencedModelBlocks.add(wrappedBlock);
      }
    }
    return referencedModelBlocks;
  }

  public Collection<SimulinkBlockWrapper> getExternalReferencedModelBlocks() {
    Collection<SimulinkBlockWrapper> externalReferencedModelBlocks =
        new ArrayList<SimulinkBlockWrapper>();
    Collection<SimulinkBlockWrapper> referencedModelBlocks = getRereferecedModelBlocks();//
    for (SimulinkBlockWrapper referencedModelBlock : referencedModelBlocks) {
      if (referencedModelBlock.isExternalModelReference()) {
        externalReferencedModelBlocks.add(referencedModelBlock);
      }
    }
    return externalReferencedModelBlocks;
  }

  public boolean exists() {
    return simulinkModel != null;
  }

  public void addParameter(String parameterName, String parameterValue) {
    simulinkModel.setParameter(parameterName, parameterValue);
  }

  public String getParameter(String parameterName) {
    return simulinkModel.getParameter(parameterName);
  }

  public boolean isReferenced() {
    return parentSimulinkModel != null && parentSimulinkModel.exists();
  }
}
