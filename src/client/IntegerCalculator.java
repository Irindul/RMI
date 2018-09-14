package client;

import java.io.Serializable;

public class IntegerCalculator implements Serializable {
  private static final long serialVersionUID = 1L;

  public static int add(Integer a, Integer b) {
    return a+b;
  }
}
