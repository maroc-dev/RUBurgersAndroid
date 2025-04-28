package ruburgers.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataManager - A Singleton class for sharing data between Activities
 * Follows the Singleton design pattern to ensure only one instance exists
 */
public class DataManager {
    // Static instance variable
    private static DataManager instance;

    // Private data fields to store shared information
    private Map<String, Object> dataMap;
    private List<String> orderItems;
    private double orderTotal;
    private String customerName;
    private String customerPhone;

    // Private constructor prevents direct instantiation
    private DataManager() {
        // Initialize data structures
        dataMap = new HashMap<>();
        orderItems = new ArrayList<>();
        orderTotal = 0.0;
        customerName = "";
        customerPhone = "";
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

    // Generic methods for storing and retrieving data
    public void setData(String key, Object value) {
        dataMap.put(key, value);
    }

    public Object getData(String key) {
        return dataMap.get(key);
    }

    // Order-specific methods
    public void addOrderItem(String item) {
        orderItems.add(item);
    }

    public void removeOrderItem(String item) {
        orderItems.remove(item);
    }

    public List<String> getOrderItems() {
        return orderItems;
    }

    public void clearOrder() {
        orderItems.clear();
        orderTotal = 0.0;
    }

    // Getter and setter methods for order total
    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double total) {
        this.orderTotal = total;
    }

    // Customer information methods
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String name) {
        this.customerName = name;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String phone) {
        this.customerPhone = phone;
    }
}