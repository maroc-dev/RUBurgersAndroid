package model;

/**
 * Enum for the Side orders.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class SideOrder extends MenuItem {
    private Side type;
    private Size size;

    public SideOrder(Side type, Size size, int quantity) {
        this.type = type;
        this.size = size;
        this.quantity = 1; // Default quantity for side orders
    }

    @Override
    public double price() {
        return type.getPriceBySize(size) * quantity;
    }

    @Override
    public String toString() {
        return quantity + "x " + size + " " + type.name().replace("_", " ") + " ($" +
                String.format("%.2f", price()) + ")";
    }

}
