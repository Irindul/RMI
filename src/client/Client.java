package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class Client {

  public static void main(String[] args) {
    try {
      Socket socket = new Socket("127.0.0.1", 8080);
      OutputStream out = socket.getOutputStream();
      InputStream in = socket.getInputStream();
      DataOutputStream dos = new DataOutputStream(out);
      FileInputStream fis = new FileInputStream("/Users/irindul/Desktop/test.txt");

      PrintWriter pw = new PrintWriter(out);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));

      pw.write("sourcecoll\n");
      pw.flush();
      System.out.println(br.readLine());

      String message = "calc.java " + fis.getChannel().size() + "\n";
      System.out.println(message);

      pw.write(message);
      pw.flush();
      byte[] buffer = new byte[4096];
      int read = 0;
      while ((read = fis.read(buffer)) > 0) {
        dos.write(buffer, 0, read);
      }

      System.out.println(br.readLine());

      fis.close();
      dos.close();
      pw.close();
      br.close();
      in.close();
      out.close();
      socket.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
