package client.states;

public class ClientStateFactory {
  public static ClientState getClientState(String action) {
    switch (action) {
      case "sourcecoll":
        return new Sourcecoller();
      default:
        return new Idle();
    }
  }
}
