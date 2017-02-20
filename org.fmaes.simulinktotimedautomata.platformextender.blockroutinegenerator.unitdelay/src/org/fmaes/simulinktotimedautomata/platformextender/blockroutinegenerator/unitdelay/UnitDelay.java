/**
 * 
 */
package org.fmaes.simulinktotimedautomata.platformextender.blockroutinegenerator.unitdelay;

import org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface;
import org.fmaes.simulinktotimedautomata.types.wrappers.Neighbour;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;

/**
 * @author pfj01
 *
 */
public class UnitDelay implements BlockRoutineGeneratorInterface {

  /*
   * (non-Javadoc)
   * 
   * @see org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface#
   * generateBlockRoutine(org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper)
   */
  @Override
  public String generateBlockRoutine(SimulinkBlockWrapper blockForParsing) {
    Neighbour pred = blockForParsing.getPredecessorAtPosition(0);
    // TODO Auto-generated method stub
    String blockRoutine = "";
    return null;
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
    return null;
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
