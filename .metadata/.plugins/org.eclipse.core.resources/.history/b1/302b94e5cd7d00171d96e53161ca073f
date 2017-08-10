package org.fmaes.simulinktotimedautomata.platformextender.blockroutinegenerator.gain;

import java.util.Iterator;

import org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface;
import org.fmaes.simulinktotimedautomata.types.wrappers.Neighbour;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;

public class Gain implements BlockRoutineGeneratorInterface {

  @Override
  public String generateBlockRoutine(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    /*
     * The mode of gain is determined based on the input and the gain parameter also called
     * Multiplication
     */
    String elementWise = "element-wise(k.*u)";
    String matrixKu = "matrix(k*u)";
    String matrixuK = "matrix(u*k)";
    String blockRoutine = "void blockRoutine(){\n}";

    String operationMode = blockForParsing.getParameter("Multiplication");
    String gain = blockForParsing.getParameter("Gain");
    String outmin = blockForParsing.getParameter("OutMin");
    String outmax = blockForParsing.getParameter("OutMax");

    String outSignalName = blockForParsing.getSignalName();
    Iterator<Neighbour> iterator = blockForParsing.getPredecessors().iterator();
    Neighbour predecessor = null;
    if (iterator.hasNext()) {
      predecessor = iterator.next();
    }
    if (operationMode.trim().toLowerCase().equals(elementWise) && predecessor != null) {
      String inputSignalName = predecessor.getSimulinkBlock().getNameForSTA();
      inputSignalName = predecessor.getSimulinkBlock().getSignalName();
      blockRoutine = gainScalarScalar(inputSignalName, outSignalName, gain);
    }
    return blockRoutine;
  }

  private String gainScalarScalar(String input, String output, String gain) {
    String blockRoutine =
        String.format("void blockRoutine(){\n %s = %s * %s;\n}", output, input, gain);
    return blockRoutine;
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
