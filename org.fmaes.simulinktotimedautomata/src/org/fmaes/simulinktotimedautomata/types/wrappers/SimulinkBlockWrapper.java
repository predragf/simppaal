package org.fmaes.simulinktotimedautomata.types.wrappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.conqat.lib.simulink.model.ReferencedBlockInfo;
import org.conqat.lib.simulink.model.SimulinkBlock;
import org.conqat.lib.simulink.model.SimulinkInPort;
import org.conqat.lib.simulink.model.SimulinkLine;
import org.conqat.lib.simulink.model.SimulinkPortBase;
import org.fmaes.simulinktotimedautomata.builders.SimulinkModelBuilder;
import org.fmaes.simulinktotimedautomata.configuration.ApplicationConfiguration;
import org.fmaes.simulinktotimedautomata.types.enums.SimulinkBlockTypesEnum;
import org.fmaes.simulinktotimedautomata.types.enums.SimulinkDeclaredParametersEnum;
import org.fmaes.simulinktotimedautomata.types.enums.SimulinkParametersEnum;
import org.fmaes.simulinktotimedautomata.util.SerializableHashTable;
import org.fmaes.simulinktotimedautomata.util.Util;

public class SimulinkBlockWrapper {

  private SimulinkBlock simulinkBlock;

  public int executionOrderNumber;

  private Collection<Neighbour> predecessors;

  private Collection<Neighbour> successors;

  @SuppressWarnings("unused")
  private SimulinkBlockWrapper() {}

  public SimulinkBlockWrapper(SimulinkBlock _simulinkBlock) {
    simulinkBlock = _simulinkBlock;
    executionOrderNumber = -1;
    predecessors = new ArrayList<Neighbour>();
    successors = new ArrayList<Neighbour>();
  }

  public Collection<Neighbour> getPredecessors() {
    return predecessors;
  }

  public Neighbour getPredecessorAtPosition(int position) {
    int index = 0;
    Neighbour predecessor = new Neighbour();
    Iterator<Neighbour> iter = predecessors.iterator();
    while (iter.hasNext()) {
      if (index == position) {
        predecessor = iter.next();
        break;
      }
      index++;
    }
    return predecessor;
  }

  public Neighbour getSuccessorAtPosition(int position) {
    int index = 0;
    Neighbour successor = new Neighbour();
    Iterator<Neighbour> iter = successors.iterator();
    while (iter.hasNext()) {
      if (index == position) {
        successor = iter.next();
        break;
      }
      index++;
    }
    return successor;
  }

  public void setPredecessors(Collection<Neighbour> _predecessors) {
    predecessors = _predecessors;
  }

  public Collection<Neighbour> getSuccessors() {
    return successors;
  }

  public void setSuccessors(Collection<Neighbour> _successors) {
    successors = _successors;
  }

  public SimulinkBlockWrapper(SimulinkBlock _simulinkBlock, int _executionNumber,
      List<Neighbour> _predecessors) {
    simulinkBlock = _simulinkBlock;
    executionOrderNumber = _executionNumber;
    predecessors = _predecessors;
  }

  public Boolean isLibrary() {
    String referenceType = SimulinkBlockTypesEnum.REFERENCE.toString();
    String modelReferenceType = SimulinkBlockTypesEnum.MODELREFERENCE.toString();
    String blockType = simulinkBlock.getType();
    return (Util.matchStringsIgnoreCase(blockType, referenceType)
        || Util.matchStringsIgnoreCase(blockType, modelReferenceType));
  }

  public Boolean isSubsystem() {
    String subSystemType = SimulinkBlockTypesEnum.SUBSYSTEM.toString();
    String blockType = simulinkBlock.getType();
    return Util.matchStringsIgnoreCase(subSystemType, blockType);
  }

  public Boolean isAtomic() {
    /* maybe a better definition of atomic element is required */
    return !isLibrary() && !isSubsystem();
  }

