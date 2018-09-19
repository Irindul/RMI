package server.invokation;

import helpers.SocketStreams;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class InvokaterWrapper {

  private static final Logger LOGGER = Logger.getLogger("InvokaterWrapper");
  private SocketStreams streams;
  private Class<?> receivedClass;

  public InvokaterWrapper(SocketStreams streams) {
    this.streams = streams;
  }

  public InvokaterWrapper load(Class<?> receivedClass) {
    this.receivedClass = receivedClass;
    return this;
  }

  public Optional<Integer> invoke() {
    boolean loop = false;
    Optional<Integer> value = Optional.empty();
    do {
      try {
        loop = false;
        String rawArguments = streams.readLine();
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
        value = Optional.of(result);
      } catch (NoSuchMethodException e) {
        streams.writeAndFlush("no such method");
        loop = true;
      } catch (IOException
          | IllegalAccessException
          | InstantiationException
          | InvocationTargetException e) {
        LOGGER.severe(e.getMessage() + e.getLocalizedMessage());
      }
    } while (loop);

    return value;
  }

}
