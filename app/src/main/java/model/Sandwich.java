package model;

import java.util.ArrayList;

/**
 * Class representing a customizable sandwich menu item.
 *
 * A sandwich consists of a selected bread type, a protein option,
 * and optional add-ons. The price is determined based on the selected
 * ingredients and the quantity ordered.
 *
 * Extends the MenuItem class.
 *
 * Used as the base structure for more specialized items such as {@link Burger}.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class Sandwich extends MenuItem {
    protected Bread bread;
    protected Protein protein;
    protected ArrayList<AddOns> addOns;

    /**
     * Constructs a new Sandwich with the specified bread, protein, add-ons, and quantity.
     *
     * @param bread the bread type for the sandwich
     * @param protein the protein option for the sandwich
     * @param addOns the list of add-ons selected for the sandwich
     * @param quantity the number of sandwiches ordered
     */
    public Sandwich(Bread bread, Protein protein, ArrayList<AddOns> addOns, int quantity) {
        this.bread = bread;
        this.protein = protein;
        this.addOns = addOns;
        this.quantity = quantity;
    }

    /**
     * Calculates the total price for the sandwich order.
     * The price includes the base protein price and the prices of any selected add-ons.
     *
     * @return the total price for the sandwiches
     */
    @Override
    public double price() {
        double basePrice = protein.price();
        for (AddOns addOn : addOns) {
            basePrice += addOn.getPrice();
        }
        return basePrice * quantity;
    }

    /**
     * Returns a simplified string description of the sandwich,
     * primarily used when a sandwich is included as part of a combo meal.
     *
     * @return formatted string describing the sandwich for a combo
     */
    public String toComboString() {
        return quantity + " x " + bread + " sandwich with " + protein +
                " and " + addOns.toString();
    }

    /**
     * Returns a detailed string representation of the sandwich,
     * including bread type, protein, add-ons, quantity, and total price.
     *
     * @return formatted string with sandwich details
     */
    @Override
    public String toString() {
        return quantity + " x " + bread + " sandwich with " + protein +
                " and " + addOns.toString() + " ($" + String.format("%.2f", price()) + ")";
    }
}