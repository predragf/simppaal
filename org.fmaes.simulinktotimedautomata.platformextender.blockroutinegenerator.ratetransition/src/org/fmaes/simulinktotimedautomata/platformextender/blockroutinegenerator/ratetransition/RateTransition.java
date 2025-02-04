package org.fmaes.simulinktotimedautomata.platformextender.blockroutinegenerator.ratetransition;

import static org.hamcrest.Matchers.stringContainsInOrder;

import org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface;
import org.fmaes.simulinktotimedautomata.types.wrappers.Neighbour;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;

public class RateTransition implements BlockRoutineGeneratorInterface {

  @Override
  public String generateBlockRoutine(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    String broutine = "void blockRoutine(){//I am from plugin\n}";
    int rtOperationType = determineOperationType(blockForParsing);
    // rtOperationType = OperationTypesEnum.UNITDELAY;

    if (rtOperationType == OperationTypesEnum.ERROR) {
      return "void blockRoutine(){//I am from plugin (ERRTYPE)\n}";
    }

    if (rtOperationType == OperationTypesEnum.COPY) {
      broutine = generateCopy(blockForParsing);
    }
    if (rtOperationType == OperationTypesEnum.BUFF) {
      broutine = generateBuff(blockForParsing);
    }
    if (rtOperationType == OperationTypesEnum.DB_BUFF) {
      broutine = generateDbBuff(blockForParsing);
    }
    if (rtOperationType == OperationTypesEnum.UNITDELAY) {
      broutine = generateUnitDelay(blockForParsing);
    }

    if (rtOperationType == OperationTypesEnum.ZOH) {
      broutine = generateZOH(blockForParsing);
    }


    return broutine;
  }

  @Override
  public String generateSignalDeclaration(SimulinkBlockWrapper blockForParsing) {
    // TODO Auto-generated method stub
    return null;
  }

