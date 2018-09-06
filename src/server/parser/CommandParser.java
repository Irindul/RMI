package server.parser;

import server.states.Idle;
import server.states.Quit;
import server.states.RMIState;
import server.states.SourceColl;

public class CommandParser {
  public static RMIState parse(String command) {
    try {
      String parsed = command.split(" ")[0].toLowerCase();
      switch (parsed) {
        case "sourcecoll":
          return new SourceColl();
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
