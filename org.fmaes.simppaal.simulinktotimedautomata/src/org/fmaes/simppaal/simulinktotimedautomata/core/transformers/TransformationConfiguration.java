/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.transformers;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
public class TransformationConfiguration {

  private Collection<String> dependencyChain;
  private int encapsulationLevel;

  public TransformationConfiguration() {
    dependencyChain = new ArrayList();
    encapsulationLevel = -1;
  }

  public TransformationConfiguration(Collection<String> dChain, int encapsulationLevel) {
    dependencyChain = new ArrayList<>();
    dependencyChain.addAll(dChain);
    this.encapsulationLevel = encapsulationLevel;
  }

  public boolean hasDependencyChain() {
    return dependencyChain.size() > 0;
  }

  public Collection<String> getDependencyChain() {
    return dependencyChain;
  }

  public int getEncapsulationLevel() {
    return encapsulationLevel;
  }
}
