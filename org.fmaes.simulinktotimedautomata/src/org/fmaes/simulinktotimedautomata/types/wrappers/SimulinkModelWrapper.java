package org.fmaes.simulinktotimedautomata.types.wrappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import org.conqat.lib.simulink.model.SimulinkBlock;
import org.conqat.lib.simulink.model.SimulinkInPort;
import org.conqat.lib.simulink.model.SimulinkModel;
import org.conqat.lib.simulink.model.SimulinkOutPort;
import org.conqat.lib.simulink.model.SimulinkPortBase;
import org.fmaes.simulinktotimedautomata.builders.SimulinkModelBuilder;
import org.fmaes.simulinktotimedautomata.parsers.SimulinkLineParser;
import org.fmaes.simulinktotimedautomata.types.enums.SimulinkBlockTypesEnum;
import org.fmaes.simulinktotimedautomata.types.enums.SimulinkParametersEnum;
import org.fmaes.simulinktotimedautomata.util.SerializableHashTable;
import org.fmaes.simulinktotimedautomata.util.Util;

public class SimulinkModelWrapper {
  private SimulinkModel simulinkModel;

  @SuppressWarnings("unused")
  private SimulinkModelWrapper() {};

  public SimulinkModelWrapper(SimulinkModel _simulinkModel) {
    simulinkModel = _simulinkModel;
  }

  public Collection<SimulinkBlockWrapper> getChildBlocksByType(String blockType) {
    Collection<SimulinkBlockWrapper> baseBlocksByType = new ArrayList<SimulinkBlockWrapper>();
    for (SimulinkBlock simulinkBlock : simulinkModel.getSubBlocks()) {
      SimulinkBlockWrapper wrappedBaseBlock = new SimulinkBlockWrapper(simulinkBlock);
      if (wrappedBaseBlock.isSubsystem()) {
        baseBlocksByType.addAll(wrappedBaseBlock.getSubBlockByType(blockType));
      }
      if (Util.matchStringsIgnoreCase(blockType, wrappedBaseBlock.getType())) {
        baseBlocksByType.add(wrappedBaseBlock);
      }
    }
    return baseBlocksByType;
  }

  private SimulinkBlockWrapper getBlockRecursively(String blockId) {
    SimulinkBlockWrapper wrappedBaseBlock = new SimulinkBlockWrapper(null);
    /*
     * This is for optimization. if the blockId is null or empty, there is no need to continue
     * executing the complete function.
     */
    if (Util.stringNullOrEmpty(blockId)) {
      return wrappedBaseBlock;
    }
    Collection<SimulinkBlockWrapper> referencedModelChildren =
        getChildBlocksByType(SimulinkBlockTypesEnum.REFERENCE.toString());
    for (SimulinkBlockWrapper referencedChildBlock : referencedModelChildren) {
      SimulinkModelWrapper wrappedReferencedLibrary =
          SimulinkModelBuilder.buildReferencedSimulinkModel(referencedChildBlock);
      if (!wrappedReferencedLibrary.exists()) {
        continue;
      }
      wrappedBaseBlock = wrappedReferencedLibrary.getBlockById(blockId);
      if (wrappedBaseBlock.exists()) {
        break;
      }
    }
    return wrappedBaseBlock;
  }

  public SimulinkBlockWrapper getBlockById(String blockId) {
    /* This is only for a block on a same level as the model */
    SimulinkBlock baseBlock = null;
    SimulinkBlockWrapper wrappedSimulinkBlock = new SimulinkBlockWrapper(baseBlock);
    if (isReferenced()) {
      SimulinkBlockWrapper rootSubSystem = getRootSubsystem();
      String localModelId = rootSubSystem.getIdInLocalContext();
      String globalModelId = rootSubSystem.getIdInGlobalContext();
      String localBlockId = "";
      if (blockId.startsWith(globalModelId)) {
        localBlockId = blockId.replace(globalModelId, localModelId);
      }
      baseBlock = simulinkModel.getBlock(localBlockId);
    } else {
      baseBlock = simulinkModel.getBlock(blockId);
    }
    /* If the required block was not found on a root level, do it for the referenced models. */
    if (baseBlock != null) {
      wrappedSimulinkBlock = new SimulinkBlockWrapper(baseBlock);
    } else {
      wrappedSimulinkBlock = getBlockRecursively(blockId);
    }
    return wrappedSimulinkBlock;
  }

