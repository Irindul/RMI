package client.states;

import client.ClientInterface;
import client.network.FileSender;

public class Sourcecoller extends AbstractClientState {

  private FileSender fileSender;

  public Sourcecoller(String name) {
    super(name);
    fileSender = new FileSender(streams);
  }

  @Override
  public void subinteractions() {
    String path = ClientInterface.askFileName(false);
    fileSender.send(path);

    //todo rest
  }

}
