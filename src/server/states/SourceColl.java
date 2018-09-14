package server.states;

import helpers.SocketStreams;
import java.util.Optional;
import server.invokation.CompilerWrapper;
import server.invokation.InvokaterWrapper;
import server.invokation.LoaderWrapper;
import server.network.FileReceiver;

public class SourceColl implements RMIState {

  private FileReceiver fileReceiver;
  private CompilerWrapper compiler;
  private LoaderWrapper loader;
  private InvokaterWrapper invokater;
  private SocketStreams streams;

  public SourceColl(SocketStreams streams) {
    fileReceiver = new FileReceiver(streams);
    compiler = new CompilerWrapper();
    loader = new LoaderWrapper();
    invokater = new InvokaterWrapper(streams);
    this.streams = streams;
  }

  @Override
  public void execute() {
    Optional<String> optionalFilename = fileReceiver.receive();

    optionalFilename.ifPresent(fileName -> {
      Optional<String> optionalCompiledName = compiler.compile(fileName);
      optionalCompiledName.ifPresent(compiledFilePath -> {
        Optional<Class<?>> optionalClass = loader.load(compiledFilePath);
        optionalClass.ifPresent(cls -> {
          //Execute method;
          Optional<Integer> optionalResult = invokater.load(cls).invoke();
          optionalResult.ifPresent(result -> streams.writeAndFlush(result.toString()));
        });
      });
    });
  }
}
