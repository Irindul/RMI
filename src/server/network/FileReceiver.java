package server.network;

import helpers.SocketStreams;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class FileReceiver {

  private static final Logger LOGGER = Logger.getLogger("FileReceiver");
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

      String ack = "File received";
      streams.writeAndFlush(ack);
      fileOutputStream.close();

      LOGGER.info(ack);
      return Optional.of(fileName);
    } catch (SocketException e) {
      String message = e.getLocalizedMessage();
      if ("connection reset".equals(message)) {
        LOGGER.info(message);
        Thread.currentThread().interrupt();
      }
      return Optional.empty();
    } catch (IOException e) {
      System.out.println(LOGGER.getLevel());
      LOGGER.severe(e.getMessage());
      Thread.currentThread().interrupt();
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
      LOGGER.info("The directory structure was created");
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
    fileOutputStream.flush();
  }
}
