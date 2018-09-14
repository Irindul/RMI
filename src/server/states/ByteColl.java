package server.states;

import helpers.SocketStreams;
import java.util.Optional;
import server.invokation.LoaderWrapper;

public class ByteColl extends ObjectColl {

  private LoaderWrapper loader;
  private String compiledFilePath;

  public ByteColl(SocketStreams streams) {
    super(streams);
    loader= new LoaderWrapper();
  }

  public void setCompiledFilePath(String compiledFilePath) {
    this.compiledFilePath = compiledFilePath;
  }

  @Override
  public void execute() {
    Optional<Class<?>> optionalClass = loader.load(compiledFilePath);
    optionalClass.ifPresent(cls -> {
      super.setClazz(cls);
      super.execute();
    });
  }
}
