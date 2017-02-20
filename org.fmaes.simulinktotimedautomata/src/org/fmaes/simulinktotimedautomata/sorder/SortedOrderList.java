package org.fmaes.simulinktotimedautomata.sorder;

import java.util.ArrayList;
import java.util.Collections;

public class SortedOrderList extends ArrayList<SortedOrderEntry> {

  public void sort() {
    Collections.sort(this);
  }

  public int getBlockExecutionOrderById(String blockId) {
    int blockExecutionOrder = 0;
    for (int index = 0; index < this.size(); index++) {
      SortedOrderEntry sListEntry = this.get(index);
      if (sListEntry.id.equals(blockId)) {
        blockExecutionOrder = index + 1;
        break;
      }
    }
    return blockExecutionOrder;
  }

  public int getHighestChildExecutionOrder(String blockId) {
    // this.sort();
    int blockExecutionOrder = 0;
    for (int index = 0; index < this.size(); index++) {
      SortedOrderEntry sListEntry = this.get(index);
      if (sListEntry.id.contains(blockId)) {
        blockExecutionOrder = index + 1;
      }
    }
    return blockExecutionOrder;
  }
}
