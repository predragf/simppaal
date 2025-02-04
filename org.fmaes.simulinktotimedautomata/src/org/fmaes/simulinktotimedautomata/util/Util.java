package org.fmaes.simulinktotimedautomata.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.conqat.lib.commons.logging.SimpleLogger;
import org.conqat.lib.simulink.builder.SimulinkModelBuilder;
import org.conqat.lib.simulink.model.SimulinkModel;
import org.fmaes.simulinktotimedautomata.configuration.ApplicationConfiguration;
import org.fmaes.simulinktotimedautomata.types.enums.SimulinkParametersEnum;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkModelWrapper;

public class Util {

  public static SimulinkModel loadSimulinkModel(String simulinkModelLocation) {
    File modelFile = new File(simulinkModelLocation);
    SimulinkModel model = null;
    try (SimulinkModelBuilder builder = new SimulinkModelBuilder(modelFile, new SimpleLogger())) {
      model = builder.buildModel();
    } catch (Exception ex) {
      model = null;
    }
    return model;
  }

  public static SimulinkModelWrapper loadAndWrapSimulinkModel(String simulinkModelLocation) {
    SimulinkModel sModel = loadSimulinkModel(simulinkModelLocation);
    SimulinkModelWrapper wrappedSimulinkModel = new SimulinkModelWrapper(sModel);
    return wrappedSimulinkModel;
  }

  /*
   * public static SimulinkModelWrapper loadAndWrapReferencedModel( SimulinkBlockWrapper
   * referencedSubSystemBlock) { SimulinkModelWrapper blockParentModel =
   * referencedSubSystemBlock.getModelWrapped(); String modelLocationOnDisk = "";//
   * blockParentModel.getModelLocationOnDisk(); String libraryName = String.format("%s.mdl",
   * referencedSubSystemBlock.getLibraryName()); Path pathToFile = Paths.get(modelLocationOnDisk,
   * libraryName); String pathToFileString = pathToFile.toString(); String currentModel =
   * referencedSubSystemBlock.getModelName(); String rootModelName =
   * blockParentModel.getParameter(SimulinkParametersEnum.ROOT_MODEL.toString());
   * SerializableHashTable inheritanceTree; SimulinkModelWrapper referencedModel =
   * loadAndWrapSimulinkModel(pathToFileString); if (referencedModel != null) {
   * referencedModel.setParameter(SimulinkParametersEnum.IS_REFERENCED.toString(), "true");
   * referencedModel.setParameter(SimulinkParametersEnum.GLOBALID.toString(),
   * referencedSubSystemBlock.getIdInGlobalContext());
   * referencedModel.setParameter(SimulinkParametersEnum.PARENT_MODEL.toString(), currentModel);
   * referencedModel.setParameter(SimulinkParametersEnum.ROOT_MODEL.toString(), rootModelName);
   * String sampleTime = referencedSubSystemBlock.getSampleTime(); if
   * (!Util.stringNullOrEmpty(sampleTime)) { String globalTriggeringInfoSerialized =
   * blockParentModel.getParameter(SimulinkParametersEnum.TRIGGERING_INFO.toString()); if
   * (Util.stringNullOrEmpty(globalTriggeringInfoSerialized)) { globalTriggeringInfoSerialized = "";
   * } SerializableHashTable triggeringInfoTable =
   * SerializableHashTable.deserialize(globalTriggeringInfoSerialized);
   * triggeringInfoTable.put(referencedSubSystemBlock.getIdInGlobalContext(), sampleTime);
   * referencedModel.setParameter(SimulinkParametersEnum.TRIGGERING_INFO.toString(),
   * triggeringInfoTable.serialize()); } if
   * (blockParentModel.getSimulinkModelName().toLowerCase().equals(rootModelName)) { inheritanceTree
   * = new SerializableHashTable();
   * inheritanceTree.put(referencedSubSystemBlock.getIdInGlobalContext(), "");
   * referencedModel.setParameter(SimulinkParametersEnum.INHERITANCE_TREE.toString(),
   * inheritanceTree.serialize()); } else { String inheritanceTreeString =
   * blockParentModel.getParameter(SimulinkParametersEnum.INHERITANCE_TREE.toString());
   * inheritanceTree = SerializableHashTable.deserialize(inheritanceTreeString);
   * SimulinkBlockWrapper rootSubSystemBlock = referencedModel.getRootSubsystem();
   * inheritanceTree.put(rootSubSystemBlock.getIdInGlobalContext(),
   * referencedSubSystemBlock.getIdInGlobalContext()); } } return referencedModel; }
   */
  public static SimulinkModel loadReferencedModel(SimulinkBlockWrapper referencedSubSystemBlock) {
    SimulinkModelWrapper wrappedModel =
        org.fmaes.simulinktotimedautomata.builders.SimulinkModelBuilder
            .buildReferencedSimulinkModel(referencedSubSystemBlock);
    return wrappedModel.getSimulinkModel();
  }

  public static SimulinkModelWrapper loadParentModel(SimulinkBlockWrapper referencedSubSystem) {
    SimulinkModelWrapper currentContext = referencedSubSystem.getSimulinkModelWrapped();
    String inhritanceTreeString =
        currentContext.getParameter(SimulinkParametersEnum.INHERITANCE_TREE.toString());
    SerializableHashTable inheritanceTree = SerializableHashTable.deserialize(inhritanceTreeString);
    String parentId = inheritanceTree.get(referencedSubSystem.getIdInGlobalContext());

    if (stringNullOrEmpty(parentId)) {

    }

    return null;
  }

  public static String buildGlobalId(String globalModelId, String blockName) {
    String globalBlockId = String.format("%s/%s", globalModelId, blockName);
    return globalBlockId;
  }

