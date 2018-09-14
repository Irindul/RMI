package server.states;

import helpers.SocketStreams;
import java.util.Optional;
import server.invokation.CompilerWrapper;

public class SourceColl extends ByteColl {

  private CompilerWrapper compiler;

  public SourceColl(SocketStreams streams) {
    super(streams);
    compiler = new CompilerWrapper();
  }

  @Override
  protected void subexecution(String fileName) {
    Optional<String> optionalCompiledFilePath = compiler.compile(fileName);
    optionalCompiledFilePath.ifPresent(super::subexecution);
  }
}
