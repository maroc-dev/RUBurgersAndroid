package model;

/**
 * Enum for the Add ons.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public enum AddOns {
    LETTUCE(0.30),
    TOMATOES(0.30),
    ONIONS(0.30),
    AVOCADOS(0.50),
    CHEESE(1.00);

    private final double price;

    AddOns(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
