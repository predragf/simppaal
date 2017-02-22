/**
 * 
 */
package org.fmaes.simulinktotimedautomata.platformextender.blockroutinegenerator.derivative;

import org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface;
import org.fmaes.simulinktotimedautomata.types.wrappers.Neighbour;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;

/**
 * @author pfj01
 *
 */
public class Derivative implements BlockRoutineGeneratorInterface {

  /*
   * (non-Javadoc)
   * 
   * @see org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface#
   * generateBlockRoutine(org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper)
   */
  @Override
  public String generateBlockRoutine(SimulinkBlockWrapper blockForParsing) {
    String insig1 = blockForParsing.getPredecessorAtPosition(0).getSimulinkBlock().getSignalName();
    String outsig1 = blockForParsing.getSuccessorAtPosition(0).getSimulinkBlock().getSignalName();

    String globalDecl = "double tprev = 0.0;\n" + "double stepsize = 0.1;\n" + "double stateval = 0.0;\n";
    String expression =
    String.format("void blockRoutine()// to be optimized!!"+
       " {"+
            "if(gtime >= tprev + stepsize){"+
        	  "  %2$s=(%1$s - stateval)/stepsize;"+
               " stateval = %1$s;"+
             "   tprev = gtime;}"+
    "}", insig1, outsig1);

    String bRoutine = String.format("%s%s", globalDecl, expression);

    return bRoutine;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface#
   * generateSignalDeclaration(org.fmaes.simulinktotimedautomata.types.wrappers.
   * SimulinkBlockWrapper)
   */
  @Override
  public String generateSignalDeclaration(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface#
   * generateInitRoutine(org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper)
   */
  @Override
  public String generateInitRoutine(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    String initRoutine = "void customInit(){tprev = gtime;}\n";
    return initRoutine;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface#
   * generateDeclaration(org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper)
   */
  @Override
  public String generateDeclaration(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    return null;
  }

}
