package client;

import java.io.Serializable;

public class IntegerCalculator implements Serializable {

  private static final long serialVersionUID = 1L;
  private static int instances = 0;

  private int number;

  public IntegerCalculator() {
    instances++;
    number = instances;
  }

  @Override
  public String toString() {
    return "Calculator number " + number;
  }

  public static int add(Integer a, Integer b) {
    return a + b;
  }
}
