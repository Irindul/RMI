package server.states;

import client.IntegerCalculator;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
  private BufferedReader in;
  private PrintWriter out;

  public SourceColl(InputStream inFromClient, OutputStream outToClient) {
    fileReceiver = new FileReceiver(inFromClient, outToClient);
    compiler = new CompilerWrapper();
    loader = new LoaderWrapper();
    invokater = new InvokaterWrapper(inFromClient, outToClient);
    this.out = new PrintWriter(new OutputStreamWriter(outToClient));
    this.in = new BufferedReader(new InputStreamReader(inFromClient));
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
          optionalResult.ifPresent(result -> {
            out.write(result + "\n");
            out.flush();
          });
        });
      });
    });
  }
}
