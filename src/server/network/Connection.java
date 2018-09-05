package server.network;

import common.CustomRunnable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class Connection implements CustomRunnable {

  private static Logger LOGGER = Logger.getLogger("Connection");

  private Socket socket;
  private InputStream inFromClient;
  private OutputStream outToClient;
  private BufferedReader in;
  private PrintWriter out;

  public Connection(Socket socket) {
    this.socket = socket;
    initializeStreams();
  }

  private void initializeStreams() {
    try {
      inFromClient = socket.getInputStream();
      outToClient = socket.getOutputStream();
      in = new BufferedReader(new InputStreamReader(inFromClient));
      out = new PrintWriter(new OutputStreamWriter(outToClient));

      LOGGER.info("Connection started on " + Thread.currentThread().getName() + " with " +
          socket.getInetAddress().toString());
    } catch (IOException e) {
      e.printStackTrace();
      close();
    }

  }

  private void close() {
    try {
      if (inFromClient != null) {
        inFromClient.close();
      }
      if (outToClient != null) {
        outToClient.close();
      }
      if (in != null) {
        in.close();
      }
      if (out != null) {
        out.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    interupt();
  }

  @Override
  public void execute() {
    //handle here
    System.out.println("Closing");
    close();
  }
}
