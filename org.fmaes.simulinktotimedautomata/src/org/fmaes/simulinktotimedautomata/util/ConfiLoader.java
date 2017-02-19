package org.fmaes.simulinktotimedautomata.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ConfiLoader {

  public static Properties loadConfiguration(String configPath) {
    Properties config = new Properties();
    InputStream input = null;
    try {
      input = new FileInputStream(configPath);
      config.loadFromXML(input);
    } catch (Exception io) {
      /* log the error or smth similar */
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (Exception e) {
          /* log the error or smth similar */
        }
      }
    }
    return config;
  }

  public static void saveConfiguration(Properties config, String confingFilePath) {
    OutputStream output = null;
    try {
      output = new FileOutputStream(confingFilePath);
      config.storeToXML(output, null);
    } catch (Exception io) {
      /* log the error or smth similar */
    } finally {
      if (output != null) {
        try {
          output.close();
        } catch (Exception e) {
          /* log the error or smth similar */
        }
      }
    }
  }
}