  public Boolean isComputational() {
    /*
     * demux is a standard non-computational block since it has only one output
     */
    ApplicationConfiguration appConfig = ApplicationConfiguration.loadConfiguration();
    String nonComputationalTypesFromConfig = appConfig.getProperty("nonComputationalBlocks");
    String[] defaultConfiguration = {"demux", "mux", "goto", "from", "inport", "outport"};
    String[] nonComputationalTypes = {};
    Boolean isComputational = true;
    if (!Util.stringNullOrEmpty(nonComputationalTypesFromConfig)) {
      nonComputationalTypes = nonComputationalTypesFromConfig.split(";");
    }
    if (nonComputationalTypes.length == 0) {
      nonComputationalTypes = defaultConfiguration;
    }
    for (String nonComputationalType : nonComputationalTypes) {
      if (Util.matchStringsIgnoreCase(nonComputationalType, getType())) {
        isComputational = false;
        break;
      }
    }
    return isComputational;
  }

  public Boolean isRootOfALibrary() {
    Boolean isRootOfALibrary = false;
    SimulinkModelWrapper parentModel = this.getSimulinkModelWrapped();
    if (parentModel.isReferenced()) {
      SimulinkBlockWrapper rootSubSystem = parentModel.getRootSubsystem();
      if (Util.matchStringsIgnoreCase(rootSubSystem.getIdInLocalContext(),
          this.getIdInLocalContext())) {
        isRootOfALibrary = true;
      }
    }
    return isRootOfALibrary;
  }

  public String getIdInLocalContext() {
    return simulinkBlock.getId();
  }

  public String getIdInGlobalContext() {
    String blockId = getIdInLocalContext();
    SimulinkModelWrapper blocksModel = getSimulinkModelWrapped();
    if (!blocksModel.isReferenced()) {
      return blockId;
    }
    String trgistryId = blocksModel.getRegistryEntryId();
    String localContext = Util.extractLocalIdFromRegistryEntry(trgistryId);
    String globalContext = Util.extractGlobalIdFromRegistryEntry(trgistryId);
    String globalId = blockId.replaceAll(localContext, globalContext);
    return globalId.trim();
  }

  public String getName() {
    return simulinkBlock.getName();
  }

  public String getType() {
    return simulinkBlock.getType();
  }

  public String getLibraryName() {
    String libraryName = "";
    if (isLibrary()) {
      ReferencedBlockInfo rbi = simulinkBlock.getReferencedBlockInfo();
      libraryName = rbi.getModelName();
    }
    return libraryName;
  }

  public String getLibraryAndSubsystem() {
    return String.format("%s/%s", getLibraryName(), getName());
  }

  public SimulinkModelWrapper getSimulinkModelWrapped() {
    return new SimulinkModelWrapper(simulinkBlock.getModel());
  }

  public SimulinkBlock getSimulinkBlock() {
    return simulinkBlock;
  }

  public Boolean exists() {
    return simulinkBlock != null;
  }

  public Collection<SimulinkLineWrapper> getIncomingLines() {
    /* isLibrary to be revisited. */
    if (!this.exists() || this.isLibrary()) {
      return new ArrayList<SimulinkLineWrapper>();
    }

    Collection<SimulinkLine> inLines = simulinkBlock.getInLines();
    Collection<SimulinkLineWrapper> wrappedInLines = new ArrayList<>();
    for (SimulinkLine inline : inLines) {
      SimulinkLineWrapper wrappedLine = new SimulinkLineWrapper(inline);
      wrappedInLines.add(wrappedLine);
    }
    return wrappedInLines;
  }

  public Collection<SimulinkLineWrapper> getOutgoingLines() {
    /* isLibrary to be revisited. */
    if (!this.exists() || this.isLibrary()) {
      return new ArrayList<SimulinkLineWrapper>();
    }

    Collection<SimulinkLine> outLines = simulinkBlock.getOutLines();
    Collection<SimulinkLineWrapper> wrappedOutLines = new ArrayList<>();
    for (SimulinkLine inline : outLines) {
      SimulinkLineWrapper wrappedLine = new SimulinkLineWrapper(inline);
      wrappedOutLines.add(wrappedLine);
    }
    return wrappedOutLines;
  }

