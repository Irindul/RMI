package client;

import client.states.AbstractClientState;
import client.states.ClientState;
import client.states.ClientStateFactory;
import common.LoopingRunnable;
import helpers.SocketStreams;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.StringTokenizer;
import server.states.Quit;

public class ClientHandler implements LoopingRunnable {

  private Socket socket;
  private SocketStreams streams;
  private ClientInterface clientInterface;
  private String action;

  public ClientHandler(String ip, int port) {
    try {
      this.socket = socket = new Socket(ip, port);
      this.streams = new SocketStreams(socket.getOutputStream(), socket.getInputStream());
      clientInterface = new ClientInterface();
    } catch (ConnectException e) {
      System.out.println("The server could not be reached");
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
  public void execute() {
    action = clientInterface.selectState();
    ClientState state = ClientStateFactory.getClientState(action);
    if (state instanceof AbstractClientState) {
      ((AbstractClientState)state).setStreams(streams);
    }

    if(state instanceof Quit) {
      this.interupt();
    }
    state.interact();

    String methodNameAndArgs = ClientInterface.askMethod();
    streams.writeAndFlush(methodNameAndArgs);

    try {
      Integer result = Integer.valueOf(streams.readLine());
      System.out.println("Result of  " + getPrototypeMethod(methodNameAndArgs) + " is " + result);
      System.out.println();
      Thread.sleep(500);
    } catch (IOException e) {
      interupt();
    } catch (InterruptedException e) {
      //Not a problem
    }
  }

  private String getPrototypeMethod(String methodNameAndArgs) {
    StringTokenizer tokenizer = new StringTokenizer(methodNameAndArgs);
    StringBuilder prototype = new StringBuilder(tokenizer.nextToken());
    prototype.append("(");
    while(tokenizer.hasMoreTokens()) {
      String token = tokenizer.nextToken();
      prototype.append(token);
      if(tokenizer.hasMoreTokens()) {
        prototype.append(", ");
      }
    }

    prototype.append(")");

    return prototype.toString();
  }
}
