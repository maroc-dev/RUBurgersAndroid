package model;

import java.util.ArrayList;

/**
 * Class for the Sandwich.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class Sandwich extends MenuItem {
    protected Bread bread;
    protected Protein protein;
    protected ArrayList<AddOns> addOns;

    public Sandwich(Bread bread, Protein protein, ArrayList<AddOns> addOns, int quantity) {
        this.bread = bread;
        this.protein = protein;
        this.addOns = addOns;
        this.quantity = quantity;
    }

    @Override
    public double price() {
        double basePrice = protein.price();
        for (AddOns addOn : addOns) {
            basePrice += addOn.getPrice();
        }
        return basePrice * quantity;
    }

    public String toComboString() {
        return quantity + " x " + bread + " sandwich with " + protein +
                " and " + addOns.toString();
    }

    @Override
    public String toString() {
        return quantity + " x " + bread + " sandwich with " + protein +
                " and " + addOns.toString() + " ($" + String.format("%.2f", price()) + ")";
    }
}