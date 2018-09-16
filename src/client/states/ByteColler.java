package client.states;

import client.ClientInterface;
import client.network.FileSender;

public class ByteColler extends AbstractClientState {

  public ByteColler(String name) {
    super(name);
  }

  @Override
  public void subinteractions() {
    FileSender fileSender = new FileSender(this.streams);
    String filePath = ClientInterface.askFileName(true);
    fileSender.send(filePath);
  }
}
