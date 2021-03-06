package client.network;

import helpers.SocketStreams;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;

public class FileSender {

  private SocketStreams streams;
  private FileInputStream fis;
  private DataOutputStream dos;

  public FileSender(SocketStreams streams) {
    this.streams = streams;
    dos = new DataOutputStream(streams.getOutputStream());
  }

  public void send(String absolutePath) {
    String fileName = getFileName(absolutePath);

    try {
      openFileInputStrem(absolutePath);
      ack();
      streams.writeAndFlush(fileName + " " + fis.getChannel().size());
      sendFile();
      ack();
      fis.close();
    } catch (FileNotFoundException e) {
      System.out.println("The file " + absolutePath + " could not be open");
      Thread.currentThread().interrupt();
    } catch (IOException e) {
      System.out.println("There was a connection problem");
      Thread.currentThread().interrupt();
    }
  }

  private void openFileInputStrem(String absolutePath) throws FileNotFoundException {
    fis = new FileInputStream(absolutePath);
  }

  private String getFileName(String absolutePath) {
    String pattern = Pattern.quote(File.separator);
    String[] parts = absolutePath.split(pattern);
    return parts[parts.length - 1];
  }

  private void ack() throws IOException {
    String ack = streams.readLine();
    System.out.println(ack);
  }

  private void sendFile() throws IOException {
    byte[] buffer = new byte[4096];
    int read = 0;

    while ((read = fis.read(buffer)) > 0) {
      streams.getOutputStream().write(buffer, 0, read);
    }

   streams.getOutputStream().flush();
  }

  public void close() {
    try {
      dos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
