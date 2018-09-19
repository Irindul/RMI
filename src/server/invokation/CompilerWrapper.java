package server.invokation;

import java.io.File;
import java.util.Optional;
import java.util.logging.Logger;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class CompilerWrapper {

  private static final Logger LOGGER = Logger.getLogger("CompileWrapper");

  public Optional<String> compile(String fileName) {
    String pathname = "./resources/client/" + fileName;
    File file = new File(pathname);
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    int error = compiler.run(null, null, null, file.getPath());

    if (error == 0) {
      LOGGER.info("File was successfully compiled");
      return Optional.of(getCompiledName(pathname));
    } else {
      return Optional.empty();
    }
  }

  private String getCompiledName(String pathname) {
    return pathname.replace("java", "class");
  }
}
