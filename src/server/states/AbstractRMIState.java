package server.states;

import helpers.SocketStreams;
import server.network.FileReceiver;

public abstract class AbstractRMIState implements RMIState {

  protected SocketStreams streams;
  protected FileReceiver receiver;

  public AbstractRMIState(SocketStreams streams) {
    this.streams = streams;
    receiver = new FileReceiver(streams);
  }
}
