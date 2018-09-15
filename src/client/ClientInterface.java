package client;

import java.util.ArrayList;
import java.util.Scanner;

public class ClientInterface {

  private ArrayList<String> actions;

  public ClientInterface(ArrayList<String> actions) {
    this.actions = actions;
  }

  public ClientInterface() {
    actions = new ArrayList<>();
    actions.add("1) SourceColl (send a .java file)");
    actions.add("2) ByteColl (send a .class file)");
    actions.add("3) ObjectColl (send a serialized java file)");
    actions.add("4) Quit");
  }

  public String selectState() {
    displayActions();
    Scanner sc = new Scanner(System.in);
    int choice = sc.nextInt();
    String action;
    switch (choice) {
      case 1:
        action = "sourcecoll";
        break;
      case 2:
        action = "bytecoll";
        break;
      case 3:
        action = "objectcoll";
        break;
      default:
        action = "quit";
        break;
    }

    return action;

  }

  private void displayActions() {
    System.out.println("Which action would like to do on the server ?");
    actions.forEach(System.out::println);
  }

}
