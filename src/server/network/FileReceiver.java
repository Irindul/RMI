package server.network;

import helpers.SocketStreams;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.StringTokenizer;

public class FileReceiver {

  private SocketStreams streams;
  private DataInputStream dataInputStream;
  private FileOutputStream fileOutputStream;

  private String fileName;
  private int size;

  public FileReceiver(SocketStreams streams) {
    this.streams = streams;

    dataInputStream = new DataInputStream(streams.getInputStream());
  }

  public Optional<String> receive() {
    try {
      streams.writeAndFlush("Ready to send file");
      parseMetadata();
      readFileFromStream();
      streams.writeAndFlush("File received");
      fileOutputStream.close();
      return Optional.of(fileName);
    } catch (IOException | NumberFormatException | NoSuchElementException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  private void parseMetadata() throws IOException {
    StringTokenizer tokenizer = new StringTokenizer(streams.readLine());
    fileName = tokenizer.nextToken();
    size = Integer.valueOf(tokenizer.nextToken());
    initializeStream();
  }

  private void initializeStream() throws FileNotFoundException {
    dataInputStream = new DataInputStream(streams.getInputStream());
    initializeFolder();
    fileOutputStream = new FileOutputStream("./resources/client/" + fileName);
  }

  private void initializeFolder() {
    File directory = new File("resources/client");
    if (!directory.exists()) {
      directory.mkdirs();
    }
  }

  private void readFileFromStream() throws IOException {
    byte[] buffer = new byte[4096];

    int read;
    int remaining = size;

    while ((read = dataInputStream.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
      remaining -= read;
      fileOutputStream.write(buffer, 0, read);
    }
  }
}
