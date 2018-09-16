package client.states;

import client.IntegerCalculator;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ObjectColler extends AbstractClientState {

  public ObjectColler(String name) {
    super(name);
  }

  @Override
  public void subinteractions() {
    IntegerCalculator calculator = new IntegerCalculator();
    try {
      ObjectOutputStream oos = new ObjectOutputStream(streams.getOutputStream());
      oos.writeObject(calculator);
      oos.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
