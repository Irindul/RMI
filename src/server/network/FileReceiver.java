package server.network;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class FileReceiver {

  private InputStream inStream;
  private OutputStream outStream;
  private DataInputStream dataInputStream;
  private FileOutputStream fileOutputStream;
  private BufferedReader in;
  private PrintWriter out;
  private String fileName;
  private int size;

  public FileReceiver(InputStream in, OutputStream out) {
    this.inStream = in;
    this.outStream = out;
    dataInputStream = new DataInputStream(in);
    this.out = new PrintWriter(new OutputStreamWriter(outStream));
    this.in = new BufferedReader(new InputStreamReader(inStream));
  }

  public void receive() {
    try {
      writeAndFlush("Ready to receive file");
      parseMetadata();
      readFileFromStream();
      writeAndFlush("File received");
      fileOutputStream.close();
    } catch (IOException | NumberFormatException | NoSuchElementException e) {
      e.printStackTrace();
    }
  }

  private void writeAndFlush(String message) {
    out.write(message + "\n");
    out.flush();
  }

  private void parseMetadata() throws IOException {
    StringTokenizer tokenizer = new StringTokenizer(in.readLine());
    fileName = tokenizer.nextToken();
    size = Integer.valueOf(tokenizer.nextToken());
    initializeStream();
  }

  private void initializeStream() throws FileNotFoundException {
    dataInputStream = new DataInputStream(inStream);
    fileOutputStream = new FileOutputStream("./resources/" + fileName);
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
