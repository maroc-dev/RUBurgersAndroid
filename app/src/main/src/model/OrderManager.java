package model;

import java.util.ArrayList;

/**
 * Manager Class for the Orders.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class OrderManager {
    private static OrderManager instance;
    private Order currentOrder;
    private static ArrayList<Order> orderHistory = new ArrayList<>();

    private OrderManager() {
        currentOrder = new Order();

    }

    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder (Order order) {
        this.currentOrder = order;
    }

    public void placeOrder() {
        currentOrder = new Order();
    }

    public void addToOrderHistory(Order order) {
        if (orderHistory == null) {
            orderHistory = new ArrayList<>();
        }
        orderHistory.add(order);
    }
    public ArrayList<Order> getOrderHistory() {
        return orderHistory;
    }

    public void removeFromOrderHistory(Order order) {
        if (orderHistory != null) {
            orderHistory.remove(order);
        }
    }


}