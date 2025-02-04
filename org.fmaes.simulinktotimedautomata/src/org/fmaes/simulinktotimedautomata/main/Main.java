package org.fmaes.simulinktotimedautomata.main;

import java.util.Collection;

import org.conqat.lib.simulink.model.SimulinkLine;
import org.conqat.lib.simulink.model.SimulinkModel;
import org.conqat.lib.simulink.model.SimulinkPortBase;
import org.fmaes.j2uppaal.datastructures.uppaalstructures.UppaalDocument;
import org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface;
import org.fmaes.simulinktotimedautomata.builders.SimulinkModelBuilder;
import org.fmaes.simulinktotimedautomata.platformextender.BlockRoutineGeneratorPluginManager;
import org.fmaes.simulinktotimedautomata.transformers.SimulinkModelTransformer;
import org.fmaes.simulinktotimedautomata.types.wrappers.Neighbour;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkModelWrapper;
import org.fmaes.simulinktotimedautomata.util.Util;

public class Main {

  public static void main(String[] args) {
    String simulinkModelName = "brake_acc_nodiv.mdl";
    String slistFileName = "sortedorder";
    
    Double dvatri = 2.3;
    Double dva = 2.0;
    
    
    System.out.println(Util.isDouble("2.0"));
    System.out.println(Util.isDouble("2."));
    System.out.println(Util.isDouble(".0"));
    System.out.println(Util.isDouble("2222.7"));
    System.out.println(Util.isDouble("2222"));
    System.out.println(Util.isDouble(""));
    
    System.out.println(Util.isInteger("2.0"));
    System.out.println(Util.isInteger("2."));
    System.out.println(Util.isInteger(".0"));
    System.out.println(Util.isInteger("2222.7"));
    System.out.println(Util.isInteger("2222"));
    System.out.println(Util.isInteger(""));

    SimulinkModelWrapper rootModel =
        SimulinkModelBuilder.loadWrappedSimulinkModelByName(simulinkModelName);

    String iAmNumber = "123";
    String iAmEmpty = "";
    String iAmNotNumber = "1A237";
    String iAmOnlyLetters = "predrag";
    String iAmNull = null;
    String iAmNullString = "null";
    System.out.println(Util.isNumber(iAmNumber));
    System.out.println(Util.isNumber(iAmEmpty));
    System.out.println(Util.isNumber(iAmNotNumber));
    System.out.println(Util.isNumber(iAmOnlyLetters));
    System.out.println(Util.isNumber(iAmNull));
    System.out.println(Util.isNumber(iAmNullString));

    if (rootModel.exists()) {
      String exampleFrom ="brake_acc_nodiv/Vehicle_Body_Wheels/half"; //"brake_acc_nodiv/RT8";// "brake_acc_nodiv/Vehicle_Body_Wheels/half";
      
      SimulinkBlockWrapper exampleFromBlock = rootModel.getBlockById(exampleFrom);
      if (exampleFromBlock.exists()) {
        Collection<Neighbour> successors = rootModel.findSuccessors(exampleFromBlock);
        for (Neighbour successor : successors) {
          System.out.println(successor.getSimulinkBlock().getIdInGlobalContext());
        }
        SimulinkBlockWrapper matchingGoto = mapFromToGoto(exampleFromBlock);
        if (matchingGoto.exists()) {
          System.out.println(matchingGoto.getIdInGlobalContext());
        }
      }

    }
    System.out.println("++++++++++");

    UppaalDocument resultModel = SimulinkModelTransformer
        .transformSimulinkModeltoTimedAutomata(simulinkModelName, slistFileName);
    resultModel.saveToFile("resultmodel_newest.xml");

    // TODO Auto-generated method stub

    /*
     * 
     * String rootSimulinkModelPath = "./models/simulink/BBW/brake_acc_nodiv.mdl"; String
     * referencedSSId = "brake_acc_nodiv/Veh_Speed_Estimator"; String routinesDirectory =
     * "./templates/block-routines"; String fileName = String.format("%s.txt", "add"); Path
     * routineFilePath = Paths.get(routinesDirectory, fileName); SimulinkModelWrapper rootModel =
     * SimulinkModelBuilder.buildSimulinkModel(rootSimulinkModelPath); ReferencedBlockInfo rbi =
     * rootModel.getSimulinkModel().getReferencedBlockInfo(); if(rootModel.isReferenced()){
     * System.out.println("root model recognised as referenced"); } SimulinkBlockWrapper
     * referencedSubSystem = rootModel.getBlockById(referencedSSId); SimulinkModelWrapper
     * referencedModel = SimulinkModelBuilder.buildReferencedSimulinkModel(referencedSubSystem);
     * if(referencedModel.exists() && referencedModel.isReferenced()){
     * System.out.println("Referenced model loaded successfully"); } String
     * addBlockInReferencedModelId = "brake_acc_nodiv/Veh_Speed_Estimator/Add"; SimulinkBlockWrapper
     * blockInReferencedModel = rootModel.getBlockById(addBlockInReferencedModelId);
     * if(blockInReferencedModel.exists()){
     * System.out.println(blockInReferencedModel.getIdInLocalContext());
     * System.out.println(blockInReferencedModel.getIdInGlobalContext());
     * System.out.println(blockInReferencedModel.getSampleTime()); }
     * 
     * 
     * 
     * String rootSimulinkModelPath = "./models/simulink/BBW/brake_acc_nodiv.mdl"; String
     * atomicSimulinkBlockId = "brake_acc_nodiv/RT13"; String testingId = "brake_acc_nodiv/RT9";
     * String referencedModelBlockId = "brake_acc_nodiv/Veh_Speed_Estimator"; String pedalMapId =
     * "brake_acc_nodiv/LDM_BrakePedal/elSignal"; String hwRRBrakeId = "brake_acc_nodiv/HW_RRBrake";
     * 
     * SimulinkModelWrapper wrappedRootModel =
     * SimulinkModelBuilder.buildSimulinkModel(rootSimulinkModelPath); SimulinkBlockWrapper
     * atomicSimulinkBlock = wrappedRootModel.getBlockById(atomicSimulinkBlockId);
     * SimulinkBlockWrapper pedalMap = wrappedRootModel.getBlockById(pedalMapId);
     * 
     * SimulinkBlockWrapper hwRRBrake = wrappedRootModel.getBlockById(hwRRBrakeId);
     * SimulinkBlockWrapper testing = wrappedRootModel.getBlockById(testingId);
     * Collection<SimulinkBlockWrapper> testinPredecessors = testing.getPredecessors();
     * Collection<SimulinkLineWrapper> linezzz = hwRRBrake.getOutgoingLines();
     * Collection<SimulinkBlockWrapper> prdcessors = pedalMap.getPredecessors();
     * System.out.println("predecessors start"); for (SimulinkBlockWrapper simulinkBlockWrapper :
     * testinPredecessors) { System.out.println(simulinkBlockWrapper.getId()); }
     * System.out.println("predecessors end"); SimulinkBlockWrapper referenceModelBlockWrap =
     * wrappedRootModel.getBlockById(referencedModelBlockId); SimulinkModelWrapper
     * wrappedReferencedModel = SimulinkModelBuilder.buildReferencedSimulinkModel(
     * referenceModelBlockWrap, "./models/simulink/BBW/lib_spd_estimator_nodiv.mdl");
     * System.out.println(wrappedReferencedModel.getGlobalId());
     * System.out.println(referenceModelBlockWrap.getType()); System.out.println("libname " +
     * referenceModelBlockWrap.getLibraryName()); System.out.println("libname plus subsystem" +
     * referenceModelBlockWrap.getLibraryandSubsystem());
     * 
     * System.out.println(atomicSimulinkBlock.getId());
     * System.out.println(atomicSimulinkBlock.getName());
     * System.out.println("Just for debugging purpose");
     * 
     * String globalId = "model/ss1/ss2/ss3/ss4/blk"; String localId = "library/ss5";
     * System.out.println(Util.buildLocalId(globalId, localId));
     * 
     * String addBlockInReferencedModelId = "brake_acc_nodiv/Veh_Speed_Estimator/Add";
     * SimulinkBlockWrapper additioninref =
     * wrappedRootModel.getBlockById(addBlockInReferencedModelId); String bid =
     * additioninref.getId(); System.out.println(bid);
     */
  }

