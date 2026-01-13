import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ShoeWarehouse {
    //поле со списком типов обуви
    public static final List<String> PRODUCT_TYPES = List.of(
            "Nike Air Max", "Adidas Ultraboost", "Puma RS-X",
            "Reebok Classic", "Vans Old Skool", "Converse Chuck Taylor"
    );

    //список заказов (очередь)
    private final Queue<Order> orders = new LinkedList<>();
    private final int MAX_CAPACITY = 10; // Максимальный размер очереди

    //для производителя (добавление заказа)
    public synchronized void receiveOrder(Order order) {
        //ожидание, если очередь заполнена
        while (orders.size() >= MAX_CAPACITY) {
            try {
                System.out.println("Склад полон, Producer ожидает...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        orders.offer(order);
        System.out.println("Добавлен: " + order + " В очереди: " + orders.size());

        //уведомление потребителей
        notifyAll();
    }

    //для потребителя (обработка заказа)
    public synchronized Order fulfillOrder() {
        //ожидание, если очередь пуста
        while (orders.isEmpty()) {
            try {
                System.out.println("Склад пуст, Consumer ожидает...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        //извлечение заказа
        Order order = orders.poll();
        System.out.println("Обработан: " + order + " В очереди: " + orders.size());

        //уведомление для ожидающих производителей
        notifyAll();

        return order;
    }

    //для получения текущего размера очереди
    public synchronized int getQueueSize() {
        return orders.size();
    }
}