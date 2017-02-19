package org.fmaes.simulinktotimedautomata.gui;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.fmaes.j2uppaal.datastructures.uppaalstructures.UppaalDocument;
import org.fmaes.simulinktotimedautomata.configuration.ApplicationConfiguration;
import org.fmaes.simulinktotimedautomata.transformers.SimulinkModelTransformer;

public class Util {

  public static void executeTransformationProcedure(String simulinkModelLocation,
      String sListLocation, String resultFileLocation) {

    ApplicationConfiguration config = ApplicationConfiguration.loadConfiguration();
    Path simulinkModelPath = Paths.get(simulinkModelLocation);
    Path slistPath = Paths.get(sListLocation);
    Path resultModelFilePath = Paths.get(resultFileLocation);
    String simulinkModelDirectory = simulinkModelPath.getParent().toString();
    config.setProperty("modelDirectory", simulinkModelDirectory);
    config.saveConfiguration();
    String simulinkModelName = simulinkModelPath.getFileName().toString();
    String slistFileName = slistPath.getFileName().toString();
    int lastIndexOfDot = simulinkModelName.lastIndexOf(".");
    simulinkModelName = simulinkModelName.substring(0, lastIndexOfDot);
    lastIndexOfDot = slistFileName.lastIndexOf(".");
    slistFileName = slistFileName.substring(0, lastIndexOfDot);
    UppaalDocument resultModel = SimulinkModelTransformer
        .transformSimulinkModeltoTimedAutomata(simulinkModelName, slistFileName);
    resultModel.saveToFile(resultModelFilePath.toAbsolutePath().toString());

  }
}
