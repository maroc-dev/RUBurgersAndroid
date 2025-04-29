package model;

/**
 * Class representing a combo meal item that includes a sandwich, beverage, and side.
 *
 * A combo meal allows only specific side items (chips or apple slices)
 * and specific beverage options (medium-sized cola, tea, or juice).
 *
 * Extends the MenuItem class and calculates a discounted combo price.
 *
 * Enforces rules regarding allowed sides and beverages for a valid combo.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class Combo extends MenuItem {
    private Sandwich sandwich;
    private Beverage drink;
    private Side side;
    private int quantity = 1;

    /**
     * Constructs a new Combo meal with the specified sandwich, beverage, side, and quantity.
     * Validates that the side and drink meet combo meal requirements.
     *
     * @param sandwich the sandwich selected for the combo
     * @param drink the beverage selected for the combo
     * @param side the side selected for the combo
     * @param quantity the number of combo meals ordered
     * @throws IllegalArgumentException if the side or drink does not meet combo requirements
     */
    public Combo(Sandwich sandwich, Beverage drink, Side side, int quantity) {
        this.sandwich = sandwich;
        this.drink = drink;
        this.side = side;

        if (side != Side.CHIPS && side != Side.APPLE_SLICES) {
            throw new IllegalArgumentException("Combos only allow chips or apple slices as the side.");
        }

        // Only allow medium drinks of cola, tea, or juice
        if (drink.getSize() != Size.MEDIUM ||
                !(drink.getFlavor() == Flavor.COLA || drink.getFlavor() == Flavor.TEA || drink.getFlavor() == Flavor.JUICE)) {
            throw new IllegalArgumentException("Combos only allow a medium cola, tea, or juice as the drink.");
        }
        this.quantity = quantity;
    }

    /**
     * Calculates the total price for the combo meal.
     * Applies a $1.00 discount to the combined price of sandwich, drink, and side.
     *
     * @return the discounted total price for the combo
     */
    @Override
    public double price() {
        double comboPrice = sandwich.price() + drink.price() + side.getPriceBySize(Size.SMALL);
        return comboPrice - 1.00;
    }

    /**
     * Returns a string representation of the combo meal,
     * including the sandwich, side, drink, and total price.
     *
     * @return formatted string with combo meal details
     */
    @Override
    public String toString() {
        return quantity + "x Combo Meal with " +
                sandwich.toComboString() +
                ", " + side.name().toLowerCase().replace("_", " ") +
                ", and " + drink.getFlavor().toString().toLowerCase() + " drink. Total:" +
                String.format("%.2f", price() * quantity) + ")";
    }
}