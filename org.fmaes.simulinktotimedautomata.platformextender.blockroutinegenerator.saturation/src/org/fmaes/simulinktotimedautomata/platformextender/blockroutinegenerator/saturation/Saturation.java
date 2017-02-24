package org.fmaes.simulinktotimedautomata.platformextender.blockroutinegenerator.saturation;

import java.util.Iterator;

import org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface;
import org.fmaes.simulinktotimedautomata.types.wrappers.Neighbour;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;

public class Saturation implements BlockRoutineGeneratorInterface {

  @Override
  public String generateBlockRoutine(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    String roundCeil = "double roundCeil(double input){\n"
        + "double i = 0.0;\n"
        + "if(input > -1.0 &amp;&amp; input <= 0.0){\n"
        + "return 0.0;\n"
        + "}\n"
        + "do {\n"
        + "i = i + 1.0;\n"
        + "} while(input - i > 0.0);\n"
        + "return i;\n"
        + "}";

    String roundFloor = "double roundFloor(double input){\n"
        + "double i = 0.0;\n"
        + "if((input >= 0.0 &amp;&amp; input < 1.0)){\n"
        + "return 0.0;\n"
        + "}\n"
        + "do {\n"
        + "i = i + 1.0;\n"
        + "} while(input - i > 1.0);\n"
        + "return i;"
        + "\n}";

    String bRoutine = "void blockRoutine(){ \n" + 
    "double input = #input#;\n" + 
        "double output;\n"
        + "bool isNegative = false;\n"
        + "double upperRange = #upperRange#;\n" 
        + "double lowerRange = #lowerRange#;\n"
        + "double rndMth = #rndMth#;\n" 
        + "if(input >= upperRange) {\n" 
        + "output = upperRange;\n"
        + "}\n" 
        + "if(input <= lowerRange) {\n" + "output = lowerRange;\n" 
        + "}\n"
        + "if(rndMth == 0) {\n"
        + "if(output >= 0.0){\n" 
        + "output = roundFloor(output);\n"
        + "} else {\n"
        + "isNegative = true;\n"
        + "output = roundCeil(output);\n"
        + "}\n" 
        + "}\n" + "if(rndMth == 1) {\n"
            + "if(output >= 0.0) {\n"
        + "output = roundCeil(output);\n"
        + "} else {\n"
        + "isNegative = true;\n"
        + "output = roundFloor(output);\n"
        + "}\n" 
        + "}\n"
            + "if(isNegative){\n"
            + "output = -1.0 * output;\n"
            + "}\n" 
        + "#outsignal# = output;\n" 
        + "}\n" 
        + "\n";
    String upperLimit = blockForParsing.getDeclaredParameter("UpperLimit");
    if (upperLimit == null || upperLimit.trim().equals("")) {
      upperLimit = "0.5";
    }
    String lowerLimit = blockForParsing.getDeclaredParameter("LowerLimit");
    if (lowerLimit == null || lowerLimit.trim().equals("")) {
      lowerLimit = "0.5";
    }
    String rndMeth = blockForParsing.getDeclaredParameter("RndMeth");
    if (rndMeth.trim().toLowerCase().equals("ceil")) {
      rndMeth = "1";
    } else {
      rndMeth = "0";
    }
    String input = "";
    Iterator iter = blockForParsing.getPredecessors().iterator();
    if (iter.hasNext()) {
      Neighbour pred = (Neighbour) iter.next();
      input = pred.getSimulinkBlock().getSignalName();
    }
    bRoutine = bRoutine.replaceAll("#input#", input)
        .replaceAll("#outsignal#", blockForParsing.getSignalName())
        .replaceAll("#upperRange#", upperLimit).replaceAll("#rndMth#", rndMeth)
        .replaceAll("#lowerRange#", lowerLimit);
    return String.format("%s\n%s\n%s", roundCeil, roundFloor, bRoutine);
  }

  @Override
  public String generateSignalDeclaration(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    return null;
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

  /* (non-Javadoc)
   * @see org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface#generateDafnyVerificationRoutine(org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper)
   */
  @Override
  public String generateDafnyVerificationRoutine(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    return null;
  }

}