  public static String buildLocalId(String globalBlockId, String localModelId) {
    /*
     * local model id shall contain the id of the model followed by the id of the subsystem block in
     * which the contents reside. localModelId ex: lib/sybsystem/
     */
    String localBlockId = "";
    String globalBlockIdFromSubsystem = "";
    String subsystemId = "";
    String[] localIdParts = localModelId.split("/");
    /* There should be at least 3 parts model/subsystem/block */
    if (localIdParts.length >= 2) {
      subsystemId = localIdParts[1];
    } else {
      /* log that the input was incorrect */
      subsystemId = localModelId;
    }
    /*
     * Structure of localIdParts localIdParts[0] = library name localIdParts[1] = subsystem name
     */
    int subsystemContainerIdStart = globalBlockId.indexOf(subsystemId);
    if (subsystemContainerIdStart >= 0) {
      globalBlockIdFromSubsystem = globalBlockId.substring(subsystemContainerIdStart);
      String blockId = globalBlockIdFromSubsystem.replace(subsystemId, "");
      localBlockId = String.format("%s%s", localModelId, blockId);
    } else {
      /*
       * This is if the subsystem id was not found in the globalBlock id, which shall never be the
       * case
       * 
       * log this (logger functionality to be implemented in the next version)
       */
    }
    return localBlockId;
  }

  public static Boolean matchStringsIgnoreCase(String first, String second) {
    String firstLowerCase = first != null ? first.toLowerCase() : "";
    String secondLowerCase = second != null ? second.toLowerCase() : "";
    return firstLowerCase.equals(secondLowerCase);
  }

  public static Boolean stringNullOrEmpty(String input) {
    return (input == null || input == "");
  }

  public static String convertToPortIndex(String inputBlockPort) {
    String defaultPortIndex = "1";
    return !stringNullOrEmpty(inputBlockPort) ? inputBlockPort : defaultPortIndex;
  }

  public static String readFileContents(Path filepath) {
    String blockRoutineString = "";
    byte[] routineBytes;
    try {
      routineBytes = Files.readAllBytes(filepath);
      blockRoutineString = new String(routineBytes);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return blockRoutineString;
  }

  public static SimulinkModelWrapper loadAndWrapReferencedModel2(
      SimulinkBlockWrapper referencedSubSystemBlock) {
    String libraryFileName = String.format("%s.mdl", referencedSubSystemBlock.getLibraryName());
    ApplicationConfiguration appConfig = ApplicationConfiguration.loadConfiguration();
    String modelsDirectory = appConfig.getProperty("modelDirectory");
    Path pathToModel = Paths.get(modelsDirectory, libraryFileName);
    SimulinkModelWrapper referencedSubSystemModel =
        loadAndWrapSimulinkModel(pathToModel.toString());
    SimulinkModelWrapper referencedSubSystemBlockParentModel =
        referencedSubSystemBlock.getSimulinkModelWrapped();
    SerializableHashTable inheritanceTree =
        referencedSubSystemBlockParentModel.getInheritanceRegistry();
    inheritanceTree.put(referencedSubSystemBlock.getIdInLocalContext(),
        referencedSubSystemModel.getRootSubsystem().getIdInLocalContext());
    referencedSubSystemModel.setInheritanceRegistry(inheritanceTree);
    return referencedSubSystemModel;
  }

  public static SimulinkModelWrapper loadAndWrapSimulinkModel2(String simulinkModelLocation) {
    SimulinkModel sModel = loadSimulinkModel(simulinkModelLocation);
    /*
     * model.setParameter(SimulinkParametersEnum.LOCATION_ON_DISK.toString(), locationOnDisk);
     * model.setParameter(SimulinkParametersEnum.ROOT_MODEL.toString(), model.getName());
     */
    SimulinkModelWrapper wrappedSimulinkModel = new SimulinkModelWrapper(sModel);
    return wrappedSimulinkModel;

  }

  public static String extractGlobalIdFromRegistryEntry(String entry) {
    String globalId = "";
    if (stringNullOrEmpty(entry)) {
      return globalId;
    }
    Pattern ENTRY_PATTERN = Pattern.compile("#(.*)#");
    Matcher _matcher = ENTRY_PATTERN.matcher(entry);
    Collection<String> matches = new ArrayList<String>();
    while (_matcher.find()) {
      globalId = _matcher.group(1);
    }
    return globalId;
  }

  public static String extractLocalIdFromRegistryEntry(String entry) {
    if (stringNullOrEmpty(entry)) {
      return "";
    }
    String globalId = extractGlobalIdFromRegistryEntry(entry);
    return entry.replace(String.format("#%s#", globalId), "");
  }

  public static Boolean isDouble(String input) {
    Pattern doublePattern = Pattern.compile("\\d+\\.\\d+");
    return input != null && Pattern.matches(doublePattern.pattern(), input);
  }

  public static Boolean isInteger(String input) {
    Pattern integerPattern = Pattern.compile("\\d+");
    return input != null && Pattern.matches(integerPattern.pattern(), input);
  }

  public static Boolean isNumber(String input) {
    return input != null && (isInteger(input) || isDouble(input));
  }

  public static Boolean isBoolean(String input) {
    input = input.toLowerCase();
    return input != null && (input.equals("true") || input.equals("false"));
  }

  public static void saveFileAsTxt(String fileLocation, String fileContents) {
    if (!fileLocation.endsWith(".txt")) {
      fileLocation = fileLocation += ".txt";
    }
    try (PrintStream out = new PrintStream(new FileOutputStream(fileLocation))) {
      out.print(fileContents);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      System.out.println(e.getMessage());
    }
  }
}
