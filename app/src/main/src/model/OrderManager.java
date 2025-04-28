package model;

import java.util.ArrayList;

/**
 * Class that manages customer orders.
 *
 * Maintains the current active order and a history of all placed orders.
 * Provides functionality to create, retrieve, update, and manage orders throughout the system.
 *
 * Ensures only one instance of OrderManager exists.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class OrderManager {
    private static OrderManager instance;
    private Order currentOrder;
    private static ArrayList<Order> orderHistory = new ArrayList<>();

    /**
     * Private constructor to prevent external instantiation.
     * Initializes a new current order.
     */
    private OrderManager() {
        currentOrder = new Order();
    }

    /**
     * Retrieves the single instance of the OrderManager.
     * Creates a new instance if one does not already exist.
     *
     * @return the singleton instance of OrderManager
     */
    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    /**
     * Retrieves the current active order.
     *
     * @return the current order
     */
    public Order getCurrentOrder() {
        return currentOrder;
    }

    /**
     * Sets a new current order.
     *
     * @param order the new order to set as the current order
     */
    public void setCurrentOrder(Order order) {
        this.currentOrder = order;
    }

    /**
     * Finalizes the current order and starts a new one.
     */
    public void placeOrder() {
        currentOrder = new Order();
    }

    /**
     * Adds an order to the order history.
     * Initializes the order history list if it is null.
     *
     * @param order the order to add to history
     */
    public void addToOrderHistory(Order order) {
        if (orderHistory == null) {
            orderHistory = new ArrayList<>();
        }
        orderHistory.add(order);
    }

    /**
     * Retrieves the complete history of placed orders.
     *
     * @return the list of past orders
     */
    public ArrayList<Order> getOrderHistory() {
        return orderHistory;
    }

    /**
     * Removes an order from the order history.
     *
     * @param order the order to remove
     */
    public void removeFromOrderHistory(Order order) {
        if (orderHistory != null) {
            orderHistory.remove(order);
        }
    }
}