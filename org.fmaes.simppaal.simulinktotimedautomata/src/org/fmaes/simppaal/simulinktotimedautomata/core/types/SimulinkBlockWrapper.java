/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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
public class SimulinkBlockWrapper {

  private SimulinkBlock simulinkBlock;

  private Collection<Neighbour> predecessors;

  private int executionOrderNumber;

  private final String[] compositeBlockTypes =
      {"subsystem", "reference", "modelreference", "model"};

  private final String[] nonComputationalBlockTypes = {"mux", "demux", "goto", "from", "inport",
      "outport", "function-call generator", "ratetransition"};

  /**
   * @param simulinkBlock
   */
  public SimulinkBlockWrapper(SimulinkBlock _simulinkBlock) {
    simulinkBlock = _simulinkBlock;
    predecessors = null;
  }

  public String getType() {
    return simulinkBlock.getType();
  }

  public String getParameter(String parameterName) {
    return simulinkBlock.getParameter(parameterName);
  }

  public void setParameter(String parameterName, String parameterValue) {
    simulinkBlock.setParameter(parameterName, parameterValue);
  }

  public Collection<String> getDeclaredParameters() {
    return simulinkBlock.getDeclaredParameterNames();
  }

  public String getId() {
    return simulinkBlock.getId();
  }

  public String getName() {
    LabelLayoutData lld = this.simulinkBlock.obtainLabelData();

    String blockName = "";
    if (lld != null) {
      blockName = lld.getText();
    }

    return blockName;
  }

  public String getNameNoWhiteSpaces() {
    String originalName = getName();
    /*
     * in uppaal nothing should start with number. this is for such blocks, so we put underscore at
     * the beginning
     */
    if (originalName.length() > 0 && Character.isDigit(originalName.charAt(0))) {
      originalName = "_" + originalName;
    }
    return originalName.replaceAll("[^a-zA-Z\\d:]", "_").toLowerCase().trim();
  }

  public boolean exists() {
    return simulinkBlock != null;
  }

  public boolean isAtomic() {
    boolean isAtomic = true;

    for (String compositeBlockType : compositeBlockTypes) {
      if (this.getType().toLowerCase().equals(compositeBlockType)) {
        isAtomic = false;
        break;
      }
    }

    return isAtomic;
  }

  public boolean isComputational() {
    boolean isComputational = true;

    for (String nonComputationalBlockType : nonComputationalBlockTypes) {
      if (this.getType().toLowerCase().equals(nonComputationalBlockType)) {
        isComputational = false;
        break;
      }
    }

    return isComputational;
  }

  public SimulinkBlockWrapper getParent() {
    SimulinkBlock parent = null;
    if (simulinkBlock != null) {
      parent = simulinkBlock.getParent();
    }
    return new SimulinkBlockWrapper(parent);
  }

  public Collection<SimulinkPortBase> getInPorts() {
    Collection<SimulinkPortBase> inports = new ArrayList<>();

    inports.addAll(simulinkBlock.getInPorts());

    return inports;
  }

  public Collection<SimulinkPortBase> getOutPorts() {
    Collection<SimulinkPortBase> outports = new ArrayList<>();

    outports.addAll(simulinkBlock.getOutPorts());

    return outports;
  }

  public Collection<SimulinkPortBase> getTriggeredPorts() {
    Collection<SimulinkPortBase> triggeredPorts = new ArrayList<>();

    for (SimulinkPortBase simulinkPortBase : getInPorts()) {
      if (simulinkPortBase instanceof SimulinkInPort) {
        SimulinkInPort inPort = (SimulinkInPort) simulinkPortBase;
        if (inPort.isTriggerPort()) {
          triggeredPorts.add(inPort);
        }
      }
    }

    return triggeredPorts;
  }

  public Collection<SimulinkLine> getInLines() {
    return this.simulinkBlock.getInLines();
  }

  public Collection<SimulinkLine> getInLinesByPortIndex(String portIndex) {
    Collection<SimulinkLine> inLinesByPort = new ArrayList<>();

    for (SimulinkLine inLine : this.getInLines()) {
      if (inLine.getDstPort().getIndex().equals(portIndex)) {
        inLinesByPort.add(inLine);
      }
    }

    return inLinesByPort;
  }

  public Collection<SimulinkLine> getInLinesByPort(SimulinkPortBase inPort) {
    return getInLinesByPortIndex(inPort.getIndex());
  }

