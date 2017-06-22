/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.types.wrappers;

import java.io.File;
import java.util.Collection;

import org.conqat.lib.simulink.model.ReferencedBlockInfo;
import org.conqat.lib.simulink.model.SimulinkBlock;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.hierarchy.ReferencedModelTypeEnum;
import org.fmaes.simppaal.simulinktotimedautomata.utils.DiskUtils;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
public class SimulinkBlockWrapper {

  private SimulinkBlock simulinkBlock;

  /**
   * @param simulinkBlock
   */
  public SimulinkBlockWrapper(SimulinkBlock _simulinkBlock) {
    simulinkBlock = _simulinkBlock;
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

  public boolean isExternalModelReference() {
    // I have noticed that all internal references start with "simulink/"
    // The best way would be to check whether the referenced block in the blockreference is in the
    // folder of referenced models
    String modelNameGivenAsReference = getReferencedModelNameWithoutExtension();
    return !modelNameGivenAsReference.toLowerCase().startsWith("simulink");
  }

  public String getReferencedModelNameWithoutExtension() {
    String referencedModelName = simulinkBlock.getParameter("SourceBlock");
    int firstSlashIndex = referencedModelName.indexOf('/');
    // The model name shall be at least 1 character long
    if (firstSlashIndex < 1) {
      // if no "/" was found, then take the whole name as the model name
      firstSlashIndex = referencedModelName.length() - 1;
    }
    return referencedModelName.substring(0, firstSlashIndex);
  }

  public String getId() {
    return simulinkBlock.getId();
  }
}
