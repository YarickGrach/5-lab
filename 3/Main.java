import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        System.out.println("--Склад обуви - ExecutorService--");

        ShoeWarehouse warehouse = new ShoeWarehouse();
        Random random = new Random();

        //ExecutorService для производителей
        ExecutorService producerExecutor = Executors.newFixedThreadPool(2);

        //запуск, несколько производителей
        for (int p = 1; p <= 2; p++) {
            final int producerId = p;
            producerExecutor.submit(() -> {
                for (int i = 1; i <= 10; i++) {
                    String shoeType = ShoeWarehouse.PRODUCT_TYPES.get(
                            random.nextInt(ShoeWarehouse.PRODUCT_TYPES.size())
                    );
                    int quantity = random.nextInt(10) + 1;
                    int orderId = producerId * 100 + i; //уникальный ID

                    Order order = new Order(orderId, shoeType, quantity);
                    warehouse.receiveOrder(order);

                    try {
                        Thread.sleep(random.nextInt(300)); //имитация работы
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                System.out.println("Producer-" + producerId + " завершил работу");
            });
        }

        //время на добавление заказов
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        producerExecutor.shutdown();
        try {
            //ожидание завершения всех производителей
            if (!producerExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                producerExecutor.shutdownNow();
            }

            //время на обработку оставшихся заказов
            Thread.sleep(3000);
            warehouse.shutdown();

        } catch (InterruptedException e) {
            producerExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("--Все операции завершены--");
    }
}
