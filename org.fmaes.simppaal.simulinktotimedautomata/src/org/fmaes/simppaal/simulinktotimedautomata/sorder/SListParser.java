package org.fmaes.simppaal.simulinktotimedautomata.sorder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SListParser {

  private static String compositeElementPatternString = ".*---- Sorted list for '.*'.*$";

  private static String childElementPatternString = ".*[0-9]+(:[0-9]+)+.*";

  private static String ExecutePattern(String row, String regex) {
    String result = "";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(row);
    try {
      if (matcher.find()) {
        result = matcher.group(1);
      }
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      System.out.println(row);
    }
    return result;
  }

  private static String ExtractIDFromRow(String row) {
    String regex = "'(.*?)'";
    return ExecutePattern(row, regex);
  }

  private static String ExtractSortedOrderId(String row) {
    String regex = "([0-9]+:[0-9]+)";
    return ExecutePattern(row, regex);
  }

  private static SortedOrderEntry ProcessSListEntry(String row, String parentID) {
    SortedOrderEntry element = new SortedOrderEntry();
    element.parentId = parentID;
    element.id = ExtractIDFromRow(row);
    element.sortedNumber = ExtractSortedOrderId(row);
    return element;
  }

  private static List<SortedOrderEntry> OpenAndProcessSList(String sListPath) {
    List<SortedOrderEntry> fileContents = new ArrayList<SortedOrderEntry>();
    File file = new File(sListPath);
    String currentCompositeNodeId = "";
    String line;
    try {
      try (BufferedReader br = new BufferedReader(new FileReader(file))) {

        while ((line = br.readLine()) != null) {

          line = line.trim();
          if (line == null || line == "") {
            continue;
          }
          // process the line.
          if (Pattern.matches(compositeElementPatternString, line)) {
            /* extract the current parent */
            currentCompositeNodeId = ExtractIDFromRow(line);
            continue;
          }

          if (Pattern.matches(childElementPatternString, line)) {
            /* handle the child element parsing */
            SortedOrderEntry entryToAdd = ProcessSListEntry(line, currentCompositeNodeId);
            if (!fileContents.contains(entryToAdd)) {
              fileContents.add(entryToAdd);
            }
            continue;
          }
        }
      }
    } catch (Exception ex) {

      System.err.println(ex.getMessage());

    }
    return fileContents;
  }

  private static SortedOrderList Sort(List<SortedOrderEntry> sublist,
      List<SortedOrderEntry> allElements) {
    SortedOrderList result = new SortedOrderList();
    for (SortedOrderEntry sOrderEntry : sublist) {
      List<SortedOrderEntry> children = GetElementChildren(allElements, sOrderEntry);
      if (children.size() < 1) {
        result.add(sOrderEntry);
        continue;
      }
      result.addAll(Sort(children, allElements));
    }

    return result;
  }

  private static SortedOrderList GetElementChildren(List<SortedOrderEntry> list,
      SortedOrderEntry parent) {
    SortedOrderList result = new SortedOrderList();
    for (SortedOrderEntry sOrderEntry : list) {
      if (sOrderEntry.parentId.equals(parent.id)) {
        String parentSN =
            parent.sortedNumber != null && parent.sortedNumber != "" ? parent.sortedNumber : "0";
        sOrderEntry.sortedNumber = String.format("%s:%s", parentSN, sOrderEntry.sortedNumber);
        if (!sOrderEntry.id.toLowerCase().contains("scope")) {
          result.add(sOrderEntry);
        }
      }
    }
    return result;
  }

  public static SortedOrderList GetSortedOrderList(String modelName, String sListLocationPath) {
    if (modelName.endsWith(".mdl")) {
      modelName = modelName.substring(0, modelName.lastIndexOf('.'));
    }
    SortedOrderList result = new SortedOrderList();
    List<SortedOrderEntry> allElements = OpenAndProcessSList(sListLocationPath);
    /* SOrderEntry with "" for ID is considered to be root */
    SortedOrderEntry globalParrent = new SortedOrderEntry();
    globalParrent.id = modelName;
    result = GetElementChildren(allElements, globalParrent);
    result = Sort(result, allElements);
    result.sort();
    return result;

  }

  public static List<String> GetSortedOrderListAsListOfStrings(String modelName,
      String sListLocationPath) {
    List<String> result = new ArrayList<String>();
    List<SortedOrderEntry> sortedList = GetSortedOrderList(modelName, sListLocationPath);
    for (SortedOrderEntry entry : sortedList) {
      result.add(entry.id);
    }
    return result;

  }
}
