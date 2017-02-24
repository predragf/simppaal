package org.fmaes.simulinktotimedautomata.platformextender.blockroutinegenerator.minmax;

import org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface;
import org.fmaes.simulinktotimedautomata.types.wrappers.Neighbour;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;

public class MinMax implements BlockRoutineGeneratorInterface {

  @Override
  public String generateBlockRoutine(SimulinkBlockWrapper blockForParsing) {

    String roundingMethod = blockForParsing.getDeclaredParameter("RndMeth");
    String saturateOnIntegerOverflow =
        blockForParsing.getDeclaredParameter("SaturateOnIntegerOverflow");
    String zeroCross = blockForParsing.getDeclaredParameter("ZeroCross");
    String blockR = "void blockRoutine(){#routineBody#;}";
    String outSignal = blockForParsing.getSignalName();
    /* you should read the correct one */
    String function = blockForParsing.getDeclaredParameter("Function");
    String operator = "<=";
    if (function != null || function.trim().equals("max")) {
      operator = ">=";
    }

    String routineBody = "";
    for (Neighbour prdcsr : blockForParsing.getPredecessors()) {
      if (!prdcsr.getSimulinkBlock().exists()) {
        continue;
      }
      String predecessorCondition = "";
      int counter = 0;
      for (Neighbour predecessor : blockForParsing.getPredecessors()) {
        if (predecessor.getSimulinkBlock().exists() && prdcsr.getSimulinkBlock()
            .getSignalName() != predecessor.getSimulinkBlock().getSignalName()) {
          if (counter == 0) {
            predecessorCondition =
                String.format("%s %s %s", prdcsr.getSimulinkBlock().getSignalName(), operator,
                    predecessor.getSimulinkBlock().getSignalName());
          } else {
            predecessorCondition +=
                String.format("&amp;&amp; %s %s %s", prdcsr.getSimulinkBlock().getSignalName(),
                    operator, predecessor.getSimulinkBlock().getSignalName());
          }
        }
        counter++;
      }
      String conditionFixed = String.format("if(%s){%s = %s;}\n", predecessorCondition, outSignal,
          prdcsr.getSimulinkBlock().getSignalName());
      routineBody += conditionFixed;
    }
    blockR = blockR.replace("#routineBody#", routineBody);
    return blockR;
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

  /* (non-Javadoc)
   * @see org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface#generateDafnyVerificationRoutine(org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper)
   */
  @Override
  public String generateDafnyVerificationRoutine(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    return null;
  }

}
