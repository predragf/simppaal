package org.fmaes.simulinktotimedautomata.platformextender;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.fmaes.simulinktotimedautomata.blockroutinegenerator.BlockRoutineGeneratorInterface;
import org.fmaes.simulinktotimedautomata.util.Util;

public class BlockRoutineGeneratorPluginManager {

  public static String pluginDirectory = "./plugins/blockRoutine/Generators";

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

  public static Collection<BlockRoutineGeneratorInterface> loadPlugins(Collection<File> plugins)
      throws MalformedURLException {
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

  public static BlockRoutineGeneratorInterface loadPluginByName(String pluginName) {
    List<File> matchingPlugins = new ArrayList<File>();
    Collection<File> allPLugins = getPluginsFromDirectory(pluginDirectory);
    if (!Util.stringNullOrEmpty(pluginName)) {
      for (File plugin : allPLugins) {
        if (plugin.getPath().toLowerCase()
            .endsWith(String.format("%s.jar", pluginName.toLowerCase()))) {
          matchingPlugins.add(plugin);
        }
      }
    }

    BlockRoutineGeneratorInterface matchingPlugin = null;
    try {
      Iterator<BlockRoutineGeneratorInterface> blockRoutineGeneratorIterator =
          loadPlugins(matchingPlugins).iterator();
      if (blockRoutineGeneratorIterator.hasNext()) {
        matchingPlugin = blockRoutineGeneratorIterator.next();
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      matchingPlugin = null;
      System.err.println(e.getMessage());
    }
    return matchingPlugin;
  }

}
