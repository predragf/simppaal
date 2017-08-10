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

  private static Collection<BlockRoutineGeneratorInterface> loadPluginsAsArray(
      Collection<File> plugins) throws MalformedURLException {
    List<BlockRoutineGeneratorInterface> pluginInstances =
        new ArrayList<BlockRoutineGeneratorInterface>();
    URL[] plugin_urls = new URL[plugins.size()];
    int index = 0;
    for (File plugin : plugins) {
      plugin_urls[index] = plugin.toURI().toURL();
      index++;
    }
    URLClassLoader urlClassLoader = new URLClassLoader(plugin_urls);
    ServiceLoader<BlockRoutineGeneratorInterface> sl =
        ServiceLoader.load(BlockRoutineGeneratorInterface.class, urlClassLoader);
    Iterator<BlockRoutineGeneratorInterface> apit = sl.iterator();
    while (apit.hasNext()) {
      pluginInstances.add(apit.next());
    }
    return pluginInstances;
  }

  public static HashMap<String, BlockRoutineGeneratorInterface> loadPlugins(
      Collection<File> pluginFiles) throws MalformedURLException {
    HashMap<String, BlockRoutineGeneratorInterface> plugins =
        new HashMap<String, BlockRoutineGeneratorInterface>();
    ArrayList<File> files = new ArrayList<File>();
    files.addAll(pluginFiles);
    Collection<BlockRoutineGeneratorInterface> pluginClasses = loadPluginsAsArray(pluginFiles);
    int index = 0;
    for (BlockRoutineGeneratorInterface pluginInstance : pluginClasses) {
      File pluginFile = files.get(index);
      String pluginName = pluginFile.getName();
      pluginName = pluginName.substring(0, pluginName.indexOf('.'));
      plugins.put(pluginName, pluginInstance);
      index++;
    }

    return plugins;
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
