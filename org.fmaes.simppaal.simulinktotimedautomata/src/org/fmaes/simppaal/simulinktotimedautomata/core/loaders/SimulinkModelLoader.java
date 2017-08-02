/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.loaders;

import java.io.File;

import org.conqat.lib.commons.logging.SimpleLogger;
import org.conqat.lib.simulink.builder.SimulinkModelBuilder;
import org.conqat.lib.simulink.model.SimulinkModel;
import org.fmaes.simppaal.simulinktotimedautomata.core.configuration.ApplicationConfiguration;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.SimulinkBlockWrapper;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.SimulinkModelWrapper;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.interfaces.SimulinkModelWrapperInterface;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
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

  public SimulinkModelWrapperInterface loadAndWrapSimulinkModelByName(String simulinkModelName) {
    SimulinkModel simulinkModel = loadSimulinkModelByName(simulinkModelName);
    return new SimulinkModelWrapper(simulinkModel);
  }


  public File openFileAssumingExtension(String modelDirectory, String modelName, String extension) {
    String modelNameWithExtension = modelName.concat(extension);
    return new File(modelDirectory, modelNameWithExtension);
  }

  public SimulinkModel loadSimulinkModel(String modelDirectory, String modelName) {
    // if the model name
    File modelFile;
    if (!modelName.endsWith(".mdl") && !modelName.endsWith(".slx")) {
      modelFile = openFileAssumingExtension(modelDirectory, modelName, ".mdl");
      if (!modelFile.exists()) {
        modelFile = openFileAssumingExtension(modelDirectory, modelName, ".slx");
      }
    } else {
      modelFile = openFileAssumingExtension(modelDirectory, modelName, "");
    }
    return loadSimulinkModelFromFile(modelFile);
  }

  public SimulinkModelWrapperInterface loadAndWrapSimulinkmodel(String modelDirectory,
      String modelName) {
    SimulinkModel simulinkModel = loadSimulinkModel(modelDirectory, modelName);
    return new SimulinkModelWrapper(simulinkModel);
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
