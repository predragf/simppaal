/**
 * 
 */
package org.fmaes.simppaal.simulinktotimedautomata.core.transformers;

/**
 * @author Predrag Filipovikj (predrag.filipovikj@mdh.se)
 *
 */
public class AutomatonData {

  private String name;

  private String sampleTime;

  private String blockRoutine;

  private String initialization;

  private String dafnyProcedure;

  private String declarationStatement;

  private String signalDeclaration;

  private String offset;

  private String iAT;

  public AutomatonData() {
    name = "";
    sampleTime = "";
    setInitialization("");
    blockRoutine = "";
    dafnyProcedure = "";
    declarationStatement = "";
    signalDeclaration = "";
    offset = "";
    iAT = "";
  }

  public AutomatonData(String name, String sTime, String initialization, String bRoutine,
      String dafnyProcedure, String declarationStatement, String signalDeclaration, String offset,
      String iat) {
    this.name = name;
    this.sampleTime = sTime;
    this.setInitialization(initialization);
    this.blockRoutine = bRoutine;
    this.dafnyProcedure = dafnyProcedure;
    this.declarationStatement = declarationStatement;
    this.signalDeclaration = signalDeclaration;
    this.offset = offset;
    this.iAT = iat;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSampleTime() {
    return sampleTime;
  }

  public void setSampleTime(String sampleTime) {
    this.sampleTime = sampleTime;
  }

  public String getBlockRoutine() {
    return blockRoutine;
  }

  public void setBlockRoutine(String blockRoutine) {
    this.blockRoutine = blockRoutine;
  }

  public String getDafnyProcedure() {
    return dafnyProcedure;
  }

  public void setDafnyProcedure(String dafnyProcedure) {
    this.dafnyProcedure = dafnyProcedure;
  }

  public String getDeclarationStatement() {
    return declarationStatement;
  }

  public void setDeclarationStatement(String declarationStatement) {
    this.declarationStatement = declarationStatement;
  }

  public String getSignalDeclaration() {
    return signalDeclaration;
  }

  public void setSignalDeclaration(String signalDeclaration) {
    this.signalDeclaration = signalDeclaration;
  }

  public String getOffset() {
    return offset;
  }

  public void setOffset(String offset) {
    this.offset = offset;
  }

  public String getiAT() {
    return iAT;
  }

  public void setiAT(String iAT) {
    this.iAT = iAT;
  }

  public String getInitialization() {
    return initialization;
  }

  public void setInitialization(String initialization) {
    this.initialization = initialization;
  }

}
