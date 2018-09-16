package client;

import java.io.IOException;
import java.net.Socket;

public class Client {

  public static void main(String[] args) {
    ClientHandler clientHandler = new ClientHandler("127.0.0.1", 8080);
    new Thread(clientHandler).start();

  }
}
