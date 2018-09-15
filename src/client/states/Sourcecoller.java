package client.states;

import client.ClientInterface;
import client.network.FileSender;
import helpers.SocketStreams;

public class Sourcecoller extends AbstractClientState {

  private FileSender fileSender;

  public Sourcecoller(String name, SocketStreams streams) {
    super(name, streams);
    fileSender = new FileSender(streams);
  }

  @Override
  public void subinteractions() {
    String path = ClientInterface.getFileNameFromUser();
    fileSender.send(path);

    //todo rest
  }

}
