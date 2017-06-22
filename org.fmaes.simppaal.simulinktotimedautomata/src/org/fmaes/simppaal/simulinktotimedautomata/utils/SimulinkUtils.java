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


  public static String trimSimulinkIdFromBack(String blockId) {
    blockId = preProcessId(blockId);
    int lastIndexOfSlash = blockId.lastIndexOf('/');
    String idTrimedFromBack = blockId;
    if (lastIndexOfSlash > 0) {
      idTrimedFromBack = blockId.substring(0, lastIndexOfSlash);
    }
    return idTrimedFromBack;
  }

  public static String trimSimulinkIdFromBack(String blockId, int numberOfEntriesToBeTrimmed) {
    String trimmedId = blockId;
    while (numberOfEntriesToBeTrimmed > 0) {
      trimmedId = trimSimulinkIdFromBack(trimmedId);
      numberOfEntriesToBeTrimmed--;
    }
    return trimmedId;
  }

  public static String trimSimulinkIdFromFront(String blockId) {
    blockId = preProcessId(blockId);
    int firstIndexOfSlash = blockId.indexOf('/');
    String idTrimedFromFront = blockId;
    if (firstIndexOfSlash > 0) {
      idTrimedFromFront = blockId.substring(firstIndexOfSlash + 1);
    }
    return idTrimedFromFront;
  }

  public static String trimSimulinkIdFromFront(String blockId, int numberOfEntriesToBeTrimmed) {
    String trimmedId = blockId;
    while (numberOfEntriesToBeTrimmed > 0) {
      trimmedId = trimSimulinkIdFromFront(trimmedId);
      numberOfEntriesToBeTrimmed--;
    }
    return trimmedId;
  }

  public static int getNumberOfLevels(String globalId) {
    return globalId.split("/").length;
  }

  public static boolean compareStringsIgnoreCase(String first, String second) {
    return (first != null && second != null && first.toLowerCase().equals(second.toLowerCase()))
        ? true : false;
  }

  public static String stripExtension(String nameWithExtension) {
    int lastDotPosition = nameWithExtension.lastIndexOf('.');
    String nameWithouthExtension = nameWithExtension;
    if (lastDotPosition > 0) {
      nameWithouthExtension = nameWithExtension.substring(0, lastDotPosition);
    }
    return nameWithouthExtension;
  }

}
