package helpers;

public class Env {

  public static String getEnv(String name) {
    return System.getenv(name);
  }

  public static String getEnvOrDefault(String name, String defaultValue) {
    String value = getEnv(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
