/**
 * 
 */
package org.fmaes.simulinktotimedautomata.platformextender.blockroutinegenerator.rounding;

import org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface;
import org.fmaes.simulinktotimedautomata.types.wrappers.Neighbour;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;

/**
 * @author pfj01
 *
 */
public class Rounding implements BlockRoutineGeneratorInterface {

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
    String operation = blockForParsing.getParameter("Operator");
    
    final int FLOOR = 0, CEILING =1, ROUND = 2;
    int operation_int = 0;
    if(operation.equals("floor"))
    	operation_int = FLOOR;
    else if(operation.equals("ceiling"))
    	operation_int = CEILING;
    else if(operation.equals("round"))
    	operation_int = ROUND;
    
    String globalDecl =" bool rounding = true;\ndouble i, in_prev;\n";
    String expression =
        String.format("/* Rounding */"
      +"     		void blockRoutine()"
      +"    		{"
      +"   		        if(%2$s != in_prev)"
      +"  		    {in_prev = %2$s;"
      +" 		    while(rounding)"
      +"  		    {"
      +"  		        i = i + 1.0;"
      +"  		        if (i > %2$s)"
      +"  		        {"
      +"  		            if(%1$s == 0)//floor"
      +"  		                %3$s = i-1.0;"
      +"  		            else if(%1$s == 1)//ceiling"
      +"  		                %3$s = i;"
      +"  		            else if(%1$s == 2)//round"
      +"  		            {"
      +"  		                if(i - %2$s > 0.5)//floor"
      +"  		                    %3$s = i-1.0;"
      +"  		                else//ceiling"
      +"  		                    %3$s = i;"
      +"  		            }"
      +"  		            rounding = false;"
      +"  		        }"
      +"  		    }rounding = true;i = 0.0;"
      +"  		    }"
      +"  		}", operation_int, insig1, outsig1);

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
