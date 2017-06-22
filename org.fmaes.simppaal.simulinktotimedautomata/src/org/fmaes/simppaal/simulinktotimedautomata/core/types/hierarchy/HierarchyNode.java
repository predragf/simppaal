/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.types.hierarchy;

import java.util.UUID;

import org.fmaes.simppaal.simulinktotimedautomata.utils.SimulinkUtils;

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
    setGlobalModelId(_parentNode, _simulinkFileName, _referencedBlockId, _referencedModelType);
  }

  public HierarchyNode(String _simulinkFileName) {
    nodeId = UUID.randomUUID().toString();
    parentNodeId = "";
    referencedBlockId = "";
    simulinkModelFileName = _simulinkFileName;
    setReferencedModelType(ReferencedModelTypeEnum.MODEL);
    setGlobalModelId(null, _simulinkFileName, "", ReferencedModelTypeEnum.MODEL);
  }

  public String getNodeId() {
    return nodeId;
  }

  public String getParentNodeId() {
    return parentNodeId != null ? parentNodeId : "";
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

  private String processReferencedBlockId(int _referencedModelType, String _referencedBlockId) {
    String referencedBlockIdProcessed = _referencedBlockId;
    if (_referencedModelType == ReferencedModelTypeEnum.LIBRARY) {
      referencedBlockIdProcessed = SimulinkUtils.trimSimulinkIdFromFront(_referencedBlockId, 2);
    }
    if (_referencedModelType == ReferencedModelTypeEnum.MODEL) {
      referencedBlockIdProcessed = SimulinkUtils.trimSimulinkIdFromFront(_referencedBlockId, 1);
    }
    return referencedBlockIdProcessed;
  }

  private void setGlobalModelId(HierarchyNode _parentNode, String _simulinkModelFileName,
      String _referencedBlockId, int _referencedModelType) {
    if (_parentNode == null) {
      globalModelId = SimulinkUtils.stripExtension(_simulinkModelFileName);
      return;
    }
    String referencedBlockIdProcessed =
        processReferencedBlockId(_referencedModelType, _referencedBlockId);
    globalModelId = String.format("%s/%s", _parentNode.globalModelId, referencedBlockIdProcessed);
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
