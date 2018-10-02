package server.network;

import common.LoopingRunnable;
import helpers.Env;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ConnectionHandler implements LoopingRunnable {

  private final static Logger LOGGER = Logger.getLogger("ConnectionHandler");
  private ServerSocket serverSocket;

  public ConnectionHandler() {
    int port = Integer.valueOf(Env.getEnvOrDefault("RMI_PORT", "8080"));
    try {
      serverSocket = new ServerSocket(port);
      System.out.println("Listening on port " + port);

    } catch (BindException e) {
      LOGGER.severe("Port " + port + " already in use");
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
