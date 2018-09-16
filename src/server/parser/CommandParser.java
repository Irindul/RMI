package server.parser;

import helpers.SocketStreams;
import server.states.ByteColl;
import server.states.Idle;
import server.states.ObjectColl;
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
        case "bytecoll":
          return new ByteColl(streams);
        case "objectcoll":
          return new ObjectColl(streams, true);
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
