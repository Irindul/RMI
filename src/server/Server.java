package server;

import server.network.ConnectionHandler;

public class Server {
  public static void main(String[] args) {
    ConnectionHandler handler = new ConnectionHandler();
    new Thread(handler).start();
  }
}
