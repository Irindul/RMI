package client.states;

import helpers.SocketStreams;

public abstract class AbstractClientState implements ClientState {

  protected String name;
  protected SocketStreams streams;

  public AbstractClientState(String name, SocketStreams streams) {
    this.name = name;
    this.streams = streams;
  }

  @Override
  public void interact() {
    streams.writeAndFlush(name);
    subinteractions();
  }

  public abstract void subinteractions();
}
