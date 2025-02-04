package org.fmaes.simulinktotimedautomata.sorder;

public class SortedOrderEntry implements Comparable<SortedOrderEntry> {
  public String id;
  public String parentId;
  public String sortedNumber;

  public SortedOrderEntry() {
    id = parentId = sortedNumber = "";
  }

  @Override
  public int compareTo(SortedOrderEntry sortedOrderEntry) {
    // TODO Auto-generated method stub
    int retValue = 0;
    String[] currentEntryParts = sortedNumber.split(":");
    String[] guestEntryParts = sortedOrderEntry.sortedNumber.split(":");
    int minLength = Integer.min(currentEntryParts.length, guestEntryParts.length);
    for (int i = 0; i < minLength; i++) {
      int currentEntrySection = Integer.parseInt(currentEntryParts[i]);
      int guestEntrySection = Integer.parseInt(guestEntryParts[i]);
      if (currentEntrySection < guestEntrySection) {
        retValue = -1;
        break;
      }
      if (currentEntrySection > guestEntrySection) {
        retValue = 1;
        break;
      }
    }
    if (retValue == 0) {
      if (currentEntryParts.length <= guestEntryParts.length) {
        retValue = -1;
      } else {
        retValue = 1;
      }
    }
    return retValue;
  }

}