  private int determineOperationType(SimulinkBlockWrapper blockForParsing) {

    int operationType = OperationTypesEnum.ERROR;
    boolean deterministic, dataIntegrity;
    String deterministicParam = blockForParsing.getDeclaredParameter("Deterministic");
    String dataIntegrityParam = blockForParsing.getDeclaredParameter("Integrity");
    deterministic =
        !(deterministicParam != null  && deterministicParam.trim().toLowerCase().equals("off"));
    dataIntegrity =
        !(dataIntegrityParam != null && dataIntegrityParam.trim().toLowerCase().equals("off"));

    PortData inportData = findInportData(blockForParsing);
    PortData outportData = findOutportData(blockForParsing);
    
    String outPortSampleTimeOpt = blockForParsing.getDeclaredParameter("OutPortSampleTimeOpt");

    if (outPortSampleTimeOpt.trim().equals("Multiple of input port sample time")) {
      String sampleMultiplierStr =
          blockForParsing.getDeclaredParameter("OutPortSampleTimeMultiple");
      try {
        int sampleMultiplier = Integer.parseInt(sampleMultiplierStr);
        outportData.ts = inportData.ts * sampleMultiplier;
      } catch (Exception e) {
        // TODO: handle exception
        outportData.ts = -1.0;
      }
    }
    if (inportData.ts == outportData.ts && inportData.offset == outportData.offset && dataIntegrity
        && deterministic) {
      operationType = OperationTypesEnum.COPY;
    }

    /* equal start */
    if (inportData.ts == outportData.ts && inportData.offset < outportData.offset && dataIntegrity
        && !deterministic) {
      operationType = OperationTypesEnum.BUFF;
    }

    if (inportData.ts == outportData.ts && inportData.offset == outportData.offset && dataIntegrity
        && !deterministic) {
      operationType = OperationTypesEnum.BUFF;
    }

    if (inportData.ts == outportData.ts && inportData.offset > outportData.offset && dataIntegrity
        && !deterministic) {
      operationType = OperationTypesEnum.BUFF;
    }

    /* equal end */

    /* fast to slow start */

    if (inportData.ts < outportData.ts && inportData.offset == 0 && outportData.offset == 0
        && outportData.ts % inportData.ts == 0 && dataIntegrity && deterministic) {
      operationType = OperationTypesEnum.ZOH;
    }

    if (inportData.ts < outportData.ts && inportData.offset == 0 && outportData.offset == 0
        && outportData.ts % inportData.ts == 0 && dataIntegrity && !deterministic) {
      operationType = OperationTypesEnum.BUFF;
    }

    if (inportData.ts < outportData.ts && inportData.offset <= outportData.offset
        && outportData.ts % inportData.ts == 0 && dataIntegrity && !deterministic) {
      operationType = OperationTypesEnum.BUFF;
    }

    if (inportData.ts < outportData.ts && inportData.offset > outportData.offset
        && outportData.ts % inportData.ts == 0 && dataIntegrity && !deterministic) {
      operationType = OperationTypesEnum.DB_BUFF;
    }

    if (inportData.ts < outportData.ts && outportData.ts % inportData.ts != 0 && dataIntegrity
        && !deterministic) {
      operationType = OperationTypesEnum.DB_BUFF;
    }

    /* fast to slow end */

    /* slow to fast start */

    if (inportData.ts > outportData.ts && dataIntegrity && deterministic && inportData.offset == 0
        && outportData.offset == 0) {
      operationType = OperationTypesEnum.UNITDELAY;
    }

    if (inportData.ts > outportData.ts && dataIntegrity && !deterministic) {
      operationType = OperationTypesEnum.DB_BUFF;
    }

    /* slow to fast end */


    /*
     * 
     * if ((deterministic || dataIntegrity) && (inportData.ts == outportData.ts) &&
     * (inportData.offset == outportData.offset)) { operationType = OperationTypesEnum.COPY; }
     * 
     * if (dataIntegrity && !deterministic && ((inportData.ts == outportData.ts) &&
     * (inportData.offset < outportData.offset) || (inportData.ts < outportData.ts &&
     * (((inportData.offset == 0.0 && outportData.offset == 0.0) && (inportData.ts > outportData.ts)
     * && ((outportData.ts % inportData.ts) == 0.0)) || ((inportData.offset <= outportData.offset)
     * && (inportData.ts > outportData.ts) && ((outportData.ts % inportData.ts) == 0.0)))))) {
     * operationType = OperationTypesEnum.BUFF; }
     * 
     * if ((dataIntegrity && !deterministic) && ((inportData.ts > outportData.ts) && dataIntegrity
     * && !deterministic) || ((inportData.ts < outportData.ts) && ((outportData.ts % inportData.ts)
     * != 0.0)) || ((inportData.ts < outportData.ts) && (inportData.offset > outportData.offset))) {
     * operationType = OperationTypesEnum.DB_BUFF; }
     * 
     * if (dataIntegrity && deterministic && (inportData.ts < outportData.ts) && (inportData.offset
     * == outportData.offset) && (inportData.offset == 0.0) && (outportData.ts % inportData.ts ==
     * 0.0)) { operationType = OperationTypesEnum.ZOH; } if (dataIntegrity && deterministic &&
     * (inportData.ts > outportData.ts) && (inportData.ts % outportData.ts == 0.0)) { operationType
     * = OperationTypesEnum.UNITDELAY; }
     */
    return operationType;
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

  private PortData findInportData(SimulinkBlockWrapper rt) {
    PortData inportData = new PortData();
    double inportSampleTime = -1;
    double inportOffset = 0;
    Neighbour predecessor = rt.getPredecessorAtPosition(0);
    if (predecessor.getSimulinkBlock().exists()) {
      String ts = predecessor.getSimulinkBlock().getSampleTime();
      String offset = predecessor.getSimulinkBlock().getDeclaredParameter("Offset");
      if (ts == null || ts.equals("") || ts.toLowerCase().equals("inf")) {
        inportSampleTime = Double.MAX_VALUE;
      } else {
        try {
          inportSampleTime = Double.parseDouble(ts);
        } catch (Exception e) {
          // TODO: handle exception
          inportSampleTime = -1;
        }
      }

      try {
        inportOffset = Double.parseDouble(offset);
      } catch (Exception e) {
        // TODO: handle exception
        inportOffset = 0;
      }
    }
    inportData.offset = inportOffset;
    inportData.ts = inportSampleTime;
    return inportData;
  }

  private PortData findOutportData(SimulinkBlockWrapper rt) {
    double outPortSampleTime = -1;
    double outportoffset = 0;
    int continuousPosition = -1;
    int index = 0;

    for (Neighbour successor : rt.getSuccessors()) {
      double currentSucTs = -1;
      if (successor.getSimulinkBlock().exists()) {
        String ts = successor.getSimulinkBlock().getSampleTime();
        if (ts == null || ts.equals("") || ts.toLowerCase().equals("inf")) {
          currentSucTs = Double.MAX_VALUE;
          continuousPosition = index;
        } else {
          try {
            outPortSampleTime = Double.parseDouble(ts);
          } catch (Exception e) {
            // TODO: handle exception
            currentSucTs = -1;
          }
        }
        if ((outPortSampleTime == -1 && currentSucTs != -1)
            || (outPortSampleTime != -1 && currentSucTs < outPortSampleTime)) {
          outPortSampleTime = currentSucTs;
          String offset = successor.getSimulinkBlock().getDeclaredParameter("Offset");
          try {
            outportoffset = Double.parseDouble(offset);
          } catch (Exception e) {
            // TODO: handle exception
            outportoffset = 0;
          }
        }
      }
      index++;
    }
    if (continuousPosition != -1 && outPortSampleTime == -1) {
      outPortSampleTime = Double.MAX_VALUE;
      Neighbour lastCont = rt.getSuccessorAtPosition(continuousPosition);
      String offsetStr = lastCont.getSimulinkBlock().getDeclaredParameter("Offset");
      try {
        outportoffset = Double.parseDouble(offsetStr);
      } catch (Exception e) {
        // TODO: handle exception
        outportoffset = 0;
      }
    }
    return new PortData(outPortSampleTime, outportoffset);
  }

  private String generateUnitDelay(SimulinkBlockWrapper rt) {
    String broutine = "void blockRoutine(){\n" + "buffer = #input#;\n" + "#output# = buffer;\n}";
    String output = rt.getSignalName();
    String input = "";
    Neighbour predecessor = rt.getPredecessorAtPosition(0);
    if (predecessor.getSimulinkBlock().exists()) {
      input = predecessor.getSimulinkBlock().getSignalName();
    }
    broutine = broutine.replaceAll("#input#", input).replaceAll("#output#", output);
    return broutine;
  }

  private String generateZOH(SimulinkBlockWrapper rt) {
    String broutine = "zoh";
    return broutine;
  }

  private String generateCopy(SimulinkBlockWrapper rt) {
    String outSignalName = rt.getSignalName();
    Neighbour predecessor = rt.getPredecessorAtPosition(0);
    String inputSignalName = "";
    if (predecessor.getSimulinkBlock().exists()) {
      inputSignalName = predecessor.getSimulinkBlock().getSignalName();
    }

    String broutine =
        String.format("void blockRoutine(){" + "%s = %s" + ";}", outSignalName, inputSignalName);
    return broutine;
  }

  private String generateBuff(SimulinkBlockWrapper rt) {
    String broutine = "buff";
    return broutine;
  }

  private String generateDbBuff(SimulinkBlockWrapper rt) {
    String broutine = "dbbuff";
    return broutine;
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
