package model;

import java.util.ArrayList;

/**
 * Class representing a burger menu item.
 *
 * A burger consists of a selected bread type, protein (beef patty), optional add-ons,
 * and the ability to specify a single or double patty.
 *
 * Extends the Sandwich class, inheriting its basic structure and enhancing it with the double patty option.
 *
 * Calculates price based on patty type, add-ons, and quantity ordered.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class Burger extends Sandwich {
    /** Indicates whether the burger includes a double patty */
    private boolean doublePatty;

    /**
     * Constructs a new Burger with the specified bread, add-ons, quantity, and patty type.
     *
     * @param bread the type of bread selected for the burger
     * @param addOns the list of add-ons included with the burger
     * @param quantity the number of burgers ordered
     * @param doublePatty true if the burger has a double patty, false for a single patty
     */
    public Burger(Bread bread, ArrayList<AddOns> addOns, int quantity, boolean doublePatty) {
        super(bread, Protein.BEEF_PATTY, addOns, quantity);
        this.doublePatty = doublePatty;
    }

    /**
     * Calculates the total price of the burger order.
     * The price is based on the patty type (single or double), the selected add-ons, and the quantity.
     *
     * @return the total price for the burger(s)
     */
    @Override
    public double price() {
        double basePrice = protein.price(doublePatty); // Get price from Sandwich (Protein)
        for (AddOns addOn : addOns) {
            basePrice += addOn.getPrice(); // Add price for each add-on
        }
        return basePrice * quantity;
    }

    /**
     * Returns a string representation of the burger item,
     * including quantity, patty type (single or double), bread type, add-ons, and total price.
     *
     * @return formatted string with burger details
     */
    @Override
    public String toString() {
        return quantity + " x " + (doublePatty ? "Double Patty " : "Single Patty ") +
                bread + " Burger with " + addOns + " ($" + String.format("%.2f", price()) + ")";
    }
}

