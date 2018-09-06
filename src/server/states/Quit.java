package server.states;

import java.util.logging.Logger;

public class Quit implements RMIState {

  private static Logger LOGGER = Logger.getLogger("Quit");
  private String message;

  public Quit() {
    this("Quiting connection");
  }

  public Quit(String message) {
    this.message = message;
  }

  @Override
  public void execute() {
    LOGGER.info(message);
    Thread.currentThread().interrupt();
  }
}
