package org.fmaes.simulinktotimedautomata.parsers;

import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkLineWrapper;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkModelWrapper;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkPortBaseWrapper;
import org.fmaes.simulinktotimedautomata.util.Util;

import java.util.ArrayList;
import java.util.Collection;

import org.conqat.lib.simulink.model.SimulinkPortBase;
import org.fmaes.simulinktotimedautomata.types.enums.SimulinkBlockTypesEnum;
import org.fmaes.simulinktotimedautomata.types.enums.SimulinkDeclaredParametersEnum;
import org.fmaes.simulinktotimedautomata.types.wrappers.Neighbour;
import org.fmaes.simulinktotimedautomata.types.wrappers.SimulinkBlockWrapper;

public class SimulinkLineParser {

  public static Collection<Neighbour> parseLine(SimulinkLineWrapper lineForParsing,
      String lastDemuxPort) {
    Collection<Neighbour> predecessors = new ArrayList<Neighbour>();
    SimulinkBlockWrapper lineSourceBlock = lineForParsing.getSourceBlock();
    SimulinkPortBaseWrapper lineSourcePort = lineForParsing.getSourcePort();
    Collection<SimulinkLineWrapper> linesForParsing = new ArrayList<SimulinkLineWrapper>();
    if (lineSourceBlock.isAtomic() && lineSourceBlock.isComputational()
        && !Util.matchStringsIgnoreCase(lineSourceBlock.getType(),
            SimulinkBlockTypesEnum.INPORT.toString())) {
      Neighbour predecessor = new Neighbour();
      predecessor.setSimulinkBlock(lineSourceBlock);
      predecessors.add(predecessor);
    } else if (!lineSourceBlock.isComputational()
        && lineSourceBlock.getType().toLowerCase().equals("mux")) {
      Collection<SimulinkLineWrapper> muxlines;
      if (Util.isNumber(lastDemuxPort)) {
        muxlines = lineSourceBlock.getIncomingLinesByPortIndex(lastDemuxPort);
      } else {
        muxlines = lineSourceBlock.getIncomingLines();
      }
      predecessors.addAll(parseLines(muxlines, ""));
    } else if (!lineSourceBlock.isComputational()
        && lineSourceBlock.getType().toLowerCase().equals("demux")) {
      String demuxPort = lineSourcePort.getPortIndex();
      predecessors.addAll(parseLines(lineSourceBlock.getIncomingLines(), demuxPort));
    } else if (Util.matchStringsIgnoreCase("from", lineSourceBlock.getType())) {
      SimulinkBlockWrapper matchingGoto = mapFromToGoto(lineSourceBlock);
      if (matchingGoto.exists()) {
        linesForParsing.addAll(matchingGoto.getIncomingLines());
      }
    } else if (Util.matchStringsIgnoreCase("goto", lineSourceBlock.getType())) {
      linesForParsing.addAll(lineSourceBlock.getIncomingLines());
    } else if (lineSourceBlock.isAtomic() && !lineSourceBlock.isComputational()
        && !Util.matchStringsIgnoreCase(lineSourceBlock.getType(),
            SimulinkBlockTypesEnum.INPORT.toString())) {
      linesForParsing.addAll(lineSourceBlock.getIncomingLines());
    } else if (lineSourceBlock.isAtomic() && lineSourceBlock.isComputational()
        && Util.matchStringsIgnoreCase(lineSourceBlock.getType(),
            SimulinkBlockTypesEnum.INPORT.toString())) {
      SimulinkBlockWrapper compositeParentBlock = lineSourceBlock.getParentSimulinkBlock();
      String portIndex =
          lineSourceBlock.getDeclaredParameter(SimulinkDeclaredParametersEnum.PORT.toString());
      portIndex = Util.convertToPortIndex(portIndex);
      /*
       * Dirty fix for inports on root model. if there are no input lines from the inport, then just
       * add the port
       */
      Collection<SimulinkLineWrapper> inportLines =
          compositeParentBlock.getIncomingLinesByPortIndex(portIndex);
      linesForParsing.addAll(inportLines);
      if (inportLines.size() == 0) {
        Neighbour pred = new Neighbour();
        pred.setSimulinkBlock(lineSourceBlock);
        pred.setToPort(lineSourcePort);
        predecessors.add(pred);
      }
    } else if (lineSourceBlock.getType().toLowerCase().equals("reference") && !Util
        .matchStringsIgnoreCase(lineSourceBlock.getDeclaredParameter("SourceType"), "subsystem")) {
      /* This case is masked block */
      Neighbour pred = new Neighbour();
      pred.setSimulinkBlock(lineSourceBlock);
      pred.setToPort(lineForParsing.getDestinationPort());
      predecessors.add(pred);
    } else if (lineSourceBlock.isSubsystem() || lineSourceBlock.isLibrary()) {
      String sourcePortIndex = lineSourcePort.getPortIndex();
      sourcePortIndex = Util.convertToPortIndex(sourcePortIndex);
      String _portType = SimulinkBlockTypesEnum.OUTPORT.toString().toLowerCase();
      SimulinkBlockWrapper internalPortBlock =
          lineSourceBlock.getInternalPortBlockByIndex(_portType, sourcePortIndex);
      linesForParsing.addAll(internalPortBlock.getIncomingLines());
    }
    predecessors.addAll(SimulinkLineParser.parseLines(linesForParsing, lastDemuxPort));
    return predecessors;
  }

