package client.states;

import client.ClientInterface;
import client.network.FileSender;

public class Sourcecoller extends AbstractClientState {


  public Sourcecoller(String name) {
    super(name);
  }

  @Override
  public void subinteractions() {
    FileSender fileSender = new FileSender(this.streams);
    String path = ClientInterface.askFileName(false);
    fileSender.send(path);
  }

}
