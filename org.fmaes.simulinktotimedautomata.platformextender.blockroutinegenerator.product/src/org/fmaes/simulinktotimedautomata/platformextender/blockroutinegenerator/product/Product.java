package org.fmaes.simulinktotimedautomata.platformextender.blockroutinegenerator.product;

import org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface;
import org.fmaes.simulinktotimedautomata.types.wrappers.Neighbour;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;

public class Product implements BlockRoutineGeneratorInterface {

  @Override
  public String generateBlockRoutine(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    String bRoutine = "void blockRoutine(){#expression#;}";
    String expression = String.format("%s = ", blockForParsing.getSignalName());
    int counter = 0;
    for (Neighbour predecessor : blockForParsing.getPredecessors()) {
      if (counter == 0) {
        expression += predecessor.getSimulinkBlock().getSignalName();
      } else {
        expression += " * " + predecessor.getSimulinkBlock().getSignalName();
      }
      counter++;
    }
    bRoutine = bRoutine.replace("#expression#", expression);
    return bRoutine;
  }

  @Override
  public String generateSignalDeclaration(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    return "";
  }

  @Override
  public String generateInitRoutine(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    return "";
  }

  @Override
  public String generateDeclaration(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    return "";
  }

}
