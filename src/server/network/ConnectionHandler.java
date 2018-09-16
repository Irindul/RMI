package server.network;

import common.LoopingRunnable;
import helpers.Env;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandler implements LoopingRunnable {

  private ServerSocket serverSocket;

  public ConnectionHandler() {
    int port = Integer.valueOf(Env.getEnvOrDefault("RMI_ENV", "8080"));
    try {
      serverSocket = new ServerSocket(port);
      System.out.println("Listening on port " + port);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void close() {
    try {
      if (serverSocket != null) {
        serverSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void execute() {
    try {
      Socket socket = serverSocket.accept();
      Connection connection = new Connection(socket);

      new Thread(connection).start();
    } catch (IOException e) {
      System.out.println("Error when opening socket " + e.getMessage());
    }
  }
}
