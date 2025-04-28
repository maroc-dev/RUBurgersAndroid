package model;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class for the Orders.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class Order {
    private int number;
    private ArrayList<MenuItem> items;

    public Order() {
        Random random = new Random();
        this.number = 100000000 + random.nextInt(900000000);
        this.items = new ArrayList<>();
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public void removeItem(MenuItem item) {
        items.remove(item);
    }

    public void clearItems() {
        items.clear();
    }

    public ArrayList<MenuItem> getItems() {
        return items;
    }

    public double subtotal() {
        double total = 0;
        for (MenuItem item : items) {
            total += item.price();
        }
        return total;
    }

    public double taxAmount() {
        return subtotal() * 0.06625;
    }

    public double orderTotal() {
        return subtotal() + taxAmount();
    }

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

    public String getOrderNumber() {
        return String.valueOf(this.number);
    }
}