/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.types.hierarchy;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
public class HierarchyTree extends ArrayList<HierarchyNode> {

  private String rootNodeId;

  public HierarchyTree() {
    super();
  }

  public Collection<HierarchyNode> generateBlockDependentHierarchy(String globalBlockId) {
    return null;
  }

  @Override
  public boolean add(HierarchyNode _node) {
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

}
