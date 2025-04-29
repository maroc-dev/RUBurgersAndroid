package model;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class representing a customer order containing multiple menu items.
 *
 * Each order has a unique order number and maintains a list of MenuItem objects.
 * Provides functionality to add, remove, and clear items, as well as calculate subtotal, tax, and total cost.
 *
 * Used to manage the customer's full purchase details at checkout.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class Order {
    private int number;
    private ArrayList<MenuItem> items;

    /**
     * Constructs a new Order with a randomly generated order number.
     * Initializes an empty list of menu items.
     */
    public Order() {
        Random random = new Random();
        this.number = 100000000 + random.nextInt(900000000);
        this.items = new ArrayList<>();
    }

    /**
     * Adds a menu item to the order.
     *
     * @param item the menu item to add
     */
    public void addItem(MenuItem item) {
        items.add(item);
    }

    /**
     * Removes a menu item from the order.
     *
     * @param item the menu item to remove
     */
    public void removeItem(MenuItem item) {
        items.remove(item);
    }

    /**
     * Clears all items from the order.
     */
    public void clearItems() {
        items.clear();
    }

    /**
     * Retrieves the list of menu items in the order.
     *
     * @return the list of menu items
     */
    public ArrayList<MenuItem> getItems() {
        return items;
    }

    /**
     * Calculates the subtotal cost of the order by summing the prices of all items.
     *
     * @return the subtotal amount
     */
    public double subtotal() {
        double total = 0;
        for (MenuItem item : items) {
            total += item.price();
        }
        return total;
    }

    /**
     * Calculates the tax amount for the order.
     * The tax rate is fixed at 6.625%.
     *
     * @return the tax amount
     */
    public double taxAmount() {
        return subtotal() * 0.06625;
    }

    /**
     * Calculates the total cost of the order, including subtotal and tax.
     *
     * @return the total order amount
     */
    public double orderTotal() {
        return subtotal() + taxAmount();
    }

    /**
     * Returns a string representation of the order,
     * listing each menu item along with the subtotal, tax, and total cost.
     *
     * @return formatted string containing the order details
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Order #" + number + ":\n");
        for (MenuItem item : items) {
            sb.append("- ").append(item.toString()).append("\n");
        }
        sb.append(String.format("Subtotal: $%.2f\n", subtotal()));
        sb.append(String.format("Sales Tax (6.625%%): $%.2f\n", taxAmount()));
        sb.append(String.format("Total: $%.2f", orderTotal()));
        return sb.toString();
    }

    /**
     * Retrieves the order number as a string.
     *
     * @return the order number
     */
    public String getOrderNumber() {
        return String.valueOf(this.number);
    }
}
