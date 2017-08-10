/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.transformers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.fmaes.j2uppaal.builders.UppaalDocumentBuilder;
import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.UppaalAutomatonInterface;
import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.UppaalDocumentInterface;
import org.fmaes.j2uppaal.datastructures.uppaalstructures.UppaalAutomaton;
import org.fmaes.j2uppaal.datastructures.uppaalstructures.UppaalDocument;
import org.fmaes.simppaal.simulinktotimedautomata.core.configuration.ApplicationConfiguration;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.Neighbour;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.SimulinkBlockWrapper;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.SimulinkModelWrapper;
import org.fmaes.simppaal.simulinktotimedautomata.platformextender.BlockRoutineGeneratorInterface;
import org.fmaes.simppaal.simulinktotimedautomata.platformextender.BlockRoutineGeneratorPluginManager;
import org.fmaes.simppaal.simulinktotimedautomata.sorder.SortedOrderEntry;
import org.fmaes.simppaal.simulinktotimedautomata.sorder.SortedOrderList;
import org.fmaes.simppaal.simulinktotimedautomata.utils.SimulinkUtils;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
public class SimulinkModelTransformer {

  private ApplicationConfiguration appConfig;

  private HashMap<String, UppaalAutomatonInterface> templates;

  private HashMap<String, BlockRoutineGeneratorInterface> plugins;

  private final String defaultPluginDirectory = "./plugins/";
  private final String defaultUPPAALTemplateFile = "./templates/uppaal/templates.xml";
  private final String defaultEmptyModelFile = "./templates/uppaal/empty.xml";
  private final String IAT = "0.0001";

  @SuppressWarnings("unused")
  private SimulinkModelTransformer() {

  }

  public SimulinkModelTransformer(ApplicationConfiguration _appConfig) {
    appConfig = _appConfig;
    loadTemplates();
    loadPlugins();
  }

  private void loadPlugins() {
    String pluginDirectory = appConfig.getProperty("pluginDirectory", defaultPluginDirectory);
    plugins = BlockRoutineGeneratorPluginManager.loadPluginsFromDirectory(pluginDirectory);
  }

  private void loadTemplates() {
    templates = new HashMap<String, UppaalAutomatonInterface>();
    String templateDocumentLocation =
        appConfig.getProperty("uppaalTemplatesFile", defaultUPPAALTemplateFile);
    UppaalDocument uppaalTemplateDocument =
        UppaalDocumentBuilder.buildUppaalDocument(templateDocumentLocation);

    for (UppaalAutomatonInterface automaton : uppaalTemplateDocument.getAllAutomata()) {
      if (automaton != null) {
        UppaalAutomaton atm = (UppaalAutomaton) automaton.clone();
        templates.put(automaton.getName(), atm);
      }
    }

  }

  private BlockRoutineGeneratorInterface findBlockGenerator(String blockType) {
    BlockRoutineGeneratorInterface bgi = plugins.get(blockType.toLowerCase());
    return bgi;
  }

  private UppaalAutomatonInterface loadAutomatonTemplate(String sTime) {
    UppaalAutomatonInterface blockAutomaton;

    if (sTime == null || sTime.equals("")) {
      blockAutomaton = templates.get("continuous");
    } else {
      blockAutomaton = templates.get("discrete");
    }

    return (UppaalAutomaton) blockAutomaton.clone();
  }

  private String generateComments(SimulinkBlockWrapper sBlock) {
    String predecessorIDs = "";
    for (Neighbour nbh : sBlock.getPredecessors()) {
      predecessorIDs += String.format("// %s \n", nbh.getSourceSimulinkBlock().getId());
    }
    return String.format("// BlockId: %s \n // Predecessors:\n %s", sBlock.getId(), predecessorIDs);
  }

  private String generateDefaultBlockRoutine(SimulinkBlockWrapper sBlock) {
    return String.format("void blockRoutine(){\n %s}", generateComments(sBlock));
  }

  private String generateBlockRoutine(SimulinkBlockWrapper sBlock,
      BlockRoutineGeneratorInterface generator) throws NullPointerException {
    String bRoutine = generator.generateBlockRoutine(sBlock);
    return (bRoutine != null && bRoutine.equals("")) ? bRoutine
        : generateDefaultBlockRoutine(sBlock);
  }

  private String generateDefaultInitialization(SimulinkBlockWrapper sBlock) {
    return "void customInit(){}";
  }

  private String generateBlockInitialization(SimulinkBlockWrapper sBlock,
      BlockRoutineGeneratorInterface generator) throws NullPointerException {
    String customInit = generator.generateInitRoutine(sBlock);
    return (customInit != null && !customInit.equals("")) ? customInit
        : generateDefaultInitialization(sBlock);
  }

