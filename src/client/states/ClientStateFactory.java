package client.states;

public class ClientStateFactory {

  public static ClientState getClientState(String action) {
    switch (action) {
      case "sourcecoll":
        return new Sourcecoller(action);
      case "bytecoll":
        return new ByteColler(action);
      case "objectcoll":
        return new ObjectColler(action);
      case "quit":
        return new Quiter();
      default:
        return new Idle();
    }
  }
}
