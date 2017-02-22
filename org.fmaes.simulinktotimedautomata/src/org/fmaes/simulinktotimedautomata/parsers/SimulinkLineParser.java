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
    String lineSourceBlockType = lineSourceBlock.getType();
    Collection<SimulinkLineWrapper> linesForParsing = new ArrayList<SimulinkLineWrapper>();
    /* if the block is atomic */
    if (lineSourceBlock.isAtomic()) {
      /* if it is computational (add it as a predecessor) */
      if (lineSourceBlock.isComputational()) {
        Neighbour predecessor = new Neighbour();
        predecessor.setSimulinkBlock(lineSourceBlock);
        predecessors.add(predecessor);
      }
      /* if it is not computational, check the type and determine what to do */
      else {
        if (Util.matchStringsIgnoreCase("mux", lineSourceBlockType)) {
          Collection<SimulinkLineWrapper> muxlines;
          if (Util.isNumber(lastDemuxPort)) {
            muxlines = lineSourceBlock.getIncomingLinesByPortIndex(lastDemuxPort);
          } else {
            muxlines = lineSourceBlock.getIncomingLines();
          }
          predecessors.addAll(parseLines(muxlines, ""));
        }

        else if (Util.matchStringsIgnoreCase("demux", lineSourceBlockType)) {
          String demuxPort = lineSourcePort.getPortIndex();
          predecessors.addAll(parseLines(lineSourceBlock.getIncomingLines(), demuxPort));
        }

        else if (Util.matchStringsIgnoreCase("from", lineSourceBlockType)) {
          SimulinkBlockWrapper matchingGoto = mapFromToGoto(lineSourceBlock);
          if (matchingGoto.exists()) {
            linesForParsing.addAll(matchingGoto.getIncomingLines());
          }
        } else if (Util.matchStringsIgnoreCase("inport", lineSourceBlockType)) {
          SimulinkBlockWrapper compositeParentBlock = lineSourceBlock.getParentSimulinkBlock();
          String portIndex =
              lineSourceBlock.getDeclaredParameter(SimulinkDeclaredParametersEnum.PORT.toString());
          portIndex = Util.convertToPortIndex(portIndex);
          /*
           * Dirty fix for inports on root model. if there are no input lines from the inport, then
           * just add the port
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
        } else {
          linesForParsing.addAll(lineSourceBlock.getIncomingLines());
        }
      }

    }
    /* if the block is composite (subsystem, reference, etc.) */
    else {

      if (lineSourceBlock.getType().toLowerCase().equals("reference")
          && !Util.matchStringsIgnoreCase(lineSourceBlock.getDeclaredParameter("SourceType"),
              "subsystem")) {
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
    String lineDestinationBlockType = lineDestinationBlock.getType();

    /* part for atomic simulink blocks */
    if (lineDestinationBlock.isAtomic()) {
      // if the atomic block is computational
      if (lineDestinationBlock.isComputational()) {
        Neighbour successor = new Neighbour();
        successor.setSimulinkBlock(lineDestinationBlock);
        successors.add(successor);
      }
      // if the atomic block is not computational
      else {
        // if the destination block is demux
        if (Util.matchStringsIgnoreCase("demux", lineDestinationBlockType)) {
          Collection<SimulinkLineWrapper> demuxlines;
          if (Util.isNumber(lastMuxPort)) {
            demuxlines = lineDestinationBlock.getOutgoingLinesByPortIndex(lastMuxPort);
          } else {
            demuxlines = lineDestinationBlock.getOutgoingLines();
          }
          successors.addAll(parseLinesForward(demuxlines, ""));
        }
        // if the destination block is mux
        else if (Util.matchStringsIgnoreCase("mux", lineDestinationBlockType)) {
          String muxPort = lineDestinationPort.getPortIndex();
          successors.addAll(parseLinesForward(lineDestinationBlock.getOutgoingLines(), muxPort));
        }
        // goto block
        else if (Util.matchStringsIgnoreCase("goto", lineDestinationBlockType)) {
          SimulinkBlockWrapper matchingFrom = mapGotoToFrom(lineDestinationBlock);
          if (matchingFrom.exists()) {
            linesForParsing.addAll(matchingFrom.getOutgoingLines());
          }
        }
        // from block
        else if (Util.matchStringsIgnoreCase("from", lineDestinationBlock.getType())) {
          linesForParsing.addAll(lineDestinationBlock.getOutgoingLines());
        } else if (Util.matchStringsIgnoreCase(lineDestinationBlockType,
            SimulinkBlockTypesEnum.OUTPORT.toString())) {
          SimulinkBlockWrapper compositeParentBlock = lineDestinationBlock.getParentSimulinkBlock();
          String portIndex = lineDestinationBlock
              .getDeclaredParameter(SimulinkDeclaredParametersEnum.PORT.toString());
          portIndex = Util.convertToPortIndex(portIndex);
          /*
           * Dirty fix for inports on root model. if there are no input lines from the inport, then
           * just add the port
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
        }
        // base case: for all non computational blocks that do not have special treatment, ignore
        // them, that is, their successors become the successors of the block.
        else {
          linesForParsing.addAll(lineDestinationBlock.getOutgoingLines());
        }
      }

    }
    // Part for the composite blocks
    else {

      if (Util.matchStringsIgnoreCase("reference", lineDestinationBlockType)
          && !Util.matchStringsIgnoreCase(lineDestinationBlock.getDeclaredParameter("SourceType"),
              "subsystem")) {
        /* This case is masked block - do not open it, just treat it as a neighbour */
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
