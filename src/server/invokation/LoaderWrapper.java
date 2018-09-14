package server.invokation;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class LoaderWrapper {

  private URL url;

  public LoaderWrapper() {
    String rootPath = System.getProperty("user.dir")
        + File.separator
        + "resources"
        + File.separator;

    File root = new File(rootPath);
    try {
      url = root.toURI().toURL();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  public Optional<Class<?>> load(String compiledFilePath) {
    String compiledFileName = getTrimmedCompiledName(compiledFilePath);
    try {
      URLClassLoader classLoader = URLClassLoader
          .newInstance(new URL[]{url}, getClass().getClassLoader());

      //Here we assume package is client
      String packageStructure = "client.";
      Class<?> cls = Class.forName(packageStructure + compiledFileName, true, classLoader);
      return Optional.of(cls);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  private String getTrimmedCompiledName(String compiledFilePath) {
    Path path = Paths.get(compiledFilePath);
    String compiledFileName = path.getFileName().toString();
    return compiledFileName.split("\\.")[0];
  }
}
