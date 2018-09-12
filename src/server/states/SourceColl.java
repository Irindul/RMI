package server.states;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import server.invokation.CompilerWrapper;
import server.network.FileReceiver;

public class SourceColl implements RMIState {

  private FileReceiver fileReceiver;
  private CompilerWrapper compiler;

  public SourceColl(InputStream inFromClient, OutputStream outToClient) {
    fileReceiver = new FileReceiver(inFromClient, outToClient);
    compiler = new CompilerWrapper();
  }

  @Override
  public void execute() {
    Optional<String> optionalFilename = fileReceiver.receive();

    optionalFilename.ifPresent(fileName -> {
      Optional<String> optionalCompiledName = compiler.compile(fileName);
      optionalCompiledName.ifPresent(compiledFilePath -> {
        // Load and instantiate compiled class.
        String rootPath = System.getProperty("user.dir")
            + File.separator
            + "resources"
            + File.separator;
        File root = new File(rootPath);
        Path path = Paths.get(compiledFilePath);
        String compiledFileName = path.getFileName().toString();
        compiledFileName = compiledFileName.split("\\.")[0];
        try {
          URL url = root.toURI().toURL();
          URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{url}, getClass().getClassLoader());
          Class<?> cls = Class.forName("client." + compiledFileName, true, classLoader);
          Object instance = cls.newInstance();
          System.out.println(instance);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | MalformedURLException e) {
          e.printStackTrace();
        }
      });
    });
  }
}
