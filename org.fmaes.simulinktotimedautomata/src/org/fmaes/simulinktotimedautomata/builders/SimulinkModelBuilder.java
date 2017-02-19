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

  private static SimulinkModel loadSimulinkModel(String simulinkModelLocation) {
    File modelFile = new File(simulinkModelLocation);
    SimulinkModel simulinkModel = null;
    try (org.conqat.lib.simulink.builder.SimulinkModelBuilder builder =
        new org.conqat.lib.simulink.builder.SimulinkModelBuilder(modelFile, new SimpleLogger())) {
      simulinkModel = builder.buildModel();
    } catch (Exception ex) {
      simulinkModel = null;
    }
    return simulinkModel;
  }

  public static SimulinkModelWrapper loadWrappedSimulinkModel(String simulinkModelLocation) {
    SimulinkModel sModel = loadSimulinkModel(simulinkModelLocation);
    SimulinkModelWrapper wrappedSimulinkModel = new SimulinkModelWrapper(sModel);
    return wrappedSimulinkModel;
  }

  public static SimulinkModelWrapper loadWrappedSimulinkModelByName(String simulinkModelName) {
    
    ApplicationConfiguration appConfig = ApplicationConfiguration.loadConfiguration();
    String modelsDirectory = appConfig.getProperty("modelDirectory");
    if (!simulinkModelName.endsWith(".mdl")) {
      simulinkModelName = String.format("%s.mdl", simulinkModelName);
    }
    Path pathToModel = Paths.get(modelsDirectory, simulinkModelName);
    return loadWrappedSimulinkModel(pathToModel.toString());
  }

  public static SimulinkModelWrapper buildReferencedSimulinkModel(
      SimulinkBlockWrapper referencedSubSystemBlock) {
    
    String childLibraryNameNoExtension = referencedSubSystemBlock.getLibraryName();
    String childLibraryFileName = String.format("%s.mdl", childLibraryNameNoExtension);
    SimulinkModelWrapper parentModel = referencedSubSystemBlock.getSimulinkModelWrapped();
    SimulinkModelWrapper referencedModel = loadWrappedSimulinkModelByName(childLibraryFileName);
    if (!referencedModel.exists()) {
      return referencedModel;
    }
    SimulinkBlockWrapper referencedModelRoot = referencedModel.getRootSubsystem();

    SerializableHashTable inheritanceRegistryFromParent = parentModel.getInheritanceRegistry();
    SerializableHashTable triggeringRegistryFromParent = parentModel.getTriggeringRegistry();
    String inheritanceRegistryKey = String.format("%s#%s#",
        referencedModelRoot.getIdInLocalContext(), referencedSubSystemBlock.getIdInGlobalContext());
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
