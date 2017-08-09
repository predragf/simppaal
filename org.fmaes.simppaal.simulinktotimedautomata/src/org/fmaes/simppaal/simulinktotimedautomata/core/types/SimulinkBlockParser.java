/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.types;

import java.util.Collection;

import org.conqat.lib.simulink.model.SimulinkBlock;
import org.conqat.lib.simulink.model.SimulinkInPort;
import org.conqat.lib.simulink.model.SimulinkLine;
import org.conqat.lib.simulink.model.SimulinkPortBase;
import org.conqat.lib.simulink.model.datahandler.LabelLayoutData;
import org.fmaes.simppaal.simulinktotimedautomata.utils.SimulinkUtils;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
public class SimulinkBlockParser {

  public static SimulinkBlockWrapper mapToOutportBlock(SimulinkPortBase outPort) {
    SimulinkBlockWrapper matchingInPort = new SimulinkBlockWrapper(null);
    SimulinkBlock baseCompositeBlock = outPort.getBlock();
    LabelLayoutData labelData = outPort.obtainLabelData();
    Collection<SimulinkBlock> subBlocks;
    subBlocks = baseCompositeBlock.getSubBlocks();
    String portName = labelData != null ? labelData.getText() : "";

    for (SimulinkBlock subBlock : subBlocks) {
      if (subBlock.getName().equals(portName)
          && SimulinkUtils.compareStringsIgnoreCase(subBlock.getType(), "outport")) {
        matchingInPort = new SimulinkBlockWrapper(subBlock);
        break;
      }
    }

    return matchingInPort;
  }

  public static SimulinkPortBase mapToInputPort(SimulinkBlockWrapper inportBlock) {
    final String requestedPortName = inportBlock.getName();
    SimulinkBlockWrapper subSystem = inportBlock.getParent();
    String portName;
    SimulinkPortBase requestedPort = null;

    // the matching is based on the port label
    for (SimulinkPortBase inPort : subSystem.getInPorts()) {
      portName = SimulinkUtils.getNameFromLabel(inPort);
      if (SimulinkUtils.compareStringsIgnoreCase(portName, requestedPortName)) {
        requestedPort = inPort;
        break;
      }
    }

    return requestedPort;
  }

  public static String extractSampleTime(SimulinkPortBase triggeredPort) {
    String sTime = "";
    SimulinkInPort inPort = (SimulinkInPort) triggeredPort;
    SimulinkLine inline = inPort.getLine();
    SimulinkBlock trigger = inline.getSrcPort().getBlock();

    if (trigger != null) {
      sTime = trigger.getParameter("SampleTime");
      if (sTime == null || sTime.equals("")) {
        sTime = trigger.getDeclaredParameter("SampleTime");
      }
    }
    return sTime;
  }
}