  private Collection<SimulinkLine> getSubSystemLinesForParsing(Neighbour node) {
    Collection<SimulinkLine> inLines = new ArrayList<>();
    SimulinkPortBase subSystemPort = node.getSourcePort();

    SimulinkBlockWrapper outPortInSubSystem = SimulinkBlockParser.mapToOutportBlock(subSystemPort);
    if (outPortInSubSystem.exists()) {
      inLines = outPortInSubSystem.getInLines();
    }

    return inLines;
  }

  private Collection<SimulinkLine> getInportLinesForParsing(Neighbour node) {
    SimulinkBlockWrapper inportBlock = node.getSourceSimulinkBlock();
    SimulinkBlockWrapper subSystem = inportBlock.getParent();
    SimulinkPortBase inPortForParsing = SimulinkBlockParser.mapToInputPort(inportBlock);

    Collection<SimulinkLine> inlines = new ArrayList<>();

    if (subSystem.exists()) {
      inlines = subSystem.getInLinesByPortIndex(inPortForParsing.getIndex());
    }

    return inlines;
  }

  private Collection<SimulinkLine> getMuxLinesForParsing(Neighbour node) {
    Collection<SimulinkLine> inlines = new ArrayList<>();

    SimulinkPortBase lastDemuxPort = node.getIntermediateDestinationPort();
    if (lastDemuxPort != null) {
      SimulinkBlockWrapper mux = node.getSourceSimulinkBlock();
      inlines = mux.getInLinesByPortIndex(lastDemuxPort.getIndex());
      node.setIntermediateDestinationPort(null);
    }

    return inlines;
  }

  private Collection<SimulinkLine> getDeMuxLinesForParsing(Neighbour node) {
    SimulinkBlockWrapper demux = node.getSourceSimulinkBlock();
    Collection<SimulinkLine> inlines = new ArrayList<>();

    if (demux.exists()) {
      if (node.getIntermediateDestinationPort() != null) {
        inlines = demux.getInLinesByPortIndex(node.getSourcePort().getIndex());
      } else {
        inlines = demux.getInLines();
      }
    }

    return inlines;
  }

  private Collection<SimulinkLine> getFromLinesForParsing(Neighbour node) {
    SimulinkModelWrapper model = new SimulinkModelWrapper(simulinkBlock.getModel());
    Collection<SimulinkLine> lines = new ArrayList<SimulinkLine>();
    String gotoTag = this.getParameter("GotoTag");

    for (SimulinkBlockWrapper gBlock : model.getSubBlocksRecursivelyByType("goto")) {
      if (gBlock.exists()
          && SimulinkUtils.compareStringsIgnoreCase(gotoTag, gBlock.getParameter("GotoTag"))) {
        lines = gBlock.getInLines();
        break;
      }
    }

    return lines;
  }

  private Collection<SimulinkLine> getLinesForParsing(Neighbour _node) {
    Collection<SimulinkLine> inLines;
    SimulinkBlockWrapper blk = _node.getSourceSimulinkBlock();
    String blkType = blk.getType();

    switch (blkType.toLowerCase()) {
      case "subsystem":
        inLines = getSubSystemLinesForParsing(_node);
        break;
      case "inport":
        inLines = getInportLinesForParsing(_node);
        break;
      case "mux":
        // here make the adjustment for the node
        inLines = getMuxLinesForParsing(_node);
        break;
      case "demux":
        // here make the adjustment for the node
        inLines = getDeMuxLinesForParsing(_node);
        break;
      case "from":
        inLines = getFromLinesForParsing(_node);
        break;
      case "goto":
        inLines = blk.getInLines();
        break;
      case "ratetransition":
        inLines = blk.getInLines();
        break;
      default:
        inLines = new ArrayList<>();
        break;
    }

    return inLines;
  }

  private Collection<Neighbour> getPredecessorsRecursively(Neighbour _curentNode) {
    Collection<Neighbour> _predecessors = new ArrayList<>();
    Collection<SimulinkLine> inLines = getLinesForParsing(_curentNode);

    for (SimulinkLine inLine : inLines) {
      Neighbour newNeighbour = _curentNode.clone(inLine);
      _predecessors.addAll(parseNeighbour(newNeighbour));
    }

    return _predecessors;
  }

  private Collection<Neighbour> parseNeighbour(Neighbour _neighbour) {
    Collection<Neighbour> _predecessors = new ArrayList<>();
    SimulinkBlockWrapper _predecessorBlock = _neighbour.getSourceSimulinkBlock();

    if (_predecessorBlock.isAtomic() && _predecessorBlock.isComputational()) {
      _predecessors.add(_neighbour);
    } else {
      _predecessors = getPredecessorsRecursively(_neighbour);
    }

    return _predecessors;
  }

