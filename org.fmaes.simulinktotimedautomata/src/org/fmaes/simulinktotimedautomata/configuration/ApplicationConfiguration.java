package org.fmaes.simulinktotimedautomata.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

@SuppressWarnings("serial")
public class ApplicationConfiguration extends Properties {

  public ApplicationConfiguration() {
    super();
  }

  public static ApplicationConfiguration loadConfiguration(String configPath) {
    ApplicationConfiguration config = new ApplicationConfiguration();
    InputStream input = null;
    try {
      input = new FileInputStream(configPath);
      config.loadFromXML(input);
    } catch (Exception io) {
      /* log the error or smth similar */

      /* make sure that the config is at least initialized. */
      config = new ApplicationConfiguration();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (Exception e) {
          /* log the error or smth similar */
          System.err.println(e.getMessage());
        }
      }
    }
    return config;
  }

  public static ApplicationConfiguration loadConfiguration() {
    /* This is default location where the configuration shall be. */
    String appConfigFilePath = "./config/application.properties";
    return loadConfiguration(appConfigFilePath);
  }

  public void saveConfiguration(String configPath) {
    OutputStream outputStream = null;
    try {
      File configurationFile = new File(configPath);
      configurationFile.createNewFile();
      outputStream = new FileOutputStream(configurationFile, false);
      this.storeToXML(outputStream, "");
      outputStream.close();
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
    }
  }

  public void saveConfiguration() {
    String appConfigFilePath = "./config/application.properties";
    saveConfiguration(appConfigFilePath);
  }


}