  public static Collection<Neighbour> parseLineForward(SimulinkLineWrapper lineForParsing,
      String lastMuxPort) {
    Collection<Neighbour> successors = new ArrayList<Neighbour>();
    SimulinkBlockWrapper lineDestinationBlock = lineForParsing.getDestinationBlock();
    SimulinkPortBaseWrapper lineDestinationPort = lineForParsing.getDestinationPort();
    Collection<SimulinkLineWrapper> linesForParsing = new ArrayList<SimulinkLineWrapper>();
    if (lineDestinationBlock.isAtomic() && lineDestinationBlock.isComputational()
        && !Util.matchStringsIgnoreCase(lineDestinationBlock.getType(),
            SimulinkBlockTypesEnum.OUTPORT.toString())) {
      Neighbour successor = new Neighbour();
      successor.setSimulinkBlock(lineDestinationBlock);
      successors.add(successor);
    } else if (!lineDestinationBlock.isComputational()
        && lineDestinationBlock.getType().toLowerCase().equals("demux")) {
      Collection<SimulinkLineWrapper> demuxlines;
      if (Util.isNumber(lastMuxPort)) {
        demuxlines = lineDestinationBlock.getOutgoingLinesByPortIndex(lastMuxPort);
      } else {
        demuxlines = lineDestinationBlock.getOutgoingLines();
      }
      successors.addAll(parseLinesForward(demuxlines, ""));
    } else if (!lineDestinationBlock.isComputational()
        && lineDestinationBlock.getType().toLowerCase().equals("mux")) {
      String muxPort = lineDestinationPort.getPortIndex();
      successors.addAll(parseLinesForward(lineDestinationBlock.getOutgoingLines(), muxPort));
    } else if (Util.matchStringsIgnoreCase("goto", lineDestinationBlock.getType())) {
      SimulinkBlockWrapper matchingFrom = mapGotoToFrom(lineDestinationBlock);
      if (matchingFrom.exists()) {
        linesForParsing.addAll(matchingFrom.getOutgoingLines());
      }
    } else if (Util.matchStringsIgnoreCase("from", lineDestinationBlock.getType())) {
      linesForParsing.addAll(lineDestinationBlock.getOutgoingLines());
    } else if (lineDestinationBlock.isAtomic() && !lineDestinationBlock.isComputational()
        && !Util.matchStringsIgnoreCase(lineDestinationBlock.getType(),
            SimulinkBlockTypesEnum.OUTPORT.toString())) {
      linesForParsing.addAll(lineDestinationBlock.getOutgoingLines());
    } else if (lineDestinationBlock.isAtomic() && lineDestinationBlock.isComputational()
        && Util.matchStringsIgnoreCase(lineDestinationBlock.getType(),
            SimulinkBlockTypesEnum.OUTPORT.toString())) {
      SimulinkBlockWrapper compositeParentBlock = lineDestinationBlock.getParentSimulinkBlock();
      String portIndex =
          lineDestinationBlock.getDeclaredParameter(SimulinkDeclaredParametersEnum.PORT.toString());
      portIndex = Util.convertToPortIndex(portIndex);
      /*
       * Dirty fix for inports on root model. if there are no input lines from the inport, then just
       * add the port
       */
      Collection<SimulinkLineWrapper> outPortLines =
          compositeParentBlock.getOutgoingLinesByPortIndex(portIndex);
      linesForParsing.addAll(outPortLines);
      if (outPortLines.size() == 0) {
        Neighbour successor = new Neighbour();
        successor.setSimulinkBlock(lineDestinationBlock);
        successor.setToPort(lineDestinationPort);
        successors.add(successor);
      }
    } else if (lineDestinationBlock.getType().toLowerCase().equals("reference")
        && !Util.matchStringsIgnoreCase(lineDestinationBlock.getDeclaredParameter("SourceType"),
            "subsystem")) {
      /* This case is masked block */
      Neighbour successor = new Neighbour();
      successor.setSimulinkBlock(lineDestinationBlock);
      successor.setToPort(lineForParsing.getDestinationPort());
      successors.add(successor);
    } else if (lineDestinationBlock.isSubsystem() || lineDestinationBlock.isLibrary()) {
      String sourcePortIndex = lineDestinationPort.getPortIndex();
      sourcePortIndex = Util.convertToPortIndex(sourcePortIndex);
      String _portType = SimulinkBlockTypesEnum.INPORT.toString().toLowerCase();
      SimulinkBlockWrapper internalPortBlock =
          lineDestinationBlock.getInternalPortBlockByIndex(_portType, sourcePortIndex);
      linesForParsing.addAll(internalPortBlock.getOutgoingLines());
    }
    successors.addAll(SimulinkLineParser.parseLinesForward(linesForParsing, lastMuxPort));
    return successors;
  }