  private String generateDefaultDafnyProcedure(SimulinkBlockWrapper sBlock) {
    return "";
  }

  private String generateDafnyProcedure(SimulinkBlockWrapper sBlock,
      BlockRoutineGeneratorInterface generator) throws NullPointerException {
    String dafnyProcedure = generator.generateDafnyVerificationRoutine(sBlock);
    return (dafnyProcedure != null && !dafnyProcedure.equals("")) ? dafnyProcedure
        : generateDefaultDafnyProcedure(sBlock);
  }

  private String generateAutomatonName(SimulinkBlockWrapper sBlock) {
    return String.format("%s_%d", sBlock.getNameNoWhiteSpaces(), sBlock.getExecutionOrderNumber());
  }

  private String generateDefaultDeclrationStatement(SimulinkBlockWrapper sBlock) {
    return "";
  }

  private String generateDeclrationStatement(SimulinkBlockWrapper sBlock,
      BlockRoutineGeneratorInterface generator) throws NullPointerException {
    String declaration = generator.generateDeclaration(sBlock);
    return (declaration != null && !declaration.equals("")) ? declaration
        : generateDefaultDeclrationStatement(sBlock);
  }

  private String generateDefaultSignalDeclaration(SimulinkBlockWrapper sBlock) {
    return String.format("double %s", sBlock.getSignalName());
  }

  private String generateSignalDeclaration(SimulinkBlockWrapper sBlock,
      BlockRoutineGeneratorInterface generator) throws NullPointerException {
    String signalDeclaration = generator.generateSignalDeclaration(sBlock);
    signalDeclaration = (signalDeclaration != null && !signalDeclaration.equals(""))
        ? signalDeclaration : generateDefaultSignalDeclaration(sBlock);
    return signalDeclaration.replaceAll(";", "");
  }

  private String getOffset(SimulinkBlockWrapper sBlock) {
    String offset = sBlock.getParameter("offset");
    if (offset == null || offset.equals("")) {
      offset = sBlock.getParameter("offset");
    }
    return (offset != null && offset != "") ? offset : "0";
  }

  private AutomatonData populateAutomatonDataFromPlugin(SimulinkBlockWrapper sBlock,
      BlockRoutineGeneratorInterface generator) {
    String aName = generateAutomatonName(sBlock);
    String sampleTime = sBlock.getSampleTime();
    String initialization = generateBlockInitialization(sBlock, generator);
    String blockRoutine = generateBlockRoutine(sBlock, generator);
    String dafnyProcedure = generateDafnyProcedure(sBlock, generator);
    String declarationStatement = generateDeclrationStatement(sBlock, generator);
    String signalDeclaration = generateSignalDeclaration(sBlock, generator);
    String offset = getOffset(sBlock);
    AutomatonData aData = new AutomatonData(aName, sampleTime, initialization, blockRoutine,
        dafnyProcedure, declarationStatement, signalDeclaration, offset, IAT);

    return aData;
  }

  private AutomatonData populateAutomatonDataWithDefaults(SimulinkBlockWrapper sBlock) {
    String aName = generateAutomatonName(sBlock);
    String sampleTime = sBlock.getSampleTime();
    String initialization = generateDefaultInitialization(sBlock);
    String blockRoutine = generateDefaultBlockRoutine(sBlock);
    String dafnyProcedure = generateDefaultDafnyProcedure(sBlock);
    String declarationStatement = generateDefaultDeclrationStatement(sBlock);
    String signalDeclaration = generateDefaultSignalDeclaration(sBlock);
    String offset = getOffset(sBlock);
    AutomatonData aData = new AutomatonData(aName, sampleTime, initialization, blockRoutine,
        dafnyProcedure, declarationStatement, signalDeclaration, offset, IAT);

    return aData;
  }

  private AutomatonData generateAutomatonData(SimulinkBlockWrapper sBlock) {
    BlockRoutineGeneratorInterface generator = findBlockGenerator(sBlock.getType());
    AutomatonData aData;
    if (generator != null) {
      aData = populateAutomatonDataFromPlugin(sBlock, generator);
    } else {
      aData = populateAutomatonDataWithDefaults(sBlock);
    }
    return aData;
  }

