package org.fmaes.simppaal.simulinktotimedautomata.platformextender;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;


public class BlockRoutineGeneratorPluginManager {

  private static Collection<File> getPluginsFromDirectory(String pluginDirectory) {
    File loc = new File(pluginDirectory);
    File[] plugins = loc.listFiles(new FileFilter() {
      public boolean accept(File file) {
        return file.getPath().toLowerCase().endsWith(".jar");
      }
    });
    Collection<File> pluginCollection = new ArrayList<File>();
    for (File plugin : plugins) {
      pluginCollection.add(plugin);
    }
    return pluginCollection;
  }

  private static HashMap<String, BlockRoutineGeneratorInterface> loadPlugins(
      Collection<File> plugins) throws MalformedURLException {
    HashMap<String, BlockRoutineGeneratorInterface> pluginInstances =
        new HashMap<String, BlockRoutineGeneratorInterface>();

    for (File plugin : plugins) {
      URL url = plugin.toURI().toURL();
      URLClassLoader urlClassLoader = new URLClassLoader(new URL[] {url});
      ServiceLoader<BlockRoutineGeneratorInterface> serviceLoader =
          ServiceLoader.load(BlockRoutineGeneratorInterface.class, urlClassLoader);
      Iterator<BlockRoutineGeneratorInterface> apit = serviceLoader.iterator();
      if (apit.hasNext()) {
        pluginInstances.put(plugin.getName(), apit.next());
      }
    }

    return pluginInstances;
  }

  public static HashMap<String, BlockRoutineGeneratorInterface> loadPluginsFromDirectory(
      String pluginDir) {
    HashMap<String, BlockRoutineGeneratorInterface> loadedPlugins;
    Collection<File> pluginFiles = getPluginsFromDirectory(pluginDir);
    try {
      loadedPlugins = loadPlugins(pluginFiles);
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      loadedPlugins = new HashMap<>();
    }
    return loadedPlugins;
  }
}
