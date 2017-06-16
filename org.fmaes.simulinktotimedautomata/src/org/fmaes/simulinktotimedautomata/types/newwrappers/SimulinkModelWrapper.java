/**
 * 
 */
package org.fmaes.simulinktotimedautomata.types.newwrappers;

import java.util.ArrayList;
import java.util.Collection;

import org.conqat.lib.simulink.model.SimulinkBlock;
import org.conqat.lib.simulink.model.SimulinkModel;
import org.fmaes.simulinktotimedautomata.types.enums.SimulinkBlockTypesEnum;

/**
 * @author pfj01
 *
 */
public class SimulinkModelWrapper {

  private SimulinkModel simulinkModel;

  private Collection<SimulinkModel> referencedModels;

  public SimulinkBlockWrapper getBlockById(String globalBlockId) {
    SimulinkBlock resultBlock = simulinkModel.getBlock(globalBlockId);

    /**
     * The block was not found in this .mdl file
     */
    if (resultBlock == null) {

    }

    return null;
  }

  public void loadReferencedModels() {

  }

  private String moveLevelUp(String globalId) {

    /**
     * if there is only one slash, we cannot move level up
     */
    if ((globalId.length() - globalId.replace("/", "").length()) < 2) {
      return globalId;
    }
    int lastSlash = globalId.lastIndexOf("/");
    return globalId.substring(0, lastSlash);
  }

  public Collection<SimulinkBlockWrapper> getBlocksByType() {
    Collection<SimulinkBlockWrapper> matchingBlocks = new ArrayList<SimulinkBlockWrapper>();

    for (SimulinkBlock subblock : simulinkModel.getSubBlocks()) {
      if (subblock.getType().equals(SimulinkBlockTypesEnum.REFERENCE)
          && isExternal(subblock.getParameter("SourceBlock"))) {
        matchingBlocks.add(new SimulinkBlockWrapper(subblock));
      }
    }

    return matchingBlocks;

  }

  private boolean isExternal(String referenceLocation) {
    return referenceLocation.startsWith("simulink/");
  }

}
