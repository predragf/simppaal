/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.types.wrappers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.fmaes.simppaal.simulinktotimedautomata.core.configuration.ApplicationConfiguration;
import org.fmaes.simppaal.simulinktotimedautomata.core.loaders.SimulinkModelLoader;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.hierarchy.HierarchyNode;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.hierarchy.HierarchyTree;;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
public class SimulinkSystemModel {

  public Map<String, SimulinkModelWrapper> simulinkModelFiles;
  public HierarchyTree modelHierarcyTree;
  public SimulinkModelLoader simulinkModelLoader;

  public SimulinkSystemModel(ApplicationConfiguration _appConfig, String rootModelName) {
    simulinkModelLoader = new SimulinkModelLoader(_appConfig);
    simulinkModelFiles = new HashMap<String, SimulinkModelWrapper>();
    modelHierarcyTree = new HierarchyTree();
    loadHierarchy(rootModelName);
  }

  private void loadHierarchy(String rootModelName) {
    SimulinkModelWrapper wrappedRootModel = loadSimulinkModel(rootModelName);
    addEntryToModelsList(wrappedRootModel);
    HierarchyNode hNode = new HierarchyNode(rootModelName);
    addToRegisters(hNode, wrappedRootModel);
    loadReferencedModels(hNode, wrappedRootModel);
  }

  private void loadReferencedModel(HierarchyNode parentNode,
      SimulinkBlockWrapper referencedSimulinkBlock) {
    String modelName = referencedSimulinkBlock.getReferencedModelNameWithoutExtension();
    SimulinkModelWrapper wrappedSimulinkModel = loadSimulinkModel(modelName);
    if (wrappedSimulinkModel.exists()) {
      HierarchyNode hNode = new HierarchyNode(referencedSimulinkBlock.getId(), modelName,
          wrappedSimulinkModel.getModelType(), parentNode);
      addToRegisters(hNode, wrappedSimulinkModel);
      loadReferencedModels(hNode, wrappedSimulinkModel);
    }
  }

  private void loadReferencedModels(HierarchyNode parentNode,
      SimulinkModelWrapper wrappedSimulinkModel) {
    Collection<SimulinkBlockWrapper> externalReferenceBlocks =
        wrappedSimulinkModel.getExternalReferencedModelBlocks();
    for (SimulinkBlockWrapper externalReferenceBlock : externalReferenceBlocks) {
      loadReferencedModel(parentNode, externalReferenceBlock);
    }
  }

  private void addEntryToHierarchy(HierarchyNode _node) {
    modelHierarcyTree.add(_node);
  }

  private void addEntryToModelsList(SimulinkModelWrapper _wrappedSimulinkModel) {
    String modelName = _wrappedSimulinkModel.getModelName();
    // add it only if such entry does not existst in the register
    if (simulinkModelFiles.get(modelName) == null) {
      simulinkModelFiles.put(modelName, _wrappedSimulinkModel);
    }
  }

  private SimulinkModelWrapper loadSimulinkModel(String modelName) {
    // First check in the preloaded files
    SimulinkModelWrapper wrappedModel = simulinkModelFiles.get(modelName);
    // if it does not exist in the pre-loaded files, open it from the disk
    if (wrappedModel == null) {
      wrappedModel = simulinkModelLoader.loadAndWrapSimulinkModelByName(modelName);
    }
    return wrappedModel;
  }

  void addToRegisters(HierarchyNode _hNode, SimulinkModelWrapper wrappedRootModel) {
    addEntryToHierarchy(_hNode);
    addEntryToModelsList(wrappedRootModel);
  }

}
