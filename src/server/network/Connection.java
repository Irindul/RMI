package server.network;

import common.LoopingRunnable;
import helpers.SocketStreams;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;
import server.parser.CommandParser;
import server.states.Idle;
import server.states.RMIState;

public class Connection implements LoopingRunnable {

  private static Logger LOGGER = Logger.getLogger("Connection");

  private Socket socket;
  private SocketStreams streams;
  private RMIState state;
  private CommandParser parser;
  private boolean open = true;

  public Connection(Socket socket) {
    this.socket = socket;
    initializeStreams();
    state = new Idle();
    parser = new CommandParser(streams);
  }

  private void initializeStreams() {
    try {
      streams = new SocketStreams(socket.getOutputStream(), socket.getInputStream());
      LOGGER.info("Connection started on " + Thread.currentThread().getName() + " with " +
          socket.getInetAddress().toString());
    } catch (IOException e) {
      e.printStackTrace();
      close();
    }

  }

  @Override
  public void close() {
    try {
      streams.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    interupt();
    if(open) {
      LOGGER.info("Connection closed");
      open = false;
    }
  }

  @Override
  public void execute() {
    try {
      LOGGER.info("Waiting for commands");
      String command = streams.readLine();
      if (command != null) {
        LOGGER.info("Client ask for : " + command);
        state = parser.parse(command);
        state.execute();
      } else {
        close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
