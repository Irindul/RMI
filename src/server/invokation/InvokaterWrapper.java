package server.invokation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

public class InvokaterWrapper {

  private InputStream inStream;
  private OutputStream outStream;
  private BufferedReader in;
  private PrintWriter out;
  private Class<?> receivedClass;

  public InvokaterWrapper(InputStream in, OutputStream out) {
    this.inStream = in;
    this.outStream = out;
    this.out = new PrintWriter(new OutputStreamWriter(outStream));
    this.in = new BufferedReader(new InputStreamReader(inStream));
  }

  public InvokaterWrapper load(Class<?> receivedClass) {
    this.receivedClass = receivedClass;
    return this;
  }

  public Optional<Integer> invoke() {

    try {
      String rawArguments = in.readLine();
      StringTokenizer tokenizer = new StringTokenizer(rawArguments);
      String methodName = tokenizer.nextToken();
      List<Integer> arguments = new ArrayList<>();

      while (tokenizer.hasMoreTokens()) {
        arguments.add(Integer.valueOf(tokenizer.nextToken()));
      }

      Class<?>[] classes = new Class[arguments.size()];
      for (int i = 0; i < classes.length; i++) {
        classes[i] = Integer.class;
      }

      Method method = receivedClass.getMethod(methodName, classes);
      Integer result = (Integer) method.invoke(receivedClass.newInstance(), arguments.toArray());
      return Optional.of(result);
    } catch (IOException
        | NoSuchMethodException
        | IllegalAccessException
        | InstantiationException
        | InvocationTargetException e) {
      System.out.println(e.getMessage() + e.getLocalizedMessage());
      return Optional.empty();
    }
  }

}
