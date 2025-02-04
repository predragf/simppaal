package org.fmaes.simulinktotimedautomata.platformextender.blockroutinegenerator.saturation;

import java.util.Iterator;

import org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface;
import org.fmaes.simulinktotimedautomata.types.wrappers.Neighbour;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;

public class Saturation implements BlockRoutineGeneratorInterface {

  @Override
  public String generateBlockRoutine(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    String roundCeil = "double roundCeil(double input){\n" + "double i = 0.0;\n"
        + "if(input > -1.0 &amp;&amp; input <= 0.0){\n" + "return 0.0;\n" + "}\n" + "do {\n"
        + "i = i + 1.0;\n" + "} while(input - i > 0.0);\n" + "return i;\n" + "}";

    String roundFloor = "double roundFloor(double input){\n" + "double i = 0.0;\n"
        + "if((input >= 0.0 &amp;&amp; input < 1.0)){\n" + "return 0.0;\n" + "}\n" + "do {\n"
        + "i = i + 1.0;\n" + "} while(input - i >= 1.0);\n" + "return i;" + "\n}";

    String bRoutine =
        "void blockRoutine(){ \n" + "double output = #input#;\n" + "bool isNegative = false;\n"
            + "double upperRange = #upperRange#;\n" + "double lowerRange = #lowerRange#;\n"
            + "int rndMth = #rndMth#;\n" + "if(output >= upperRange) {\n" + "output = upperRange;\n"
            + "}\n" + "if(output <= lowerRange) {\n" + "output = lowerRange;\n" + "}\n"
            + "if(rndMth == 0) {\n" + "if(output >= 0.0){\n" + "output = roundFloor(output);\n"
            + "} else {\n" + "isNegative = true;\n" + "output = output * -1.0;\n"
            + "output = roundCeil(output);\n" + "}\n" + "}\n" + "if(rndMth == 1) {\n"
            + "if(output >= 0.0) {\n" + "output = roundCeil(output);\n" + "} else {\n"
            + "isNegative = true;\n" + "output = output * -1.0;\n"
            + "output = roundFloor(output);\n" + "}\n" + "}\n" + "if(isNegative){\n"
            + "output = -1.0 * output;\n" + "}\n" + "#outsignal# = output;\n" + "}\n" + "\n";
    String upperLimit = blockForParsing.getDeclaredParameter("UpperLimit");
    upperLimit = makeItDouble(upperLimit);
    if (upperLimit == null || upperLimit.trim().equals("")) {
      /* 0.5 is the default value */
      upperLimit = "0.5";
    }
    String lowerLimit = blockForParsing.getDeclaredParameter("LowerLimit");
    lowerLimit = makeItDouble(lowerLimit);
    if (lowerLimit == null || lowerLimit.trim().equals("")) {
      /*-0.5 is the default value*/
      lowerLimit = "-0.5";
    }
    String rndMeth = blockForParsing.getDeclaredParameter("RndMeth");
    int _rndMtd = -1;
    if (rndMeth.trim().toLowerCase().equals("ceil")) {
      _rndMtd = 1;
    } else if (rndMeth.trim().toLowerCase().contains("floor")) {
      _rndMtd = 0;
    }
    /* 0.5 is the default value */
    String input = "";
    Iterator iter = blockForParsing.getPredecessors().iterator();
    if (iter.hasNext()) {
      Neighbour pred = (Neighbour) iter.next();
      input = pred.getSimulinkBlock().getSignalName();
    }
    bRoutine = bRoutine.replaceAll("#input#", input)
        .replaceAll("#outsignal#", blockForParsing.getSignalName())
        .replaceAll("#upperRange#", upperLimit).replaceAll("#rndMth#", String.format("%d", _rndMtd))
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

  private String makeItDouble(String input) {
    String dValue = input;
    try {
      Double d = Double.parseDouble(input);
      dValue = String.format("%f", d);
    } catch (Exception e) {
      dValue = input;
    }
    return dValue;
  }

}
