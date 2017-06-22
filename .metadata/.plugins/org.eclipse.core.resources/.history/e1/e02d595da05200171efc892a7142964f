/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.types.hierarchy;

import java.util.Collection;

import org.conqat.lib.simulink.model.SimulinkModel;
import org.fmaes.simppaal.simulinktotimedautomata.core.configuration.ApplicationConfiguration;
import org.fmaes.simppaal.simulinktotimedautomata.core.loaders.SimulinkModelLoader;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.wrappers.SimulinkBlockWrapper;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.wrappers.SimulinkModelWrapper;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
public class SimulinkModelHierarchy {

  private Collection<SimulinkModelWrapper> simulinkModelFiles;
  private SimulinkModelLoader modelLoader;
  private String parentBlockId;

  @SuppressWarnings("unused")
  private SimulinkModelHierarchy() {}

  public SimulinkModelHierarchy(ApplicationConfiguration _config, String rootModelFileLocation) {
    SimulinkModelWrapper rootModel = loadSimulinkModelFile(rootModelFileLocation);
    modelLoader = new SimulinkModelLoader(_config);
  }

  public SimulinkModelHierarchy(ApplicationConfiguration _config,
      SimulinkModelWrapper rootSimulinkModel) {
    modelLoader = new SimulinkModelLoader(_config);
    if (rootSimulinkModel.exists()) {
      simulinkModelFiles.add(rootSimulinkModel);
    }
  }

  private SimulinkModelWrapper loadSimulinkModelFile(String modelFileName) {    
    SimulinkModel sModel = modelLoader.loadSimulinkModelByName(modelFileName);
    return new SimulinkModelWrapper(sModel);
  }
  
  private SimulinkModelWrapper loadSimulinkModel(SimulinkModelWrapper _parent, SimulinkBlockWrapper _externalReferenceBlock){
    String referencedModelName = _externalReferenceBlock.getReferencedModelNameWithoutExtension();
    return null;
  }

  public SimulinkModelWrapper getRootSimulinkModel() {
    SimulinkModelWrapper rootModel = null;
    for (SimulinkModelWrapper sModel : simulinkModelFiles) {
      if (!sModel.isReferenced()) {
        rootModel = sModel;
        break;
      }
    }
    return rootModel;
  }

  public SimulinkBlockWrapper getSimulinkBlockById(String blockId) {
    return null;
  }

}
