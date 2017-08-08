/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
public class DiskUtils {

  public static Collection<File> getAllModelFilesFromDisk(String location) {
    File folder = new File(location);
    Collection<File> modelFiles = new ArrayList<File>();
    for (File fileItem : folder.listFiles()) {
      // if it is not file, go to the next one
      if (!fileItem.isFile()) {
        continue;
      }
      String absoluteFilePath = fileItem.getAbsolutePath();
      if ((absoluteFilePath.endsWith(".mdl") || absoluteFilePath.endsWith(".slx"))) {
        modelFiles.add(fileItem);
      }
    }
    return modelFiles;
  }

  public static Collection<String> getAllModelFileNamesFromDisk(String location) {
    Collection<String> modelFileNames = new ArrayList<String>();
    Collection<File> modelFiles = getAllModelFilesFromDisk(location);
    for (File modelFile : modelFiles) {
      modelFileNames.add(modelFile.getAbsolutePath());
    }
    return modelFileNames;
  }

  public static void saveFileAsTxt(String fileLocation, String fileContents) {
    if (!fileLocation.endsWith(".txt")) {
      fileLocation = fileLocation += ".txt";
    }
    try (PrintStream out = new PrintStream(new FileOutputStream(fileLocation))) {
      out.print(fileContents);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      System.out.println(e.getMessage());
    }
  }

}
