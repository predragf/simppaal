package org.fmaes.j2uppaal.datastructures.uppaalstrcutures.interfaces;

import java.util.Collection;

import org.fmaes.j2uppaal.datastructures.base.CompositeUppaalElement;

public interface UppaalDocumentInterface {

  public String getDeclaration();

  public void setDeclaration(String declaration);

  public UppaalAutomatonInterface getAutomatonByName(String automatonName);

  public void addAutomaton(UppaalAutomatonInterface automaton);

  public Collection<UppaalAutomatonInterface> getAllAutomata();

  public void setSystem(String system);

  public String getSystem();

  public void setQueries(String queries);

  public String getQueries();

  public void saveToFile(String filePath);

  public CompositeUppaalElement clone();
}
