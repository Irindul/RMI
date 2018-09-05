package server;

import java.net.Socket;

public class Connection implements Runnable {
  private Socket socket;

  public Connection(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    // Handle here
  }
}
