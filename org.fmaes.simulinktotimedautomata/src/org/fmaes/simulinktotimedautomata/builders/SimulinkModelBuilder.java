package org.fmaes.simulinktotimedautomata.builders;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.conqat.lib.commons.logging.SimpleLogger;
import org.conqat.lib.simulink.model.SimulinkModel;
import org.fmaes.simulinktotimedautomata.configuration.ApplicationConfiguration;
import org.fmaes.simulinktotimedautomata.types.enums.SimulinkParametersEnum;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkModelWrapper;
import org.fmaes.simulinktotimedautomata.util.SerializableHashTable;
import org.fmaes.simulinktotimedautomata.util.Util;

public class SimulinkModelBuilder {

  /**
   * This function opens a Simulink model from disk and creates SimulinkModel object, which is then
   * returned by the function. If the model cannot be loaded from the disk, the function will return
   * null object.
   * 
   * @param simulinkModelLocation
   * @return
   */
  private static SimulinkModel loadSimulinkModel(String simulinkModelLocation) {
    File modelFile = new File(simulinkModelLocation);
    SimulinkModel simulinkModel = null;
    try (org.conqat.lib.simulink.builder.SimulinkModelBuilder builder =
        new org.conqat.lib.simulink.builder.SimulinkModelBuilder(modelFile, new SimpleLogger())) {
      simulinkModel = builder.buildModel();
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      simulinkModel = null;
    }
    return simulinkModel;
  }

  /**
   * This is a public function that retrns SimulinkModelWrapper object representing 
   * a Simulink model given as a full path to the disk (location).
   * To realize its functionality, this function uses loadSimulinkModel private method.
   * @param simulinkModelLocation
   * @return
   */
  public static SimulinkModelWrapper loadWrappedSimulinkModel(String simulinkModelLocation) {
    SimulinkModel sModel = loadSimulinkModel(simulinkModelLocation);
    SimulinkModelWrapper wrappedSimulinkModel = new SimulinkModelWrapper(sModel);
    return wrappedSimulinkModel;
  }

  /**
   * This function loads Simulink model into SimulinkModel provided only the name of the Simulink model.
   * This function assumes that all Simulink models reside in the same folder.
   * @param simulinkModelName
   * @return
   */
  public static SimulinkModelWrapper loadWrappedSimulinkModelByName(String simulinkModelName) {
    ApplicationConfiguration appConfig = ApplicationConfiguration.loadConfiguration();
    String modelsDirectory = appConfig.getProperty("modelDirectory");
    if (!simulinkModelName.endsWith(".mdl")) {
      simulinkModelName = String.format("%s.mdl", simulinkModelName);
    }
    Path pathToModel = Paths.get(modelsDirectory, simulinkModelName);
    return loadWrappedSimulinkModel(pathToModel.toString());
  }

  /**
   * This function loads referenced Simulink model as a SimulinkModel object.
   * As an argument, the function accepts the subsystem SimulinkBlock which reffers to the
   * Simulink model. This function works for both regular referenced Simulink models and 
   * referenced simulink libraries
   * @param referencedSubSystemBlock
   * @return
   */
  public static SimulinkModelWrapper buildReferencedSimulinkModel(
      SimulinkBlockWrapper referencedSubSystemBlock) {

    String childLibraryNameNoExtension = referencedSubSystemBlock.getReferencedModelName();
    String childLibraryFileName = String.format("%s.mdl", childLibraryNameNoExtension);
    SimulinkModelWrapper parentModel = referencedSubSystemBlock.getSimulinkModelWrapped();
    SimulinkModelWrapper referencedModel = loadWrappedSimulinkModelByName(childLibraryFileName);
    if (!referencedModel.exists()) {
      return referencedModel;
    }
    /*
     * this line is important if the referenced model is a library because library must be wrapped
     * in a subsystem
     */
    SimulinkBlockWrapper referencedModelRoot = null;
    if (referencedSubSystemBlock.isLibrary()) {
      referencedModelRoot = referencedModel.getRootSubsystem();
    }

    SerializableHashTable inheritanceRegistryFromParent = parentModel.getInheritanceRegistry();
    SerializableHashTable triggeringRegistryFromParent = parentModel.getTriggeringRegistry();
    // if it is library we put the root subsystem name in the registry, otherwise
    // we put the name of the Simulink model
    String registryKeyName = referencedModelRoot != null ? referencedModelRoot.getIdInLocalContext()
        : referencedModel.getSimulinkModelName();
    String inheritanceRegistryKey =
        String.format("%s#%s#", registryKeyName, referencedSubSystemBlock.getIdInGlobalContext());
    String inheritanceRegistryValue =
        String.format("%s#%s#", referencedSubSystemBlock.getIdInLocalContext(),
            referencedSubSystemBlock.getIdInGlobalContext());
    inheritanceRegistryFromParent.put(inheritanceRegistryKey, inheritanceRegistryValue);
    String sampleTime = referencedSubSystemBlock.getSampleTime();
    if (!Util.stringNullOrEmpty(sampleTime)) {
      String triggeringRegistryKey = inheritanceRegistryKey;
      triggeringRegistryFromParent.put(triggeringRegistryKey, sampleTime);
    }
    referencedModel.setTriggeringRegistry(triggeringRegistryFromParent);
    referencedModel.setInheritanceRegistry(inheritanceRegistryFromParent);
    referencedModel.setRegistryEntryId(inheritanceRegistryKey);

    return referencedModel;
  }

  public static SimulinkModelWrapper buildParentSimulinkModel(
      SimulinkBlockWrapper referencedModelRoot) {
    SimulinkModelWrapper currentModel = referencedModelRoot.getSimulinkModelWrapped();
    SerializableHashTable inheritanceRegistry = currentModel.getInheritanceRegistry();
    SerializableHashTable trigeringRegistry = currentModel.getTriggeringRegistry();
    String inheritanceRegistryKey = String.format("%s#%s#",
        referencedModelRoot.getIdInLocalContext(), referencedModelRoot.getIdInGlobalContext());
    String parentModelEntry = inheritanceRegistry.get(inheritanceRegistryKey);
    String parentLocalId = Util.extractLocalIdFromRegistryEntry(parentModelEntry);
    String parentModelName = parentLocalId.split("/")[0];
    SimulinkModelWrapper parentModel = loadWrappedSimulinkModelByName(parentModelName);
    parentModel.setInheritanceRegistry(inheritanceRegistry);
    parentModel.setTriggeringRegistry(trigeringRegistry);
    parentModel.setRegistryEntryId(parentModelEntry);
    return parentModel;
  }

  public static SimulinkModelWrapper buildSimulinkModelFromFullPath(String modelLocation,
      SerializableHashTable inheritanceTree, SerializableHashTable triggeringRegistry) {
    SimulinkModel baseModel = loadSimulinkModel(modelLocation);
    if (baseModel != null) {
      baseModel.setParameter(SimulinkParametersEnum.INHERITANCE_TREE.toString(),
          inheritanceTree.serialize());
      baseModel.setParameter(SimulinkParametersEnum.TRIGGERING_INFO.toString(),
          triggeringRegistry.serialize());
    }
    return new SimulinkModelWrapper(baseModel);

  }
}
