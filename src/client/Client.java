package client;

import client.network.FileSender;
import helpers.SocketStreams;
import java.io.File;
import java.io.IOException;
import java.net.Socket;


public class Client {

  public static void main(String[] args) {
    try {
      Socket socket = new Socket("127.0.0.1", 8080);
      SocketStreams streams = new SocketStreams(socket.getOutputStream(), socket.getInputStream());

      streams.writeAndFlush("sourcecoll");

      String pwd = System.getProperty("user.dir");
      pwd += File.separator;
      String absolutePath = pwd += "src/client/IntegerCalculator.java";

      FileSender receiver = new FileSender(streams);
      receiver.send(absolutePath);

      streams.writeAndFlush("add 5 3");

      String result = streams.readLine();
      System.out.println("Result is : " + result);

      streams.writeAndFlush("quit");

      receiver.close();
      streams.close();
      socket.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
