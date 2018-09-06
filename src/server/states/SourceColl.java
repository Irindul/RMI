package server.states;

public class SourceColl implements RMIState {

  @Override
  public void execute() {
    System.out.println("Ready to receive source file");
  }
}
