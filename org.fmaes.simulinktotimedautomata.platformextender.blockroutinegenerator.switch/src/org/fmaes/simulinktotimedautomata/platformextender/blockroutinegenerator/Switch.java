package org.fmaes.simulinktotimedautomata.platformextender.blockroutinegenerator;

import org.conqat.lib.simulink.model.SimulinkBlock;
import org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface;
import org.fmaes.simulinktotimedautomata.types.wrappers.Neighbour;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;

public class Switch implements BlockRoutineGeneratorInterface {

  @Override
  public String generateBlockRoutine(SimulinkBlockWrapper blockForParsing) {
    String bRoutine = "void blockRoutine(){}";
    try {
      bRoutine = tryToCreateBlockRoutine(blockForParsing);
    } catch (Exception ex) {
      bRoutine = "void blockRoutine(){}";
    }
    return bRoutine;
  }

  private String tryToCreateBlockRoutine(SimulinkBlockWrapper blockForParsing) {
    String blockRoutine =
        "void blockRoutine() {\nif(#second# #sign# #thold#) {\n#out# = #first#;\n} else {\n#out# = #third#;\n}\n}";
    String firstSignalId = "";
    String secondSignalId = "";
    String thirdSignalId = "";
    String outSingalId = blockForParsing.getSignalName();
    for (Neighbour predecessor : blockForParsing.getPredecessors()) {
      String signalId = predecessor.getSimulinkBlock().getSignalName();
      if (predecessor.getFromPort().getPortIndex().trim().equals("1")) {
        firstSignalId = signalId;
      }
      if (predecessor.getFromPort().getPortIndex().trim().equals("2")) {
        secondSignalId = signalId;
      }
      if (predecessor.getFromPort().getPortIndex().trim().equals("3")) {
        thirdSignalId = signalId;
      }
    }
    // TODO Auto-generated method stub
    /* We know that this is swtich and that it has 3 inputs */

    /* Threshold */
    /*
     * Criteria is always for passing the first input >, >=, !=
     */
    SimulinkBlock baseSimulinkBlock = blockForParsing.getSimulinkBlock();
    String th = blockForParsing.getParameter("Threshold");
    String criteria = blockForParsing.getParameter("Criteria");
    String sign = "";
    if (criteria.contains(">=")) {
      sign = ">=";
    } else if (criteria.contains(">")) {
      sign = ">";
    } else if (criteria.contains("~=")) {
      sign = "!=";
    }
    return blockRoutine.replaceAll("#first#", firstSignalId).replaceAll("#second#", secondSignalId)
        .replaceAll("#third#", thirdSignalId).replaceAll("#out#", outSingalId)
        .replaceAll("#thold#", th).replaceAll("#sign#", sign);
  }

  @Override
  public String generateSignalDeclaration(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String generateInitRoutine(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String generateDeclaration(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface#generateDafnyVerificationRoutine(org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper)
   */
  @Override
  public String generateDafnyVerificationRoutine(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    return null;
  }

}
