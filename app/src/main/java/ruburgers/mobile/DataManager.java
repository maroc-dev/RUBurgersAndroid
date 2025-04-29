package ruburgers.mobile;

import model.Burger;
import model.MenuItem;
import model.Order;
import model.OrderManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * DataManager - A Singleton class for sharing data between Activities
 * Follows the Singleton design pattern to ensure only one instance exists
 */
public class DataManager {
    // Static instance variable
    private static DataManager instance;

    private MenuItem temporaryItem;


    // Order-related fields
    private Order currentOrder;
    private double orderTotal;

    // Private constructor prevents direct instantiation
    private DataManager() {
        currentOrder = new Order();
        orderTotal = 0.0;
    }

    /**
     * Get the singleton instance of DataManager
     * Uses double-checked locking for thread safety
     */
    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }


    // Order-specific methods
    public void addOrderItem(MenuItem item) {
        currentOrder.addItem(item);
        updateOrderTotal();
    }

    public void removeOrderItem(MenuItem item) {
        currentOrder.removeItem(item);
        updateOrderTotal();
    }

    public ArrayList<MenuItem> getOrderItems() {
        return currentOrder.getItems();
    }

    public void clearOrder() {
        currentOrder.clearItems();
        updateOrderTotal();
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order order) {
        this.currentOrder = order;
        updateOrderTotal();
    }

    public void placeOrder() {
        OrderManager orderManager = OrderManager.getInstance();
        orderManager.addToOrderHistory(currentOrder);
        currentOrder = new Order();
        updateOrderTotal();
    }

    public ArrayList<Order> getOrderHistory() {
        return OrderManager.getInstance().getOrderHistory();
    }

    // Updates the order total based on the current order
    private void updateOrderTotal() {
        orderTotal = currentOrder.orderTotal();
    }

    // Getter methods for order totals
    public double getSubtotal() {
        return currentOrder.subtotal();
    }

    public double getTaxAmount() {
        return currentOrder.taxAmount();
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public String getOrderNumber() {
        return currentOrder.getOrderNumber();
    }


    public void setTemporaryItem(MenuItem item) {
        this.temporaryItem = item;
    }

    public MenuItem getTemporaryItem() {
        return temporaryItem;
    }
}