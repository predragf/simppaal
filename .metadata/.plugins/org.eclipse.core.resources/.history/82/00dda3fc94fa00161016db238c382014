/**
 * 
 */
package org.fmaes.simulinktotimedautomata.platformextender.blockroutinegenerator.integrator;

import org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface;
import org.fmaes.simulinktotimedautomata.types.wrappers.Neighbour;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;

/**
 * @author pfj01
 *
 */
public class Integrator implements BlockRoutineGeneratorInterface {

  /*
   * (non-Javadoc)
   * 
   * @see org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface#
   * generateBlockRoutine(org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper)
   */
  @Override
  public String generateBlockRoutine(SimulinkBlockWrapper blockForParsing) {
    String insig1 = blockForParsing.getPredecessorAtPosition(0).getSimulinkBlock().getSignalName();
    String insig2 = blockForParsing.getPredecessorAtPosition(1).getSimulinkBlock().getSignalName();
    String outsig1 = blockForParsing.getSuccessorAtPosition(0).getSimulinkBlock().getSignalName();

    String globalDecl = String.format("" 
    + "double tprev = 0.0;%n" 
    + "double stepsize = 0.1;%n" 
    + "double stateval = %s;%n",
        insig2);
    
    String expression =
        String.format("void blockRoutine()// to be optimized!!%n"+
        "{%n"+
            "if(gtime >= tprev + stepsize)%n"+
         "	{"+
         "  	%2$s = %1$s+stateval;%n"+
                "stateval=%2$s; tprev = gtime;%n"+
         "	}%n"+
        "}%n", insig1, outsig1);

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
