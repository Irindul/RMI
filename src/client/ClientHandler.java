package client;

import client.states.ClientState;
import client.states.ClientStateFactory;
import common.LoopingRunnable;
import helpers.SocketStreams;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements LoopingRunnable {

  private Socket socket;
  private SocketStreams streams;
  private ClientInterface clientInterface;
  private String action;

  public ClientHandler(Socket socket) {
    try {
      this.socket = socket;
      this.streams = new SocketStreams(socket.getOutputStream(), socket.getInputStream());
      clientInterface = new ClientInterface();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void close() {

  }

  @Override
  public void execute() {
    action = clientInterface.selectState();
    ClientState state = ClientStateFactory.getClientState(action);
    state.interact();

  }

  private void sendAction() {
    streams.writeAndFlush(action);
  }
}
