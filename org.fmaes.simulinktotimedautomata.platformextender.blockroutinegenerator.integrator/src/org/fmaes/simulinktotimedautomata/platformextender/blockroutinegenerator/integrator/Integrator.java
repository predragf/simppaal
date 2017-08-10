/**
 * 
 */
package org.fmaes.simulinktotimedautomata.platformextender.blockroutinegenerator.integrator;

import java.util.ArrayList;

import org.fmaes.simppaal.simulinktotimedautomata.core.types.Neighbour;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.SimulinkBlockWrapper;
import org.fmaes.simppaal.simulinktotimedautomata.platformextender.BlockRoutineGeneratorInterface;

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
    ArrayList<Neighbour> predecessors = blockForParsing.getPredecessorsAsList();
    String insig1 = predecessors.get(0).getSourceSimulinkBlock().getSignalName();
    String insig2 = predecessors.get(1).getSourceSimulinkBlock().getSignalName();
    String outsig1 = blockForParsing.getSignalName();

    String globalDecl = String.format(
        "" + "double tprev = 0.0;%n" + "double stepsize = 0.1;%n" + "double stateval = %s;%n",
        insig2);

    String expression = String.format("void blockRoutine()// to be optimized!!%n" + "{%n"
        + "if(gtime >= tprev + stepsize)%n" + "	{" + "  	%2$s = %1$s+stateval;%n"
        + "stateval=%2$s; tprev = gtime;%n" + "	}%n" + "}%n", insig1, outsig1);

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
    return null;
  }

}
