package org.fmaes.simulinktotimedautomata.platformextender.blockroutinegenerator.ratetransition;

public class PortData {

  public double ts;
  public double offset;

  public PortData(Double _ts, Double _offset) {
    ts = _ts;
    offset = _offset;
  }

  public PortData() {
    ts = offset = -1.0;
  }

}
