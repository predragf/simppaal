/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.testing;

import java.util.Arrays;
import java.util.Collection;

import org.fmaes.simppaal.simulinktotimedautomata.core.configuration.ApplicationConfiguration;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.hierarchy.HierarchyNode;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.hierarchy.HierarchyTree;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.wrappers.SimulinkSystemModel;
import org.fmaes.simppaal.simulinktotimedautomata.utils.SimulinkUtils;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
public class Testing {

  /**
   * @param args
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    ApplicationConfiguration _appConfig = ApplicationConfiguration.loadConfiguration();
    System.out.println(_appConfig.getOrDefault("modelDirectory123", "defaultValue"));
    SimulinkSystemModel systemModel = new SimulinkSystemModel(_appConfig, "brake_acc_nodiv.mdl");
    System.out.println(Arrays.asList(systemModel.simulinkModelFiles));
    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");    
    System.out.println(systemModel.modelHierarcyTree.toString());
    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    Collection<HierarchyNode> ht = systemModel.modelHierarcyTree.generateBlockDependentHierarchy("brake_acc_nodiv/Veh_Speed_Estimator/Add");
    System.out.println(ht);
    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    //brake_acc_nodiv/Veh_Speed_Estimator/Add
  }
}
