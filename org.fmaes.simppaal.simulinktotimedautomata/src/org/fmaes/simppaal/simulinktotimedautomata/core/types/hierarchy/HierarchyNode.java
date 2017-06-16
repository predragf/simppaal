/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.types.hierarchy;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
public class HierarchyNode {

  private String nodeId;

  private String parentNodeId;

  private String referencedBlockId;

  private String simulinkModelFileName;

  private String globalModelId;

  private int referencedModelType;

  public HierarchyNode() {
    nodeId = "";
    parentNodeId = "";
    referencedBlockId = "";
    simulinkModelFileName = "";
    globalModelId = "";
    setReferencedModelType(ReferencedModelTypeEnum.INVALID);
  }

  public HierarchyNode(String _referencedBlockId, String _simulinkFileName,
      int _referencedModelType, HierarchyNode _parentNode) {
    nodeId = UUID.randomUUID().toString();
    parentNodeId = _parentNode.nodeId;
    referencedBlockId = _referencedBlockId;
    simulinkModelFileName = _simulinkFileName;
    setReferencedModelType(_referencedModelType);
  }

  public String getNodeId() {
    return nodeId;
  }

  public String getParentNodeId() {
    return parentNodeId;
  }

  public String getReferencedBlockId() {
    return referencedBlockId;
  }

  public String getSimulinkModelFileName() {
    return simulinkModelFileName;
  }

  public String getGlobalModelId() {
    return globalModelId;
  }

  public int getReferencedModelType() {
    return referencedModelType;
  }

  private void setGlobalModelId(String _parentGlobalId){
    
  }
  
  private void setReferencedModelType(int _referencedModelType) {
    if (_referencedModelType == ReferencedModelTypeEnum.LIBRARY
        || _referencedModelType == ReferencedModelTypeEnum.MODEL) {
      referencedModelType = _referencedModelType;
    } else {
      _referencedModelType = ReferencedModelTypeEnum.INVALID;
    }
  }
}
