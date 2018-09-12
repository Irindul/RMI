package server.states;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import server.invokation.CompilerWrapper;
import server.invokation.LoaderWrapper;
import server.network.FileReceiver;

public class SourceColl implements RMIState {

  private FileReceiver fileReceiver;
  private CompilerWrapper compiler;
  private LoaderWrapper loader;

  public SourceColl(InputStream inFromClient, OutputStream outToClient) {
    fileReceiver = new FileReceiver(inFromClient, outToClient);
    compiler = new CompilerWrapper();
    loader = new LoaderWrapper();
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
        });
      });
    });
  }
}
