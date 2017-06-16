/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.utils;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
public class SimulinkUtils {

  public static String preProcessId(String blockId) {
    String processedId = blockId;
    if (blockId.endsWith("/")) {
      processedId = blockId.substring(0, blockId.lastIndexOf('/'));
    }
    return processedId;
  }


  public static String eliminateLastEntryOfId(String blockId) {
    blockId = preProcessId(blockId);
    int lastIndexOfSlash = blockId.lastIndexOf('/');
    String oneLevelUp = blockId;
    if (lastIndexOfSlash > 0) {
      oneLevelUp = blockId.substring(0, lastIndexOfSlash);
    }
    return oneLevelUp;
  }

  public int getNumberOfNestedLevels(String globalId) {
    String globalIdNoSlashes = globalId.replace("/", "");
    return globalId.length() - globalIdNoSlashes.length();
  }



}
