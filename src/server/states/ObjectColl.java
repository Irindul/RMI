package server.states;

import helpers.SocketStreams;
import java.util.Optional;
import server.invokation.InvokaterWrapper;

public class ObjectColl implements RMIState {

  private Class<?> clazz;
  private InvokaterWrapper invokater;
  protected SocketStreams streams;

  public ObjectColl(SocketStreams streams) {
    this.streams = streams;
    invokater = new InvokaterWrapper(streams);
  }

  public void setClazz(Class<?> clazz) {
    this.clazz = clazz;
  }

  @Override
  public void execute() {
    Optional<Integer> optionalResult = invokater.load(clazz).invoke();
    optionalResult.ifPresent(result -> streams.writeAndFlush(result.toString()));
  }
}