  static void printBlockProps(SimulinkBlockWrapper block) {
    for (String declaredParam : block.getDeclaredParameterNames()) {
      System.out.println("param name: " + declaredParam);
      System.out.println("param value: " + block.getDeclaredParameter(declaredParam));
    }
  }

  private static void printModelDeclaredParam(SimulinkModelWrapper modelWrapped) {
    SimulinkModel sModel = modelWrapped.getSimulinkModel();
    for (String prop : sModel.getParameterNames()) {
      System.out.println("name: " + prop);
      System.out.println("value: " + sModel.getDeclaredParameter(prop));

    }

  }

  public static SimulinkBlockWrapper mapFromToGoto(SimulinkBlockWrapper fromBlock) {
    SimulinkModelWrapper currentModel = fromBlock.getSimulinkModelWrapped();
    String gotoTag = fromBlock.getParameter("GotoTag");
    Collection<SimulinkBlockWrapper> allGotTos = currentModel.getChildBlocksByType("goto");
    SimulinkBlockWrapper matchingGoto = new SimulinkBlockWrapper(null);
    for (SimulinkBlockWrapper gotoBlock : allGotTos) {
      if (gotoBlock.exists()
          && Util.matchStringsIgnoreCase(gotoTag, gotoBlock.getDeclaredParameter("GotoTag"))) {
        matchingGoto = gotoBlock;
        break;
      }
    }
    return matchingGoto;
  }

}