  private String getGlobalId() {
    /* TODO: to be implemented. */
    return "";
  }

  private void setGlobalId(String globalId) {
    /* TODO: to be implemented. */
  }

  public Boolean exists() {
    return simulinkModel != null;
  }

  public Boolean isReferenced() {
    Boolean isReferenced = false;
    SerializableHashTable inheritanceTree = getInheritanceRegistry();
    Enumeration<String> childrenIds = inheritanceTree.keys();
    while (childrenIds.hasMoreElements()) {
      String childId = (String) childrenIds.nextElement();
      if (childId.startsWith(String.format("%s/", simulinkModel.getName()))) {
        isReferenced = true;
        break;
      }
    }
    return isReferenced;
  }

  public SimulinkModel getSimulinkModel() {
    return simulinkModel;
  }

  public String getSimulinkModelName() {
    return simulinkModel.getName();
  }

  public Collection<SimulinkBlockWrapper> getContainedBlocks() {
    Collection<SimulinkBlockWrapper> allBlocks = new ArrayList<>();
    for (SimulinkBlock internalSimulinkBlock : simulinkModel.getSubBlocks()) {
      SimulinkBlockWrapper wrappedInternalBlock = new SimulinkBlockWrapper(internalSimulinkBlock);
      allBlocks.add(wrappedInternalBlock);
    }
    return allBlocks;
  }

  public String getParameter(String parameterName) {
    /*
     * make sure that the function returns valid string. In case of non-existing paramter empty
     * string will be returned.
     */
    String parameterValue = simulinkModel.getParameter(parameterName);
    if (Util.stringNullOrEmpty(parameterValue)) {
      parameterValue = "";
    }
    return parameterValue;
  }

  public void setParameter(String parameterName, String parameterValue) {
    if (!Util.stringNullOrEmpty(parameterName)) {
      simulinkModel.setParameter(parameterName, parameterValue);
    }
  }

  public SimulinkBlockWrapper getRootSubsystem() {
    return getRootSubsystem(true);
  }

  public SimulinkBlockWrapper getRootSubsystem(Boolean flag) {
    SimulinkBlockWrapper rootSubSystem = new SimulinkBlockWrapper(null);
    if (flag || isReferenced()) {
      Collection<SimulinkBlock> children = simulinkModel.getSubBlocks();
      Iterator<SimulinkBlock> childIterator = children.iterator();
      SimulinkBlock rootSubsystemNative = childIterator.next();
      rootSubSystem = new SimulinkBlockWrapper(rootSubsystemNative);
    }
    return rootSubSystem;
  }

  public SerializableHashTable getInheritanceRegistry() {
    SerializableHashTable inheritanceTree = new SerializableHashTable();
    String inheritanceTreeSerialized =
        this.getParameter(SimulinkParametersEnum.INHERITANCE_TREE.toString());
    if (!Util.stringNullOrEmpty(inheritanceTreeSerialized)) {
      inheritanceTree = SerializableHashTable.deserialize(inheritanceTreeSerialized);
    }
    return inheritanceTree;
  }

  public void setInheritanceRegistry(SerializableHashTable inheritanceTree) {
    String inheritanceTreeSerialized = inheritanceTree.serialize();
    this.setParameter(SimulinkParametersEnum.INHERITANCE_TREE.toString(),
        inheritanceTreeSerialized);
  }

