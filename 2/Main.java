import java.util.Random;

public class Main {
    public static void main(String[] args) {
        System.out.println("-- Склад обуви - Producer-Consumer --");

        ShoeWarehouse warehouse = new ShoeWarehouse();
        Random random = new Random();

        //Producer поток
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 15; i++) {
                //случайный тип обуви и количество
                String shoeType = ShoeWarehouse.PRODUCT_TYPES.get(
                        random.nextInt(ShoeWarehouse.PRODUCT_TYPES.size())
                );
                int quantity = random.nextInt(10) + 1; // 1-10 пар

                Order order = new Order(i, shoeType, quantity);
                warehouse.receiveOrder(order);

                try {
                    Thread.sleep(random.nextInt(500)); // Имитация работы
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("!Producer завершил работу!");
        }, "Producer");

        //Consumer потоки (3 потока по 5 заказов каждый)
        for (int i = 1; i <= 3; i++) {
            final int consumerId = i;
            Thread consumer = new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    Order order = warehouse.fulfillOrder();
                    if (order != null) {
                        //имитация обработки заказа
                        try {
                            Thread.sleep(random.nextInt(800) + 200);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }
                System.out.println("!Сonsumer-" + consumerId + " завершил работу!");
            }, "Consumer-" + i);

            consumer.start();
        }

        //запуск producer после consumers чтобы они ждали заказы
        producer.start();

        //ожидание завершения потоков
        try {
            producer.join();
            //время consumers завершить обработку
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("-- Все операции завершены --");
    }
}