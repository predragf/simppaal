package org.fmaes.simulinktotimedautomata.platformextender.blockroutinegenerator.abs;

import java.util.Collection;
import java.util.Iterator;

import org.fmaes.simppaal.simulinktotimedautomata.core.types.Neighbour;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.SimulinkBlockWrapper;
import org.fmaes.simppaal.simulinktotimedautomata.platformextender.BlockRoutineGeneratorInterface;

public class Abs implements BlockRoutineGeneratorInterface {

  @Override
  public String generateBlockRoutine(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    String outSignal = blockForParsing.getSignalName();
    Collection<Neighbour> neighbours = blockForParsing.getPredecessors();
    Iterator iter = neighbours.iterator();
    Neighbour p = null; // = blockForParsing.getPredecessorAtPosition(0);
    if (iter.hasNext()) {
      p = (Neighbour) iter.next();
    }
    String inSignal = "";
    if (p != null) {
      SimulinkBlockWrapper sb = p.getSourceSimulinkBlock();
      inSignal = sb.getSignalName();
    }
    return "void blockRoutine(){\n #out# = #in#;\n if(#out# < 0){\n #out# = #out# * -1.0; \n}\n }"
        .replace("#out#", outSignal).replaceAll("#in#", inSignal);
  }

  @Override
  public String generateSignalDeclaration(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    return String.format("double %s;", blockForParsing.getSignalName());
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

  /*
   * (non-Javadoc)
   * 
   * @see org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface#
   * generateDafnyVerificationRoutine(org.fmaes.simulinktotimedautomata.types.wrappers.
   * SimulinkBlockWrapper)
   */
  @Override
  public String generateDafnyVerificationRoutine(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    /* Assumes that always has one input */
    return "method absR(input: real) returns (out: real) ensures out >= 0.0 { if (input < 0.0) {return -1.0 * input;} return input; }";
  }

}
