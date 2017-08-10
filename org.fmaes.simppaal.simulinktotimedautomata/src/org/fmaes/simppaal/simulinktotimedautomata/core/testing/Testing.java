/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.testing;

import java.util.Collection;

import org.conqat.lib.simulink.model.SimulinkBlock;
import org.conqat.lib.simulink.model.SimulinkModel;
import org.conqat.lib.simulink.model.SimulinkObject;
import org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces.UppaalAutomatonInterface;
import org.fmaes.j2uppaal.datastructures.uppaalstructures.UppaalAutomaton;
import org.fmaes.j2uppaal.datastructures.uppaalstructures.UppaalDocument;
import org.fmaes.simppaal.simulinktotimedautomata.core.configuration.ApplicationConfiguration;
import org.fmaes.simppaal.simulinktotimedautomata.core.loaders.SimulinkModelLoader;
import org.fmaes.simppaal.simulinktotimedautomata.core.transformers.SimulinkModelTransformer;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.Neighbour;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.SimulinkBlockWrapper;
import org.fmaes.simppaal.simulinktotimedautomata.core.types.SimulinkModelWrapper;
import org.fmaes.simppaal.simulinktotimedautomata.sorder.SListParser;
import org.fmaes.simppaal.simulinktotimedautomata.sorder.SortedOrderEntry;
import org.fmaes.simppaal.simulinktotimedautomata.sorder.SortedOrderList;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
public class Testing {

  /**
   * @param args
   */

  private static int notFound;

  @SuppressWarnings("unused")
  public static void main(String[] args) {

    long startTime = System.currentTimeMillis();

    ApplicationConfiguration appConfig = ApplicationConfiguration.loadConfiguration();
    SimulinkModelTransformer smt = new SimulinkModelTransformer(appConfig);
    SimulinkModelLoader modelLoader = new SimulinkModelLoader(appConfig);
    SimulinkModelWrapper bbw = modelLoader.loadAndWrapSimulinkModelByName("bbw.mdl");
    SortedOrderList sList = SListParser.GetSortedOrderList("bbw", "./models/simulink/BBW/bbw.txt");
    UppaalDocument uppaalModel = (UppaalDocument) smt.generateUppaalModel(bbw, sList);
    System.out.println(uppaalModel.getAllAutomata().size());
    
    uppaalModel.saveToFile("/Users/pfj01/Desktop/model.xml");
    long endTime = System.currentTimeMillis();
    long elapsedTime = (endTime - startTime) / 1000;
    System.out.println(String.format("The model parsing took %s seconds", elapsedTime));
    System.out.println("pause");
  }

  private static void printN(SimulinkBlockWrapper bw, String id) {

    if (bw.exists()) {
      System.out.println(String.format("%s (%d) has %d predecessors", bw.getId(),
          bw.getExecutionOrderNumber(), bw.getPredecessors().size()));
      System.out.println(String.format("Sample time is: %s", bw.getSampleTime()));
      Collection<Neighbour> neighbours = bw.getPredecessors();
      neighbours = bw.getPredecessors();
      for (Neighbour neighbour : neighbours) {
        System.out.println("* \t" + neighbour.getSourceSimulinkBlock().getId());
      }
    } else {
      System.err.println(String.format("%s was not found", id));
      notFound++;
    }
    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
  }

}
