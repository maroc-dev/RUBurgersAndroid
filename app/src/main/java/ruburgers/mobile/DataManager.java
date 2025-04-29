package ruburgers.mobile;

import model.MenuItem;
import model.Order;
import model.OrderManager;

import java.util.ArrayList;


/**
 * The singleton design class that manages the data for the application.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class DataManager {
    // Static instance variable
    private static DataManager instance;

    private MenuItem temporaryItem;

    private ArrayList<Order> orderHistory = new ArrayList<>();



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


    public Order getCurrentOrder() {
        return currentOrder;
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

    private void updateOrderTotal() {
        orderTotal = currentOrder.orderTotal();
    }


}