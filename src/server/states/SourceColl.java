package server.states;

import java.io.InputStream;
import java.io.OutputStream;
import server.network.FileReceiver;

public class SourceColl implements RMIState {

  private FileReceiver fileReceiver;

  public SourceColl(InputStream inFromClient, OutputStream outToClient) {
    fileReceiver = new FileReceiver(inFromClient, outToClient);
  }

  @Override
  public void execute() {
    fileReceiver.receive();
  }
}