  public SimulinkBlockWrapper getInternalPortBlockByIndex(String portType, String portIndex) {
    SimulinkBlockWrapper resultBlock = new SimulinkBlockWrapper(null);
    SimulinkBlock subSystemForParsing = simulinkBlock;
    if (isLibrary()) {
      SimulinkModelWrapper wrappedModel = SimulinkModelBuilder.buildReferencedSimulinkModel(this);
      if (wrappedModel.exists()) {
        SimulinkBlockWrapper rootSubSystem = wrappedModel.getRootSubsystem();
        subSystemForParsing = rootSubSystem.getSimulinkBlock();
      }
    }
    for (SimulinkBlock internalBlock : subSystemForParsing.getSubBlocks()) {
      if (Util.matchStringsIgnoreCase(internalBlock.getType(), portType)) {
        String iteratingBlockIndex =
            internalBlock.getDeclaredParameter(SimulinkDeclaredParametersEnum.PORT.toString());
        iteratingBlockIndex = Util.convertToPortIndex(iteratingBlockIndex);
        if (Util.matchStringsIgnoreCase(iteratingBlockIndex, portIndex)) {
          resultBlock = new SimulinkBlockWrapper(internalBlock);
          break;
        }
      }
    }
    return resultBlock;
  }

  public Collection<SimulinkBlockWrapper> getSubBlocks() {
    Collection<SimulinkBlockWrapper> subBlocks = new ArrayList<>();
    if (isLibrary()) {
      /* Because if it is a library you should open it as a model. */
      return subBlocks;
    }
    for (SimulinkBlock simulinkBlock : simulinkBlock.getSubBlocks()) {
      SimulinkBlockWrapper wrappedChild = new SimulinkBlockWrapper(simulinkBlock);
      subBlocks.add(wrappedChild);
    }
    return subBlocks;
  }

  public Collection<SimulinkBlockWrapper> getSubBlockByType(String blockType) {
    Collection<SimulinkBlockWrapper> allSubBlocks = getSubBlocks();
    Collection<SimulinkBlockWrapper> resultBlocks = new ArrayList<>();
    for (SimulinkBlockWrapper wrappedBlock : allSubBlocks) {
      if (wrappedBlock.isSubsystem()) {
        resultBlocks.addAll(wrappedBlock.getSubBlockByType(blockType));
      }
      if (Util.matchStringsIgnoreCase(blockType, wrappedBlock.getType())) {
        resultBlocks.add(wrappedBlock);
      }
    }
    return resultBlocks;
  }

  public String getParameter(String parameterName) {
    return simulinkBlock.getParameter(parameterName);
  }

  public void seParameter(String parameterName, String parameterValue) {
    if (parameterName != null && parameterName != "") {
      simulinkBlock.setParameter(parameterName, parameterValue);
    }
  }

  public String getDeclaredParameter(String parameterName) {
    String value = "";
    if (!Util.stringNullOrEmpty(parameterName)) {
      value = simulinkBlock.getDeclaredParameter(parameterName);
      value = value != null ? value : "";
    }
    return value;
  }

  public Collection<String> getDeclaredParameterNames() {
    return simulinkBlock.getParameterNames();
  }

