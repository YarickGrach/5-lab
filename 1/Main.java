import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Запуск двух потоков...");

        Thread evenThread = new EvenThread();
        evenThread.setName("EvenThread");
        Thread oddThread = new Thread(new OddRunnable(), "OddThread");

        evenThread.start();
        oddThread.start();

        try {
            evenThread.join();
            oddThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("-оба потока завершили работу-");
    }
}