package model;

/**
 * Enum represents available add-on options for sandwiches and burgers.
 * Each add-on has a specific additional price.
 *
 * Retrieves the price for a given add-on option.
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

    /** The additional price of the add-on */
    private final double price;

    /**
     * Constructs an AddOns enum with the specified price.
     *
     * @param price the additional cost associated with the add-on
     */
    AddOns(double price) {
        this.price = price;
    }

    /**
     * Retrieves the additional price associated with this add-on.
     *
     * @return the price of the add-on
     */
    public double getPrice() {
        return price;
    }
}
