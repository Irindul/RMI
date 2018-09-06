package common;

public interface CustomRunnable extends Runnable {

   default boolean isNotInterrupted() {
    return !isInterrupted();
  }

   default boolean isInterrupted() {
    return Thread.currentThread().isInterrupted();
  }

  default void interupt() {
     Thread.currentThread().interrupt();
  }

  void close();
  void execute();

  default void run() {
     while(isNotInterrupted()) {
       execute();
     }
     close();
  }
}