  public Collection<SimulinkLineWrapper> getIncomingLinesByPortIndex(String index) {
    Collection<SimulinkLineWrapper> wrappedIncomingLines = new ArrayList<SimulinkLineWrapper>();
    SimulinkLineWrapper wrappedLine = null;
    SimulinkBlock blockToExtractLinesFrom = this.simulinkBlock;
    if (isRootOfALibrary()) {
      SimulinkModelWrapper parentModel = SimulinkModelBuilder.buildParentSimulinkModel(this);
      SimulinkBlockWrapper subSystemInParentContext =
          parentModel.getBlockById(getIdInGlobalContext());
      if (subSystemInParentContext.exists()) {
        blockToExtractLinesFrom = subSystemInParentContext.getSimulinkBlock();
      }
    }
    for (SimulinkLine incomingLine : blockToExtractLinesFrom.getInLines()) {
      SimulinkPortBase dstPort = incomingLine.getDstPort();
      if (Util.matchStringsIgnoreCase(index, dstPort.getIndex())) {
        wrappedLine = new SimulinkLineWrapper(incomingLine);
        wrappedIncomingLines.add(wrappedLine);
      }
    }
    return wrappedIncomingLines;
  }

  public Collection<SimulinkLineWrapper> getOutgoingLinesByPortIndex(String index) {
    Collection<SimulinkLineWrapper> wrappedOutgingLines = new ArrayList<SimulinkLineWrapper>();
    SimulinkLineWrapper wrappedLine = null;
    SimulinkBlock blockToExtractLinesFrom = this.simulinkBlock;
    if (isRootOfALibrary()) {
      SimulinkModelWrapper parentModel = SimulinkModelBuilder.buildParentSimulinkModel(this);
      SimulinkBlockWrapper subSystemInParentContext =
          parentModel.getBlockById(getIdInGlobalContext());
      if (subSystemInParentContext.exists()) {
        blockToExtractLinesFrom = subSystemInParentContext.getSimulinkBlock();
      }
    }
    for (SimulinkLine outgoingLine : blockToExtractLinesFrom.getOutLines()) {
      SimulinkPortBase srcPort = outgoingLine.getSrcPort();
      if (Util.matchStringsIgnoreCase(index, srcPort.getIndex())) {
        wrappedLine = new SimulinkLineWrapper(outgoingLine);
        wrappedOutgingLines.add(wrappedLine);
      }
    }
    return wrappedOutgingLines;
  }

  public Boolean isRemoteSignalBlock() {
    return (Util.matchStringsIgnoreCase(simulinkBlock.getType(),
        SimulinkBlockTypesEnum.FROM.toString())
        || (Util.matchStringsIgnoreCase(simulinkBlock.getType(),
            SimulinkBlockTypesEnum.GOTO.toString())));
  }

  public Boolean isSubSystemSampled() {
    /* we say it is sampled if it has a trigger port */
    Boolean isSampled = false;
    for (SimulinkInPort inPort : simulinkBlock.getInPorts()) {
      if (inPort.isTriggerPort()) {
        isSampled = true;
        break;
      }
    }
    return isSampled;
  }

  public Boolean isSubSystemSampledRecursively() {
    Boolean isSampled = this.isSubSystemSampled();
    if (!isSampled
        && !Util.matchStringsIgnoreCase(this.getParentSimulinkBlock().getType(),
            SimulinkBlockTypesEnum.MODELREFERENCE.toString())
        && !Util.matchStringsIgnoreCase(this.getParentSimulinkBlock().getType(),
            SimulinkBlockTypesEnum.MODEL.toString())) {
      isSampled = this.getParentSimulinkBlock().isSubSystemSampledRecursively();
    }
    return isSampled;
  }

  public String getSampleTime() {
    String ts = getSampleTimeFromRegistry();
    if (Util.stringNullOrEmpty(ts)) {
      ts = getSampleTimeResursively();
    }
    return ts != null ? ts : "";
  }

