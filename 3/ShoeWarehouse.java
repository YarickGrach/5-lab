import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShoeWarehouse {
    // Список типов обуви
    public static final List<String> PRODUCT_TYPES = List.of(
            "Nike Air Max", "Adidas Ultraboost", "Puma RS-X",
            "Reebok Classic", "Vans Old Skool", "Converse Chuck Taylor"
    );

    private final Queue<Order> orders = new LinkedList<>();
    private final int MAX_CAPACITY = 10;

    //для обработки заказов
    private final ExecutorService fulfillmentExecutor;

    public ShoeWarehouse() {
        //FixedThreadPool с 3 потоками для обработки заказов
        this.fulfillmentExecutor = Executors.newFixedThreadPool(3);
    }

    //метод для производителя
    public synchronized void receiveOrder(Order order) {
        while (orders.size() >= MAX_CAPACITY) {
            try {
                System.out.println("Склад полон! Producer ожидает...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        orders.offer(order);
        System.out.println("Добавлен: " + order + " | В очереди: " + orders.size());

        //автоматический запуск обработки нового заказа
        processNextOrder();
        notifyAll();
    }

    //автоматическая обработка следующего заказа через ExecutorService
    private void processNextOrder() {
        fulfillmentExecutor.submit(() -> {
            Order order = fulfillOrder();
            if (order != null) {
                //имитация времени обработки
                try {
                    Thread.sleep(500);
                    System.out.println("Завершена обработка: " + order);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    //метод для извлечения заказа (используется внутри processNextOrder)
    private synchronized Order fulfillOrder() {
        while (orders.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        Order order = orders.poll();
        System.out.println("В обработке: " + order + " | Осталось: " + orders.size());
        notifyAll();
        return order;
    }

    //завершение работы ExecutorService
    public void shutdown() {
        fulfillmentExecutor.shutdown();
    }

    public synchronized int getQueueSize() {
        return orders.size();
    }
}