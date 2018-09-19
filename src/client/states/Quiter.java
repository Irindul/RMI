package client.states;

public class Quiter extends AbstractClientState {

  public Quiter(String name) {
    super(name);
  }

  @Override
  public void interact() {

  }

  @Override
  public void subinteractions() {
    Thread.currentThread().interrupt();
  }
}