  public String getSampleTimeFromTrigger() {
    String ts = "";
    Collection<SimulinkLine> incomingLines = this.simulinkBlock.getInLines();
    for (SimulinkLine inLine : incomingLines) {
      SimulinkLineWrapper incomingLine = new SimulinkLineWrapper(inLine);
      SimulinkBlockWrapper lineSourceBlock = incomingLine.getSourceBlock();
      String srcBlockType = lineSourceBlock
          .getDeclaredParameter(SimulinkDeclaredParametersEnum.SOURCETYPE.toString());
      if (Util.matchStringsIgnoreCase(srcBlockType,
          SimulinkBlockTypesEnum.FUNCTION_CALL_GENERATOR.toString())) {
        SimulinkBlockWrapper triggerBlock = incomingLine.getSourceBlock();
        ts = triggerBlock
            .getDeclaredParameter(SimulinkDeclaredParametersEnum.SAMPLE_TIME_COMPOSITE.toString());
        break;
      }
    }
    return ts;
  }

  public String getSampleTimeResursively() {
    String ts = getSampleTimeFromTrigger();
    if (Util.stringNullOrEmpty(ts)) {
      SimulinkBlockWrapper parent = this.getParentSimulinkBlock();
      if (parent.exists()) {
        ts = parent.getSampleTime();
      }
    }
    ts = ts != null ? ts : "";
    return ts;
  }

  public String getSampleTimeFromRegistry() {
    String ts = "";
    SimulinkModelWrapper currentModel = this.getSimulinkModelWrapped();
    SimulinkBlockWrapper rootSubSystem = currentModel.getRootSubsystem();
    if (rootSubSystem.exists()) {
      String localInheritanceEntry = currentModel.getRegistryEntryId();
      SerializableHashTable globalSampleTimeRegistry = getGlobalTriggeringRegistry();
      ts = globalSampleTimeRegistry.get(localInheritanceEntry);
    }
    return ts != null ? ts : "";
  }

  public SimulinkBlockWrapper getParentSimulinkBlock() {
    return new SimulinkBlockWrapper(simulinkBlock.getParent());
  }

  public SerializableHashTable getGlobalTriggeringRegistry() {
    String sampleTimeRegistrySerialized = this.getSimulinkModelWrapped()
        .getParameter(SimulinkParametersEnum.TRIGGERING_INFO.toString());
    if (Util.stringNullOrEmpty(sampleTimeRegistrySerialized)) {
      sampleTimeRegistrySerialized = "";
    }
    return SerializableHashTable.deserialize(sampleTimeRegistrySerialized);
  }

  public String getSampleTimeFromGlobalRegistry() {
    String sampleTime = "";
    SerializableHashTable trigeringRegistry = getGlobalTriggeringRegistry();
    if (trigeringRegistry.containsKey(this.getIdInGlobalContext())) {
      sampleTime = trigeringRegistry.get(this.getIdInGlobalContext());
    } else {
      SimulinkBlockWrapper parent = this.getParentSimulinkBlock();
      if (parent.exists()) {
        sampleTime = parent.getSampleTimeFromGlobalRegistry();
      }
    }
    return sampleTime;
  }

  public String getNameForSTA() {
    String originalName = getName();
    /*
     * in uppaal nothing should start with number. this is for such blocks, so we put underscore at
     * the beginning
     */
    if (originalName.length() > 0 && Character.isDigit(originalName.charAt(0))) {
      originalName = "_" + originalName;
    }
    originalName = originalName.replaceAll("[^a-zA-Z\\d:]", "_").toLowerCase().trim();
    return String.format("%s_%d", originalName, executionOrderNumber);
  }

  public String getSignalName() {
    return String.format("%s_signal", getNameForSTA(), executionOrderNumber);
  }

  public String getSignalDeclaration() {
    String signalDeclaration = String.format("double %s;", getSignalName());
    /*
     * String outDataTypeStr = this.getDeclaredParameter("OutDataTypeStr"); if
     * (Util.stringNullOrEmpty(outDataTypeStr)) { outDataTypeStr = "double"; } if
     * (outDataTypeStr.equals("boolean")) { signalDeclaration = String.format("bool %s;",
     * getSignalName()); }
     */
    return signalDeclaration;
  }

  public String getSTAInstanceName() {
    return String.format("%s_instance", getNameForSTA());
  }
}
