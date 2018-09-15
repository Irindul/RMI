package client.network;

import helpers.SocketStreams;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void openFileInputStrem(String absolutePath) throws IOException{
    fis = new FileInputStream(absolutePath);
  }

  private String getFileName(String absolutePath) {
    String[] parts = absolutePath.split(File.separator);
    return parts[parts.length - 1];
  }

  private void ack() throws IOException{
    String ack = streams.readLine();
    System.out.println(ack);

  }

  private void sendFile() throws IOException {
    byte[] buffer = new byte[4096];
    int read = 0;
    while ((read = fis.read(buffer)) > 0) {
      dos.write(buffer, 0, read);
    }
  }

  public void close() {
    try {
      dos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
