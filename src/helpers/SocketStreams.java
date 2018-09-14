package helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class SocketStreams {

  private OutputStream outputStream;
  private InputStream inputStream;
  private BufferedReader in;
  private PrintWriter out;

  public SocketStreams(OutputStream outputStream, InputStream inputStream) {
    this.outputStream = outputStream;
    this.inputStream = inputStream;
    this.out = new PrintWriter(new OutputStreamWriter(outputStream));
    this.in = new BufferedReader(new InputStreamReader(inputStream));
  }

  public OutputStream getOutputStream() {
    return outputStream;
  }

  public InputStream getInputStream() {
    return inputStream;
  }

  public void writeAndFlush(String message) {
    out.write(message + "\n");
    out.flush();
  }

  public String readLine() throws IOException {
    return  in.readLine();
  }

  public void close() throws IOException{
    if (inputStream != null) {
      inputStream.close();
    }
    if (outputStream != null) {
      outputStream.close();
    }
    if (in != null) {
      in.close();
    }
    if (out != null) {
      out.close();
    }
  }
}
