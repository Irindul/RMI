package server.states;

import helpers.SocketStreams;
import java.util.Optional;
import server.invokation.CompilerWrapper;
import server.network.FileReceiver;

public class SourceColl extends ByteColl {

  private FileReceiver fileReceiver;
  private CompilerWrapper compiler;

  public SourceColl(SocketStreams streams) {
    super(streams);
    fileReceiver = new FileReceiver(streams);
    compiler = new CompilerWrapper();
  }

  @Override
  public void execute() {
    Optional<String> optionalFilename = fileReceiver.receive();

    optionalFilename.ifPresent(fileName -> {
      Optional<String> optionalCompiledName = compiler.compile(fileName);
      optionalCompiledName.ifPresent(compiledFilePath -> {
        super.setCompiledFilePath(compiledFilePath);
        super.execute();
      });
    });
  }
}