  private Collection<Neighbour> computePredecessors() {
    Collection<Neighbour> _predecessors = new ArrayList<>();

    Collection<SimulinkLine> incomingLines = getInLines();
    for (SimulinkLine inLine : incomingLines) {
      Neighbour _currentNeighbour = new Neighbour(inLine);
      _predecessors.addAll(parseNeighbour(_currentNeighbour));
    }

    return _predecessors;
  }

  public Collection<Neighbour> getPredecessors() {

    // if the cache is null, means that the predecessors have not
    // been computed so far
    if (predecessors == null) {
      // populate the cache
      predecessors = computePredecessors();
    }

    // always return from the cache
    return predecessors;
  }

  public int getExecutionOrderNumber() {
    return executionOrderNumber;
  }

  public void setExecutionOrderNumber(int executionOrderNumber) {
    this.executionOrderNumber = executionOrderNumber;
  }

  private String getSampleTimeFromTrigger(Collection<SimulinkPortBase> triggeredPorts) {
    String sTime = "";

    for (SimulinkPortBase triggeredPort : triggeredPorts) {
      sTime = SimulinkBlockParser.extractSampleTime(triggeredPort);
    }

    return sTime;
  }

  private String determineSampleTime() {
    String sTime = getParameter("SampleTime");

    if (sTime == null || sTime.equals("") || sTime.equals("-1")) {
      Collection<SimulinkPortBase> triggeredPorts = getTriggeredPorts();
      if (triggeredPorts.size() < 1) {
        SimulinkBlockWrapper parent = getParent();
        if (parent.exists()) {
          sTime = parent.getSampleTime();
        }
      } else {
        sTime = getSampleTimeFromTrigger(triggeredPorts);
      }
    }

    return sTime;
  }

  public String getSampleTime() {
    return determineSampleTime();
  }

  public SimulinkBlock getBaseBlock() {
    return simulinkBlock;
  }

  public String getSignalName() {
    return String.format("%s_%d_signal", getNameNoWhiteSpaces(), getExecutionOrderNumber());
  }

  public ArrayList<Neighbour> getPredecessorsAsList() {
    ArrayList<Neighbour> _predecessors = new ArrayList<Neighbour>();
    _predecessors.addAll(getPredecessors());
    return _predecessors;
  }

  public ArrayList<Neighbour> generateDependencyChain() {
    HashMap<String, Neighbour> dependencyChainMap = new HashMap<String, Neighbour>();
    ArrayList<Neighbour> predecessors = new ArrayList<Neighbour>();

    predecessors.addAll(this.getPredecessors());
    dependencyChainMap = computeDependencyChain(dependencyChainMap, predecessors);

    ArrayList<Neighbour> dependencyChain = new ArrayList<>();
    dependencyChain.addAll(dependencyChainMap.values());

    return dependencyChain;
  }

  private HashMap<String, Neighbour> computeDependencyChain(
      HashMap<String, Neighbour> dependencyChain, ArrayList<Neighbour> blocksForProcessing) {
    Neighbour neighbourForParsing;
    SimulinkBlockWrapper neighbourSimulinkBlockWrapper;
    while (!blocksForProcessing.isEmpty()) {
      neighbourForParsing = blocksForProcessing.remove(0);
      neighbourSimulinkBlockWrapper = neighbourForParsing.getSourceSimulinkBlock();
      if (dependencyChain.get(neighbourSimulinkBlockWrapper.getId()) != null) {
        continue;
      }

      dependencyChain.put(neighbourSimulinkBlockWrapper.getId(), neighbourForParsing);
      blocksForProcessing.addAll(neighbourSimulinkBlockWrapper.getPredecessors());
    }
    return dependencyChain;
  }

  private boolean hasBeenProcessed(ArrayList<Neighbour> dependencyChain, Neighbour forParsing) {
    String blockForParsingID = forParsing.getSourceSimulinkBlock().getId();
    boolean hasBeenProcessed = false;
    for (Neighbour neighbour : dependencyChain) {
      if (neighbour.getSourceSimulinkBlock().exists()
          && neighbour.getSourceSimulinkBlock().getId().equals(blockForParsingID)) {
        hasBeenProcessed = true;
        break;
      }
    }
    return hasBeenProcessed;
  }
}
