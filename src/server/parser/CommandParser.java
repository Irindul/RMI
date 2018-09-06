package server.parser;

import java.io.InputStream;
import java.io.OutputStream;
import server.states.Idle;
import server.states.Quit;
import server.states.RMIState;
import server.states.SourceColl;

public class CommandParser {

  private InputStream inFromClient;
  private OutputStream outToClient;

  public CommandParser(InputStream inFromClient, OutputStream outToClient) {
    this.inFromClient = inFromClient;
    this.outToClient = outToClient;
  }

  public RMIState parse(String command) {
    try {
      String parsed = command.split(" ")[0].toLowerCase();
      switch (parsed) {
        case "sourcecoll":
          return new SourceColl(inFromClient, outToClient);
        case "quit":
          return new Quit();
        default:
          return new Idle();
      }
    } catch (Exception e ){
      return new Quit(e.getMessage());
    }
  }
}
