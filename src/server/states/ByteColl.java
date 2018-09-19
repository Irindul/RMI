package server.states;

import helpers.SocketStreams;
import java.util.Optional;
import server.invokation.LoaderWrapper;

public class ByteColl extends ObjectColl {

  private LoaderWrapper loader;

  public ByteColl(SocketStreams streams) {
    super(streams, false);
    loader = new LoaderWrapper();
  }

  @Override
  public void execute() {
    Optional<String> optionalCompiledFilePath = receiver.receive();
    optionalCompiledFilePath.ifPresent(this::subexecution);
  }

  protected void subexecution(String compiledFilePath) {
    Optional<Class<?>> optionalClass = loader.load(compiledFilePath);
    optionalClass.ifPresent(cls -> {
      super.setClazz(cls);
      super.execute();
    });
  }
}
