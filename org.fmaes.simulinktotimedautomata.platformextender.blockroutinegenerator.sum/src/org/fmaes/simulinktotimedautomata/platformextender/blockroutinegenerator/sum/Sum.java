package org.fmaes.simulinktotimedautomata.platformextender.blockroutinegenerator.sum;

import org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface;
import org.fmaes.simulinktotimedautomata.types.wrappers.Neighbour;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;

public class Sum implements BlockRoutineGeneratorInterface {

  @Override
  public String generateBlockRoutine(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    String _inputs = blockForParsing.getParameter("Inputs");
    String sumRoutine = "void blockRoutine() {\n #expression#; \n}";
    String sumExpression = String.format("%s = ", blockForParsing.getSignalName());
    int index = 0;
    for (Neighbour predecessor : blockForParsing.getPredecessors()) {
      String predecessorSignal = predecessor.getSimulinkBlock().getSignalName();
      if (index < _inputs.length()
          && (_inputs.charAt(index) == '+' || _inputs.charAt(index) == '-')) {
        sumExpression += _inputs.charAt(index) + predecessorSignal;
      } else {
        sumExpression += predecessorSignal;
      }
      index++;
    }
    return sumRoutine.replaceAll("#expression#", sumExpression);
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
