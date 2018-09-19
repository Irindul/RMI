package client;

import client.states.AbstractClientState;
import client.states.ClientState;
import client.states.ClientStateFactory;
import client.states.Quiter;
import common.LoopingRunnable;
import helpers.SocketStreams;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientHandler implements LoopingRunnable {

  private Socket socket;
  private SocketStreams streams;
  private ClientInterface clientInterface;
  private String action;

  public ClientHandler(String ip, int port) {
    try {
      this.socket = new Socket(ip, port);
      this.streams = new SocketStreams(socket.getOutputStream(), socket.getInputStream());
      clientInterface = new ClientInterface();
    } catch (ConnectException e) {
      System.out.println("The server could not be reached. Make sure you verify address and port : ");
      System.out.println("Address : " + ip);
      System.out.println("Port : " + port);
      System.out.println("Ensure the server is started as well");
      this.interupt();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void close() {
    try {
      streams.close();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    execute();
    close();
  }

  @Override
  public void execute() {
    if(clientInterface == null) {
      return;
    }
    action = clientInterface.selectState();
    ClientState state = ClientStateFactory.getClientState(action);
    if (state instanceof AbstractClientState) {
      ((AbstractClientState) state).setStreams(streams);
    }

    if (state instanceof Quiter) {
      this.interupt();
      return;
    }
    state.interact();
    if (this.isInterrupted()) {
      return;
    }

    askMethod();
  }

  private void askMethod() {
    boolean loop = false;
    String message = "";
    do {
      loop = false;
      String methodNameAndArgs = ClientInterface.askMethod();
      streams.writeAndFlush(methodNameAndArgs);

      try {
        message = streams.readLine();
        Integer result = Integer.valueOf(message);
        System.out.println("Result of  " + getPrototypeMethod(methodNameAndArgs) + " is " + result);
      } catch (NumberFormatException e) {
        if ("no such method".equals(message.toLowerCase())) {
          System.out.println(
              "The method does not exist ! "
                  + "Please ensure you have the correct arguments/method name"
          );
          loop = true;
        }
      } catch (IOException e) {
        interupt();
      }
    } while (loop);
  }

  private String getPrototypeMethod(String methodNameAndArgs) {
    StringTokenizer tokenizer = new StringTokenizer(methodNameAndArgs);
    StringBuilder prototype = new StringBuilder(tokenizer.nextToken());
    prototype.append("(");
    while (tokenizer.hasMoreTokens()) {
      String token = tokenizer.nextToken();
      prototype.append(token);
      if (tokenizer.hasMoreTokens()) {
        prototype.append(", ");
      }
    }

    prototype.append(")");

    return prototype.toString();
  }
}
