package org.fmaes.simppaal.simulinktotimedautomata.sorder;

import java.util.ArrayList;
import java.util.Collections;

import org.fmaes.simppaal.simulinktotimedautomata.utils.DiskUtils;

@SuppressWarnings("serial")
public class SortedOrderList extends ArrayList<SortedOrderEntry> {

  public void sort() {
    Collections.sort(this);
    int entryNumber = 1;
    for (SortedOrderEntry entry : this) {
      entry.executionOrderNumber = entryNumber;
      entryNumber++;
    }
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

  public String asString() {
    this.sort();
    String asString = "";
    int index = 1;
    for (SortedOrderEntry entry : this) {
      asString += String.format("%d (%s) %s \n", index, entry.sortedNumber, entry.id);
      index++;
    }

    return asString;
  }

  public void Save(String location) {
    String collectionAsList = this.asString();
    DiskUtils.saveFileAsTxt(location, collectionAsList);
  }
}
