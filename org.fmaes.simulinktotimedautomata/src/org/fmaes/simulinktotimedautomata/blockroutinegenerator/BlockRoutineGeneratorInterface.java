package org.fmaes.simulinktotimedautomata.blockroutinegenerator;

import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;

public interface BlockRoutineGeneratorInterface {

  public String generateBlockRoutine(SimulinkBlockWrapper blockForParsing);

  public String generateSignalDeclaration(SimulinkBlockWrapper blockForParsing);

  public String generateInitRoutine(SimulinkBlockWrapper blockForParsing);

  public String generateDeclaration(SimulinkBlockWrapper blockForParsing);

  public String generateDafnyVerificationRoutine(SimulinkBlockWrapper blockForParsing);
}

