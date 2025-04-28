package model;

import java.util.ArrayList;

/**
 * Class for the Burger.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class Burger extends Sandwich {
    private boolean doublePatty;

    public Burger(Bread bread, ArrayList<AddOns> addOns, int quantity, boolean doublePatty) {
        super(bread, Protein.BEEF_PATTY, addOns, quantity);
        this.doublePatty = doublePatty;
    }

    @Override
    public double price() {
        double basePrice = protein.price(doublePatty); // Get price from Sandwich
        for (AddOns addOn : addOns) {
            basePrice += addOn.getPrice(); // Add extra charge for double patty
        }
        return basePrice * quantity;
    }

    @Override
    public String toString() {
        return quantity + " x " + (doublePatty ? "Double Patty " : "Single Patty ") +
                bread + " Burger with " + addOns + " ($" + String.format("%.2f", price()) + ")";
    }
}

