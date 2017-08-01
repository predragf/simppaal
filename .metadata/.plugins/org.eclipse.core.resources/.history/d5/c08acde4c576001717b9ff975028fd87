/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.types.hierarchy;

import java.util.ArrayList;
import java.util.Collection;

import org.fmaes.simppaal.simulinktotimedautomata.utils.SimulinkUtils;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
@SuppressWarnings("serial")
public class HierarchyTree extends ArrayList<HierarchyNode> {

  private String rootNodeId;

  public HierarchyTree() {
    super();
    rootNodeId = "";
  }

  public Collection<HierarchyNode> generateBlockDependentHierarchy(String globalBlockId) {
    // the goal is to find the deepest root and then it can be easy traced back as each
    // node has only one parent
    HierarchyNode modelContatainingTheBlock = matchByGlobalIdRecursively(globalBlockId);
    return generateBlockDependentHierarchy(modelContatainingTheBlock);
  }

  public Collection<HierarchyNode> generateBlockDependentHierarchy(HierarchyNode leafModel) {
    Collection<HierarchyNode> _result = new ArrayList<HierarchyNode>();
    if(leafModel == null){
      return _result;
    }
    _result.add(leafModel);
    HierarchyNode _parentNode = getNodeById(leafModel.getParentNodeId());
    _result.addAll(generateBlockDependentHierarchy(_parentNode));
    return _result;
  }

  public HierarchyNode getNodeById(String nodeId) {
    HierarchyNode _match = null;
    for (HierarchyNode _node : this) {
      if (_node.getNodeId().equals(nodeId)) {
        _match = _node;
        break;
      }
    }
    return _match;
  }

  private HierarchyNode matchByGlobalId(String globalBlockId) {
    HierarchyNode _match = null;
    for (HierarchyNode _node : this) {
      String nodeGlobalId = _node.getGlobalModelId();
      if (nodeGlobalId.equals(globalBlockId)) {
        _match = _node;
        break;
      }
    }
    return _match;
  }

  private HierarchyNode matchByGlobalIdRecursively(String globalBlockId) {
    String matchingGlobalId = globalBlockId;
    HierarchyNode _match = null;
    int numberOfLevelsInId = SimulinkUtils.getNumberOfLevels(matchingGlobalId);
    while (numberOfLevelsInId > 0 && _match == null) {
      _match = matchByGlobalId(matchingGlobalId);
      matchingGlobalId = SimulinkUtils.trimSimulinkIdFromBack(matchingGlobalId, 1);
      numberOfLevelsInId = SimulinkUtils.getNumberOfLevels(matchingGlobalId);
    }
    return _match;
  }

  @Override
  public boolean add(HierarchyNode _node) {
    // If the rootnode has not been assigned and the node to be added fulfills the criteria for
    // being root, make it a root node
    if (rootNodeId == "" && _node.getParentNodeId() == "") {
      rootNodeId = _node.getNodeId();
    }
    return super.add(_node);
  }

  @Override
  public void clear() {
    super.clear();
    rootNodeId = "";
  }

  @Override
  public HierarchyNode remove(int index) {
    HierarchyNode resultNode = super.remove(index);
    if (super.size() < 1) {
      rootNodeId = "";
    }
    return resultNode;
  }

  public String toString() {
    String asString = "";
    for (HierarchyNode node : this) {
      asString += String.format("%s:%s:%s:%s;", node.getNodeId(), node.getGlobalModelId(),
          node.getReferencedBlockId(), node.getParentNodeId());
    }
    return asString;
  }

}
