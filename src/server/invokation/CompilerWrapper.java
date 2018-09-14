package server.invokation;

import java.io.File;
import java.util.Optional;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class CompilerWrapper {

  public Optional<String> compile(String fileName) {
    String pathname = "./resources/client/" + fileName;
    File file = new File(pathname);
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    int error = compiler.run(null, null, null, file.getPath());

    if (error == 0) {
      return Optional.of(getCompiledName(pathname));
    } else {
      return Optional.empty();
    }
  }

  private String getCompiledName(String pathname) {
    return pathname.replace("java", "class");
  }
}
