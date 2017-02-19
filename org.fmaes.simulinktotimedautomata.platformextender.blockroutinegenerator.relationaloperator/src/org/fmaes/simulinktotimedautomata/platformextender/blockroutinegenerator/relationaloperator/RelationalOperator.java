package org.fmaes.simulinktotimedautomata.platformextender.blockroutinegenerator.relationaloperator;

import org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface;
import org.fmaes.simulinktotimedautomata.types.wrappers.Neighbour;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;

public class RelationalOperator implements BlockRoutineGeneratorInterface {

  @Override
  public String generateBlockRoutine(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    String operator = blockForParsing.getParameter("Operator");
    if (operator.trim().equals("~=")) {
      operator = "!=";
    }
    String firstSignal = "";
    String secondSignal = "";
    String outSignal = "";
    if (blockForParsing.exists()) {
      outSignal = blockForParsing.getSignalName();
    }
    for (Neighbour predecessor : blockForParsing.getPredecessors()) {
      SimulinkBlockWrapper sBlock = predecessor.getSimulinkBlock();
      if (predecessor.getFromPort().getPortIndex().trim().equals("1")) {
        if (sBlock.exists()) {
          firstSignal = sBlock.getSignalName();
        }
      }
      if (predecessor.getFromPort().getPortIndex().trim().equals("2")) {
        if (sBlock.exists()) {
          secondSignal = sBlock.getSignalName();
        }
      }
    }
    String blockRoutine = "void blockRoutine(){\n#out# = #first# #operator# #second#;\n}";
    blockRoutine = blockRoutine.replace("#first#", firstSignal).replaceAll("#second#", secondSignal)
        .replaceAll("#operator#", operator).replaceAll("#out#", outSignal);
    return blockRoutine;
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

}
