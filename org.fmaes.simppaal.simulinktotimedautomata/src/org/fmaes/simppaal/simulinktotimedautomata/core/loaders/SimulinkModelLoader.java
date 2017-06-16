/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.loaders;

import java.io.File;
import java.util.Collection;

import org.conqat.lib.commons.logging.SimpleLogger;
import org.conqat.lib.simulink.model.SimulinkModel;
import org.fmaes.simppaal.simulinktotimedautomata.core.configuration.ApplicationConfiguration;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.wrappers.*;
import org.conqat.lib.simulink.builder.SimulinkModelBuilder;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se) We provide 2 options for loading
 *         SimulinkModels from disk: i) by extracting the relevant information from the SubSystem
 *         block that is reference to a model and ii) by giving the full path to the SimulinkModel
 *         file to the loader.
 */
public class SimulinkModelLoader {

  private ApplicationConfiguration applicationConfiguration;

  /**
   * This is forbidden.
   */
  @SuppressWarnings("unused")
  private SimulinkModelLoader() {}

  public SimulinkModelLoader(ApplicationConfiguration config) {
    applicationConfiguration = config;
  }

  public SimulinkModel loadSimulinkModelByName(String simulinkModelName) {
    String modelDirectory = applicationConfiguration.getProperty("modelDirectory");
    return loadSimulinkModel(modelDirectory, simulinkModelName);
  }

  public SimulinkModel loadSimulinkModel(String modelDirectory, String modelName) {
    if (!modelName.endsWith(".mdl")) {
      modelName = modelName.concat(".mdl");
    }
    File modelFile = new File(modelDirectory, modelName);
    return loadSimulinkModelFromFile(modelFile);
  }

  public SimulinkModel loadSimulinkModel(SimulinkBlockWrapper externalModelReferenceBlock) {
    String modelDirectory = applicationConfiguration.getProperty("modelDirectory");
    if (modelDirectory == null || modelDirectory.length() < 1) {
      System.out.println("The model directory does not exist in the configuration file.");
      return null;
    }
    return loadSimulinkModel(modelDirectory,
        externalModelReferenceBlock.getReferencedModelNameWithoutExtension());
  }

  private SimulinkModel loadSimulinkModelFromFile(File modelFile) {
    SimulinkModel simulinkModel = null;
    try (SimulinkModelBuilder builder = new SimulinkModelBuilder(modelFile, new SimpleLogger())) {
      simulinkModel = builder.buildModel();
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
    return simulinkModel;
  }



}