  public static Collection<Neighbour> parseLines(Collection<SimulinkLineWrapper> linesForParsing,
      String startPortIndex) {
    Collection<Neighbour> predecessors = new ArrayList<Neighbour>();
    for (SimulinkLineWrapper lineForParsing : linesForParsing) {
      predecessors.addAll(parseLine(lineForParsing, startPortIndex));
    }
    return predecessors;
  }

  public static Collection<Neighbour> parseLinesForward(
      Collection<SimulinkLineWrapper> linesForParsing, String startPortIndex) {
    Collection<Neighbour> successors = new ArrayList<Neighbour>();
    for (SimulinkLineWrapper lineForParsing : linesForParsing) {
      successors.addAll(parseLineForward(lineForParsing, startPortIndex));
    }
    return successors;
  }

  public static SimulinkBlockWrapper mapFromToGoto(SimulinkBlockWrapper fromBlock) {
    SimulinkModelWrapper currentModel = fromBlock.getSimulinkModelWrapped();
    String gotoTag = fromBlock.getParameter("GotoTag");
    Collection<SimulinkBlockWrapper> allGotTos = currentModel.getChildBlocksByType("goto");
    SimulinkBlockWrapper matchingGoto = new SimulinkBlockWrapper(null);
    for (SimulinkBlockWrapper gotoBlock : allGotTos) {
      if (gotoBlock.exists()
          && Util.matchStringsIgnoreCase(gotoTag, gotoBlock.getDeclaredParameter("GotoTag"))) {
        matchingGoto = gotoBlock;
        break;
      }
    }
    return matchingGoto;
  }

  public static SimulinkBlockWrapper mapGotoToFrom(SimulinkBlockWrapper gotoBlock) {
    SimulinkModelWrapper currentModel = gotoBlock.getSimulinkModelWrapped();
    String gotoTag = gotoBlock.getParameter("GotoTag");
    Collection<SimulinkBlockWrapper> allFroms = currentModel.getChildBlocksByType("from");
    SimulinkBlockWrapper matchingFrom = new SimulinkBlockWrapper(null);
    for (SimulinkBlockWrapper fromBlock : allFroms) {
      if (fromBlock.exists()
          && Util.matchStringsIgnoreCase(gotoTag, fromBlock.getDeclaredParameter("GotoTag"))) {
        matchingFrom = fromBlock;
        break;
      }
    }
    return matchingFrom;
  }
}
