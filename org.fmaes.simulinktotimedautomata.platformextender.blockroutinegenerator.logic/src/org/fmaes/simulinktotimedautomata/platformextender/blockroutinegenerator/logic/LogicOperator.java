package org.fmaes.simulinktotimedautomata.platformextender.blockroutinegenerator.logic;

import java.util.Iterator;

import org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface;
import org.fmaes.simulinktotimedautomata.types.wrappers.Neighbour;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;

public class LogicOperator implements BlockRoutineGeneratorInterface {

  @Override
  public String generateBlockRoutine(SimulinkBlockWrapper blockForParsing) {
    String outSignal = blockForParsing.getSignalName();
    String operatorType = blockForParsing.getDeclaredParameter("Operator");
    if (operatorType == null || operatorType.trim().equals("")) {
      operatorType = "and";
    }
    // TODO Auto-generated method stub
    String expression = "";
    int counter = 0;
    if (operatorType.toLowerCase().equals("and")) {
      for (Neighbour predecessor : blockForParsing.getPredecessors()) {
        if (!predecessor.getSimulinkBlock().exists()) {
          continue;
        }
        if (counter == 0) {
          expression += String.format("%s == 1.0", predecessor.getSimulinkBlock().getSignalName());
        } else {
          expression += String.format(" &amp;&amp; %s == 1.0",
              predecessor.getSimulinkBlock().getSignalName());
        }
        counter++;
      }
    }
    if (operatorType.toLowerCase().equals("or")) {
      for (Neighbour predecessor : blockForParsing.getPredecessors()) {
        if (!predecessor.getSimulinkBlock().exists()) {
          continue;
        }
        if (counter == 0) {
          expression += String.format("%s == 1.0", predecessor.getSimulinkBlock().getSignalName());
        } else {
          expression +=
              String.format(" || %s == 1.0", predecessor.getSimulinkBlock().getSignalName());
        }
        counter++;
      }
    }
    if (operatorType.toLowerCase().equals("not")) {
      Iterator iter = blockForParsing.getPredecessors().iterator();
      if (iter.hasNext()) {
        Neighbour first = (Neighbour) iter.next();
        expression = String.format("%s == 0.0", first.getSimulinkBlock().getSignalName());
      }
    }
    return String.format("void blockRoutine(){%s = %s;}", outSignal, expression);
  }

  @Override
  public String generateSignalDeclaration(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    return null;
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
