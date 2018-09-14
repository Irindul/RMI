package server.parser;

import helpers.SocketStreams;
import server.states.Idle;
import server.states.Quit;
import server.states.RMIState;
import server.states.SourceColl;

public class CommandParser {

  private SocketStreams streams;

  public CommandParser(SocketStreams streams) {
    this.streams = streams;
  }

  public RMIState parse(String command) {
    try {
      String parsed = command.split(" ")[0].toLowerCase();
      switch (parsed) {
        case "sourcecoll":
          return new SourceColl(streams);
        case "quit":
          return new Quit();
        default:
          return new Idle();
      }
    } catch (Exception e) {
      return new Quit(e.getMessage());
    }
  }
}
