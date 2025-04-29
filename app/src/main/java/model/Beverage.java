package model;

/**
 * Class representing a beverage item on the menu.
 *
 * Each beverage has a specific size, flavor, and quantity.
 * The price is determined based on the beverage size and quantity.
 *
 * Extends the MenuItem class.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class Beverage extends MenuItem {
    private Size size;
    private Flavor flavor;

    /**
     * Constructs a new Beverage with the specified size, flavor, and quantity.
     *
     * @param size the size of the beverage
     * @param flavor the flavor of the beverage
     * @param quantity the quantity ordered
     */
    public Beverage(Size size, Flavor flavor, int quantity) {
        this.size = size;
        this.flavor = flavor;
        this.quantity = quantity;
    }

    /**
     * Retrieves the size of the beverage.
     *
     * @return the beverage size
     */
    public Size getSize() {
        return size;
    }

    /**
     * Retrieves the flavor of the beverage.
     *
     * @return the beverage flavor
     */
    public Flavor getFlavor() {
        return flavor;
    }

    /**
     * Calculates the total price of the beverage order based on size and quantity.
     *
     * @return the total price for the beverage(s)
     */
    @Override
    public double price() {
        // Base price for the beverage based on size
        double basePrice = 0;

        switch (size) {
            case SMALL:
                basePrice = 1.99;
                break;
            case MEDIUM:
                basePrice = 2.49;
                break;
            case LARGE:
                basePrice = 2.99;
        }

        return basePrice * quantity;
    }

    /**
     * Returns a string representation of the beverage item, including quantity, size, flavor, and total price.
     *
     * @return formatted string with beverage details
     */
    @Override
    public String toString() {
        return quantity + " x " + size + " " + flavor + " Beverage ($" + String.format("%.2f", price()) + ")";
    }
}