package org.fmaes.simulinktotimedautomata.transformers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.DoubleToLongFunction;

import org.fmaes.j2uppaal.builders.UppaalDocumentBuilder;
import org.fmaes.j2uppaal.datastructures.uppaalstructures.UppaalAutomaton;
import org.fmaes.j2uppaal.datastructures.uppaalstructures.UppaalDocument;
import org.fmaes.simulinktotimedautomata.builders.SimulinkModelBuilder;
import org.fmaes.simulinktotimedautomata.configuration.ApplicationConfiguration;
import org.fmaes.simulinktotimedautomata.sorder.SListParser;
import org.fmaes.simulinktotimedautomata.sorder.SortedOrderEntry;
import org.fmaes.simulinktotimedautomata.sorder.SortedOrderList;
import org.fmaes.simulinktotimedautomata.types.wrappers.Neighbour;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkModelWrapper;
import org.fmaes.simulinktotimedautomata.util.Util;

public class SimulinkModelTransformer {

  public static UppaalDocument transformSimulinkModeltoTimedAutomata(String simulinkModelName,
      String sListName) {
    String sListNameWithExtension = sListName;
    String simulinkModelNameWithExtension = simulinkModelName;
    ArrayList<String> continuousBlocksIds = new ArrayList<String>();
    ArrayList<String> discreteBlocksIds = new ArrayList<String>();
    if (!sListNameWithExtension.endsWith(".txt")) {
      sListNameWithExtension = String.format("%s.txt", sListName);
    }
    if (!simulinkModelNameWithExtension.endsWith(".mdl")) {
      simulinkModelNameWithExtension = String.format("%s.mdl", simulinkModelNameWithExtension);
    }
    ApplicationConfiguration appConfiguration = ApplicationConfiguration.loadConfiguration();
    String transformationDirectory = appConfiguration.getProperty("modelDirectory");
    Path simulinkModelPath = Paths.get(transformationDirectory, simulinkModelNameWithExtension);
    Path sListPath = Paths.get(transformationDirectory, sListNameWithExtension);
    SortedOrderList sList = SListParser.GetSortedOrderList(simulinkModelName, sListPath.toString());
    // sList.sort();
    /*
     * for (SortedOrderEntry sortedOrderEntry : sList) {
     * 
     * System.out .println(String.format("%s %s", sortedOrderEntry.sortedNumber,
     * sortedOrderEntry.id));
     * 
     * }
     */
    SimulinkModelWrapper rootSimulinkModel =
        SimulinkModelBuilder.loadWrappedSimulinkModel(simulinkModelPath.toString());
    SimulinkBlockWrapper simulinkBlock = null;
    long startTime = System.currentTimeMillis();
    UppaalDocument uppaalDocument =
        UppaalDocumentBuilder.buildUppaalDocument("./templates/automata/empty_document.xml");
    UppaalAutomaton automaton;
    int index = 0;
    Collection<SimulinkBlockWrapper> constantBlocks = new ArrayList<SimulinkBlockWrapper>();
    sList.Save(String.format("%s/executionorder.txt", transformationDirectory));
    for (SortedOrderEntry sortedOrderEntry : sList) {
      index++;
      simulinkBlock = rootSimulinkModel.getBlockById(sortedOrderEntry.id);
      if (simulinkBlock.exists()) {
        if (!simulinkBlock.isComputational()) {
          System.out.println("I am skiping block " + simulinkBlock.getIdInGlobalContext());
          continue;
        }
        simulinkBlock.executionOrderNumber = index;
        System.out
            .println(String.format("Parsing block: %s", simulinkBlock.getIdInGlobalContext()));
        if (simulinkBlock.getType().toLowerCase().equals("constant")) {
          constantBlocks.add(simulinkBlock);
          continue;
        }
        if (Util.stringNullOrEmpty(simulinkBlock.getSampleTime())) {
          continuousBlocksIds.add(simulinkBlock.getIdInGlobalContext());
        } else {
          discreteBlocksIds.add(simulinkBlock.getIdInGlobalContext());
        }
        String globalBlockId = simulinkBlock.getIdInGlobalContext();

        simulinkBlock.setPredecessors(rootSimulinkModel.findPredecessors(simulinkBlock));
        simulinkBlock.setSuccessors(rootSimulinkModel.findSuccessors(simulinkBlock));
        for (Neighbour predecessor : simulinkBlock.getPredecessors()) {
          String globalId = predecessor.getSimulinkBlock().getIdInGlobalContext();
          int execNumber = sList.getBlockExecutionOrderById(globalId);
          if (execNumber == 0 && !predecessor.getSimulinkBlock().isAtomic()) {
            execNumber = sList.getHighestChildExecutionOrder(globalId);
          }
          predecessor.getSimulinkBlock().executionOrderNumber = execNumber;
        }

        for (Neighbour successor : simulinkBlock.getSuccessors()) {
          String globalId = successor.getSimulinkBlock().getIdInGlobalContext();
          int execNumber = sList.getBlockExecutionOrderById(globalId);
          if (execNumber == 0 && !successor.getSimulinkBlock().isAtomic()) {
            execNumber = sList.getHighestChildExecutionOrder(globalId);
          }
          successor.getSimulinkBlock().executionOrderNumber = execNumber;
        }

        automaton = SimulinkAtomicBlockTransformer.transformToUppaalAutomata(simulinkBlock);
        uppaalDocument.addAndInstantiateAutomaton(automaton);
      } else {
        System.err.println(String.format("Block not found: %s", sortedOrderEntry.id));
      }
    }
    String documentDeclaration = uppaalDocument.getDeclaration();
    documentDeclaration += transformConstantBlocks(constantBlocks);
    uppaalDocument.setDeclaration(documentDeclaration);
    long endTime = System.currentTimeMillis();
    System.out.println(String.format("SList contains %d entriess", sList.size()));
    System.out.println(
        String.format("UPPAAL document contains total %d blocks (%d automata and %d constants)",
            uppaalDocument.getAllAutomata().size() + constantBlocks.size(),
            uppaalDocument.getAllAutomata().size(), constantBlocks.size()));

    long elapsedTime = (endTime - startTime) / 1000;
    System.out.println(String.format("The model contains %d discrete and %d continuous blocks",
        discreteBlocksIds.size(), continuousBlocksIds.size()));
    System.out.println(String.format("The model parsing took %s seconds", elapsedTime));
    return uppaalDocument;
  }


  private static String transformConstantBlocks(Collection<SimulinkBlockWrapper> constantBlocks) {
    String constantString = String.format("//Constants start (Total constants in the system %d) \n",
        constantBlocks.size());
    for (SimulinkBlockWrapper constantBlock : constantBlocks) {
      if (constantBlock.exists()) {
        String constValue = constantBlock.getParameter("Value");
        if (Util.isNumber(constValue) || Util.isBoolean(constValue)) {
          if (Util.isBoolean(constValue)) {
            if (constValue.toLowerCase().equals("true")) {
              constValue = "1.0";
            } else {
              constValue = "1.0";
            }
          }
          if (!Util.isDouble(constValue)) {
            constValue += ".0";
          }
          constantString +=
              String.format("const double %s = %s; \n", constantBlock.getSignalName(), constValue);
        } else {
          constantString +=
              String.format("double %s = %s; \n", constantBlock.getSignalName(), constValue);
        }
      }
    }
    constantString += "//Constants end";
    return constantString;
  }

}
