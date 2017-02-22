package org.fmaes.simulinktotimedautomata.transformers;

import java.util.List;

import org.fmaes.j2uppaal.builders.UppaalDocumentBuilder;
import org.fmaes.j2uppaal.datastructures.uppaalstructures.UppaalAutomaton;
import org.fmaes.j2uppaal.datastructures.uppaalstructures.UppaalDocument;
import org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface;
import org.fmaes.simulinktotimedautomata.platformextender.BlockRoutineGeneratorPluginManager;
import org.fmaes.simulinktotimedautomata.types.wrappers.Neighbour;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;
import org.fmaes.simulinktotimedautomata.util.Util;

public class SimulinkAtomicBlockTransformer {

  private static final String discreteAutomatonTemplateLocation =
      "./templates/automata/templates.xml";
  private static final String continuousAutomatonTemplateLocation =
      "./templates/automata/templates.xml";

  private static UppaalAutomaton loadUppaalAutomatonTemplate(String templateDocumentLocation,
      String automatonName) {
    UppaalDocument templateUppaalDocument =
        UppaalDocumentBuilder.buildUppaalDocument(templateDocumentLocation);
    UppaalAutomaton templateAutomaton =
        (UppaalAutomaton) templateUppaalDocument.getAutomatonByName(automatonName);
    return templateAutomaton;
  }

  private static UppaalAutomaton loadContinuousTemplate() {
    String continiousAutomataTemplateName = "continuous";
    UppaalAutomaton continuousAutomatonTemplate = loadUppaalAutomatonTemplate(
        continuousAutomatonTemplateLocation, continiousAutomataTemplateName);
    return continuousAutomatonTemplate;
  }

  private static UppaalAutomaton loadDiscreteTemplate() {
    String discreteAutomataTemplateName = "discrete";
    UppaalAutomaton continuousAutomatonTemplate = loadUppaalAutomatonTemplate(
        discreteAutomatonTemplateLocation, discreteAutomataTemplateName);
    return continuousAutomatonTemplate;
  }

  private static void mapSimulinkBlockToUppaalAutomaton(SimulinkBlockWrapper simulinkBlock,
      UppaalAutomaton automaton) {
    String ts = simulinkBlock.getSampleTime();
    String _declaration = automaton.getDeclaration();
    if (!Util.stringNullOrEmpty(ts)) {
      _declaration = _declaration.replace("#ts#", ts);
    }
    String blockRoutine = generateBlockRoutine(simulinkBlock);
    String customInitRoutine = generateInitRoutine(simulinkBlock);

    String offset = simulinkBlock.getDeclaredParameter("offset");
    if (Util.stringNullOrEmpty(offset)) {
      offset = "0";
    }
    _declaration =
        _declaration.replace("#sn#", String.format("%d", simulinkBlock.executionOrderNumber));
    /* make this to be in the configuration */
    _declaration = _declaration.replace("#IAT#", "0.0001");
    _declaration = _declaration.replace("#OFFSET#", offset);
    _declaration = _declaration.replace("#customInit#", customInitRoutine);
    _declaration = _declaration.replace("#blockRoutine#", blockRoutine);
    automaton.setDeclaration(_declaration);
    automaton.setName(simulinkBlock.getNameForSTA());
    automaton.setSignalName(simulinkBlock.getSignalName());
    automaton.setInstanceName(simulinkBlock.getSTAInstanceName());
    automaton.setSignalDeclaration(simulinkBlock.getSignalDeclaration());
  }

  public static UppaalAutomaton transformToUppaalAutomata(
      SimulinkBlockWrapper blockForTransformation) {
    String blockSampleTime = blockForTransformation.getSampleTime();
    UppaalAutomaton blockAutomaton =
        Util.stringNullOrEmpty(blockSampleTime) ? loadContinuousTemplate() : loadDiscreteTemplate();
    mapSimulinkBlockToUppaalAutomaton(blockForTransformation, blockAutomaton);
    /* Add signals from predecessors as comments */
    String predecessors = "";
    for (Neighbour predecessor : blockForTransformation.getPredecessors()) {
      SimulinkBlockWrapper predecessorSimulinkBlock = predecessor.getSimulinkBlock();
      if (predecessorSimulinkBlock.exists()) {
        predecessors =
            predecessors.concat(String.format("%s (%s); ", predecessorSimulinkBlock.getNameForSTA(),
                predecessorSimulinkBlock.getIdInGlobalContext()));
      }

    }

    /* Add signals from successors as comments */
    predecessors +=
        String.format("successors begin (%d): ", blockForTransformation.getSuccessors().size());
    for (Neighbour successor : blockForTransformation.getSuccessors()) {
      SimulinkBlockWrapper successorSimulinkBlock = successor.getSimulinkBlock();
      if (successorSimulinkBlock.exists()) {
        predecessors = predecessors.concat(String.format("%s (%s); ",
            successorSimulinkBlock.getNameForSTA(), successorSimulinkBlock.getIdInGlobalContext()));
      }

    }
    String comments =
        String.format("// BlockId: %s \n  // Signal name: %s; \n // Predecessors (%d): %s",
            blockForTransformation.getIdInGlobalContext(), blockForTransformation.getSignalName(),
            blockForTransformation.getPredecessors().size(), predecessors);
    String declaration = blockAutomaton.getDeclaration();
    declaration = declaration.concat(comments);
    blockAutomaton.setDeclaration(declaration);
    return blockAutomaton;
  }

  public static String transformConstantBlocks(List<SimulinkBlockWrapper> constantBlocks) {
    String result = "";
    for (SimulinkBlockWrapper constantBlock : constantBlocks) {
    }
    return result;
  }

  public static String generateBlockRoutine(SimulinkBlockWrapper simulinkBlock) {
    String basicTemplate = "void blockRoutine(){}";
    String bRoutine = null;
    String blockType = simulinkBlock.getType().toLowerCase();
    BlockRoutineGeneratorInterface blockRoutineGenerator =
        BlockRoutineGeneratorPluginManager.loadPluginByName(blockType);
    if (blockRoutineGenerator != null) {
      try {
        bRoutine = blockRoutineGenerator.generateBlockRoutine(simulinkBlock);
      } catch (Exception ex) {
        bRoutine = null;
      }
    } else {
      System.out.println(String.format("Plugin for %s not found", blockType));
    }
    if (Util.stringNullOrEmpty(bRoutine)) {
      bRoutine = basicTemplate;
    }
    return bRoutine;
  }

  public static String generateInitRoutine(SimulinkBlockWrapper simulinkBlock) {
    String basicTemplate = "void customInit(){}";
    String bRoutine = null;
    BlockRoutineGeneratorInterface blockRoutineGenerator =
        BlockRoutineGeneratorPluginManager.loadPluginByName(simulinkBlock.getType().toLowerCase());
    if (blockRoutineGenerator != null) {
      try {
        bRoutine = blockRoutineGenerator.generateInitRoutine(simulinkBlock);
      } catch (Exception ex) {
        bRoutine = null;
      }
    }
    if (Util.stringNullOrEmpty(bRoutine)) {
      bRoutine = basicTemplate;
    }
    return bRoutine;
  }

}