  public SerializableHashTable getTriggeringRegistry() {
    SerializableHashTable triggeringRegistry = new SerializableHashTable();
    String triggeringRegistrySerialized =
        this.getParameter(SimulinkParametersEnum.TRIGGERING_INFO.toString());
    if (!Util.stringNullOrEmpty(triggeringRegistrySerialized)) {
      triggeringRegistry = SerializableHashTable.deserialize(triggeringRegistrySerialized);
    }
    return triggeringRegistry;
  }

  public void setTriggeringRegistry(SerializableHashTable triggeringRegistry) {
    String triggeringRegistrySerialized = triggeringRegistry.serialize();
    this.setParameter(SimulinkParametersEnum.TRIGGERING_INFO.toString(),
        triggeringRegistrySerialized);
  }

  public Collection<Neighbour> findPredecessors(String blockId) {
    SimulinkBlockWrapper blockForParsing = getBlockById(blockId);
    return findPredecessors(blockForParsing);

  }

  public Collection<Neighbour> findPredecessors(SimulinkBlockWrapper blockForParsing) {
    Collection<Neighbour> predecessors = new ArrayList<>();
    if (blockForParsing.exists()) {
      for (SimulinkInPort blockInPort : blockForParsing.getSimulinkBlock().getInPorts()) {
        SimulinkPortBaseWrapper rootPort = new SimulinkPortBaseWrapper(blockInPort);
        Collection<SimulinkLineWrapper> inlinesByPort =
            blockForParsing.getIncomingLinesByPortIndex(blockInPort.getIndex());
        for (SimulinkLineWrapper inlineOfBlockToBeParsed : inlinesByPort) {
          Collection<Neighbour> portPredecessors =
              SimulinkLineParser.parseLine(inlineOfBlockToBeParsed, "");
          for (Neighbour portPredecessor : portPredecessors) {
            // Predecessor predecessor = new Predecessor(rootPort, portPredecessor);
            portPredecessor.setFromPort(rootPort);
            predecessors.add(portPredecessor);
          }
        }
      }
    }
    return predecessors;
  }

  public String getRegistryEntryId() {
    return getParameter("registryEntryId");
  }

  public Collection<Neighbour> findSuccessors(String blockId) {
    SimulinkBlockWrapper sBlock = this.getBlockById(blockId);
    return findSuccessors(sBlock);
  }

  public Collection<Neighbour> findSuccessors(SimulinkBlockWrapper sBlock) {
    Collection<Neighbour> successors = new ArrayList<>();
    if (!sBlock.exists()) {
      return successors;
    }
    for (SimulinkOutPort blockOutPort : sBlock.getSimulinkBlock().getOutPorts()) {
      SimulinkPortBaseWrapper rootPort = new SimulinkPortBaseWrapper(blockOutPort);
      Collection<SimulinkLineWrapper> outlinesByPort =
          sBlock.getOutgoingLinesByPortIndex(blockOutPort.getIndex());
      for (SimulinkLineWrapper outlineOfBlockToBeParsed : outlinesByPort) {
        Collection<Neighbour> portSuccessors =
            SimulinkLineParser.parseLineForward(outlineOfBlockToBeParsed, "");
        for (Neighbour portSuccessor : portSuccessors) {
          // Predecessor predecessor = new Predecessor(rootPort, portPredecessor);
          portSuccessor.setFromPort(rootPort);
          successors.add(portSuccessor);
        }
      }
    }
    return successors;
  }

  public void setRegistryEntryId(String registryEntryId) {
    setParameter("registryEntryId", registryEntryId);
  }

  public Collection<SimulinkBlockWrapper> getContainedBlocksByType(String blockType) {
    Collection<SimulinkBlockWrapper> blocksForTheGivenType = new ArrayList<>();
    for (SimulinkBlock baseContainedBlock : this.simulinkModel.getSubBlocks()) {
      if (Util.matchStringsIgnoreCase(blockType, baseContainedBlock.getType())) {
        blocksForTheGivenType.add(new SimulinkBlockWrapper(baseContainedBlock));
      }
    }
    return blocksForTheGivenType;
  }
}
