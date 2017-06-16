/**
 * 
 */
package org.fmaes.simulinktotimedautomata.types.newwrappers;

import org.conqat.lib.simulink.model.SimulinkBlock;
import org.conqat.lib.simulink.model.SimulinkModel;

/**
 * @author pfj01
 *
 */
public class SimulinkBlockWrapper {

  private SimulinkBlock simulinkBlock;

  private SimulinkBlockWrapper referencedParent;

  public SimulinkBlockWrapper(SimulinkBlock _simulinkBlock) {
    referencedParent = null;
    simulinkBlock = _simulinkBlock;
  }

  public String getGlobalId() {
    String _blockId = simulinkBlock.getId();
    String _parentId = referencedParent != null ? referencedParent.getGlobalId() : "";
    String referencedSourceBlock = "";
    if (simulinkBlock.getModel().isLibrary()) {
      referencedSourceBlock = simulinkBlock.getParameter("SourceBlock");
      _blockId = _blockId.replaceAll(referencedSourceBlock, "");
    }
    return String.format("%s%s", _parentId, _blockId);
  }

  public SimulinkModel getSimulinkModel() {
    return simulinkBlock.getModel();
  }


}
