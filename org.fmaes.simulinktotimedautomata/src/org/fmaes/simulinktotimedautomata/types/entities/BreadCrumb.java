/**
 * 
 */
package org.fmaes.simulinktotimedautomata.types.entities;

import java.util.ArrayList;

/**
 * @author pfj01
 *
 */
public class BreadCrumb extends ArrayList<String> {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public BreadCrumb() {
    super();
  }

  @Override
  public boolean add(String entry) {
    if (entry != null && entry.length() > 1) {
      super.add(entry);
    }
    return true;
  }

}
