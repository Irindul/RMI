package server.states;

import client.IntegerCalculator;
import helpers.SocketStreams;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Optional;
import server.invokation.InvokaterWrapper;

public class ObjectColl extends AbstractRMIState {

  private InvokaterWrapper invokater;
  private Class<?> clazz;
  private boolean shouldReceiveSerializable;

  public ObjectColl(SocketStreams streams, boolean shouldReceiveSerializable) {
    super(streams);
    invokater = new InvokaterWrapper(streams);
    this.shouldReceiveSerializable = shouldReceiveSerializable;
  }

  public void setClazz(Class<?> clazz) {
    this.clazz = clazz;
  }

  @Override
  public void execute() {
    if(shouldReceiveSerializable) {
      receiveSerializable();
    }

    Optional<Integer> optionalResult = invokater.load(clazz).invoke();
    optionalResult.ifPresent(result -> streams.writeAndFlush(result.toString()));
  }

  private void receiveSerializable() {
    try {
      ObjectInputStream ois = new ObjectInputStream(streams.getInputStream());
      Object obj = ois.readObject();
      IntegerCalculator calculator = (IntegerCalculator) obj;
      this.setClazz(calculator.getClass());
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }

  }
}
