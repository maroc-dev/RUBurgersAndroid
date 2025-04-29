package model;

/**
 * Class representing an individual side order item.
 *
 * A side order consists of a selected side type (e.g., fries, onion rings) and size (small, medium, large).
 * The price is determined based on the type, size, and quantity of the order.
 *
 * Extends the MenuItem class.
 *
 * Used to handle sides ordered separately from combos.
 *
 * @see Side
 * @see Size
 * @see MenuItem
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class SideOrder extends MenuItem {
    private Side type;
    private Size size;

    /**
     * Constructs a new SideOrder with the specified side type, size, and quantity.
     *
     * @param type the type of side selected
     * @param size the size of the side
     * @param quantity the number of side orders (defaults to 1)
     */
    public SideOrder(Side type, Size size, int quantity) {
        this.type = type;
        this.size = size;
        this.quantity = 1; // Default quantity for side orders
    }

    /**
     * Calculates the total price for the side order.
     * The price is based on the side type, selected size, and quantity.
     *
     * @return the total price for the side order
     */
    @Override
    public double price() {
        return type.getPriceBySize(size) * quantity;
    }

    /**
     * Returns a string representation of the side order,
     * including the quantity, size, type, and total price.
     *
     * @return formatted string with side order details
     */
    @Override
    public String toString() {
        return quantity + "x " + size + " " + type.name().replace("_", " ") + " ($" +
                String.format("%.2f", price()) + ")";
    }
}
