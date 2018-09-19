package client;

import java.util.Scanner;

public class Client {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter server adress, leave blank for default (127.0.0.1) : ");
    String address = sc.nextLine();
    address = "".equals(address) ? "127.0.0.1" : address;

    System.out.println("Enter server port, leave blank for default (8080) : ");
    String portStr = sc.nextLine();

    int port = "".equals(portStr) ? 8080 : Integer.parseInt(portStr);

    ClientHandler clientHandler = new ClientHandler(address, port);
    new Thread(clientHandler).start();

  }
}
