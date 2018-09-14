package server.states;

import client.IntegerCalculator;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

  public SourceColl(InputStream inFromClient, OutputStream outToClient) {
    fileReceiver = new FileReceiver(inFromClient, outToClient);
    compiler = new CompilerWrapper();
    loader = new LoaderWrapper();
    invokater = new InvokaterWrapper(inFromClient, outToClient);
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
          invokater.load(cls).invoke();
        });
      });
    });
  }
}