  private UppaalAutomatonInterface instantiateAutomaton(SimulinkBlockWrapper sBlock,
      AutomatonData aData) {
    if (sBlock.getExecutionOrderNumber() == 29) {
      for (Neighbour n : sBlock.getPredecessors()) {
        System.out.println(String.format("%d %s", sBlock.getExecutionOrderNumber(),
            n.getSourceSimulinkBlock().getSignalName()));
      }


    }
    String sampleTime = sBlock.getSampleTime();
    String executionOrderNumber = String.format("%d", sBlock.getExecutionOrderNumber());
    UppaalAutomaton blockAutomatonInstance = (UppaalAutomaton) loadAutomatonTemplate(sampleTime);
    String automatonDeclaration = blockAutomatonInstance.getDeclaration();

    automatonDeclaration = automatonDeclaration.replaceAll("#TS#", aData.getSampleTime());
    automatonDeclaration = automatonDeclaration.replaceAll("#IAT#", IAT);
    automatonDeclaration = automatonDeclaration.replaceAll("#SN#", executionOrderNumber);
    automatonDeclaration = automatonDeclaration.replaceAll("#OFFSET#", aData.getOffset());
    automatonDeclaration =
        automatonDeclaration.replaceAll("#BLOCKROUTINE#", aData.getBlockRoutine());
    automatonDeclaration =
        automatonDeclaration.replaceAll("#CUSTOMINIT#", aData.getInitialization());
    blockAutomatonInstance.setName(aData.getName());
    blockAutomatonInstance.setDeclaration(automatonDeclaration);
    return blockAutomatonInstance;
  }

  public UppaalAutomatonInterface generateAutomaton(SimulinkBlockWrapper sBlock) {
    AutomatonData aData = generateAutomatonData(sBlock);
    return instantiateAutomaton(sBlock, aData);
  }

  private void assignExecutionOrderOfPredecessors(SimulinkBlockWrapper sBlock,
      SortedOrderList sList) {
    for (Neighbour neighbour : sBlock.getPredecessors()) {
      SimulinkBlockWrapper nBlock = neighbour.getSourceSimulinkBlock();
      if (nBlock.exists()) {
        int executionOrderId = sList.getBlockExecutionOrderById(nBlock.getId());
        nBlock.setExecutionOrderNumber(executionOrderId);
      }
    }
  }

  private Collection<SimulinkBlockWrapper> generateBlocksForTransformation(
      SimulinkModelWrapper sModel, SortedOrderList sList) {
    Collection<SimulinkBlockWrapper> blocksForTransformation = new ArrayList<>();
    for (SortedOrderEntry sEntry : sList) {
      SimulinkBlockWrapper blockForTransformation = sModel.getBlockById(sEntry.id);
      if (blockForTransformation.exists() && blockForTransformation.isComputational()) {
        blockForTransformation.setExecutionOrderNumber(sEntry.getExecutionOrderIdAsInt());
        assignExecutionOrderOfPredecessors(blockForTransformation, sList);
        System.out.println(blockForTransformation.getType());
        blocksForTransformation.add(blockForTransformation);
      }
    }

    return blocksForTransformation;
  }

  public UppaalDocumentInterface generateUppaalModel(SimulinkModelWrapper sModel,
      SortedOrderList sList) {

    String emptyModelFile = appConfig.getProperty("emptyUppaalDocument", defaultEmptyModelFile);
    UppaalDocumentInterface uppaalModel = UppaalDocumentBuilder.buildUppaalDocument(emptyModelFile);
    String documentDeclaration = uppaalModel.getDeclaration();
    String systemDeclaration = uppaalModel.getSystem();
    UppaalAutomaton automaton;
    AutomatonData aData;
    Collection<SimulinkBlockWrapper> blocksForTransformation =
        generateBlocksForTransformation(sModel, sList);
    for (SimulinkBlockWrapper sBlock : blocksForTransformation) {
      if (SimulinkUtils.compareStringsIgnoreCase(sBlock.getType(), "constant")) {
        documentDeclaration = documentDeclaration.replaceAll("//signalsDeclaration",
            transformConstant(sBlock));
        continue;
      }
      aData = generateAutomatonData(sBlock);
      automaton = (UppaalAutomaton) instantiateAutomaton(sBlock, aData);
      uppaalModel.addAutomaton(automaton);
      documentDeclaration = documentDeclaration.replaceAll("//signalsDeclaration",
          String.format("%s;\n//signalsDeclaration", aData.getSignalDeclaration()));
      systemDeclaration = systemDeclaration.replaceAll("//declareHere",
          String.format("%s_instance = %s();\n//declareHere", aData.getName(), aData.getName()));
      systemDeclaration = systemDeclaration.replaceAll("//instantiateHere",
          String.format("%s_instance,//instantiateHere ", aData.getName()));
    }
    systemDeclaration =
        systemDeclaration.replaceAll("//declareHere", "").replaceAll(",//instantiateHere", ";");
    uppaalModel.setDeclaration(documentDeclaration);
    uppaalModel.setSystem(systemDeclaration);

    return uppaalModel;
  }

  private String transformConstant(SimulinkBlockWrapper constantBlock) {
    String constValue = constantBlock.getParameter("Value");
    return String.format("const double %s = %s;\n//signalsDeclaration", constantBlock.getSignalName(), constValue);
  }
}
