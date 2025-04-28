package model;

/**
 * Class for the Burger.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class Combo extends MenuItem{
    private Sandwich sandwich;
    private Beverage drink;
    private Side side;
    private int quantity = 1;

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

    @Override
    public double price() {
        double comboPrice = sandwich.price() + drink.price() + side.getPriceBySize(Size.SMALL);
        return comboPrice - 1.00;
    }
    @Override
    public String toString() {
        return quantity + "x Combo Meal with " +
                sandwich.toComboString()  +
                ", " + side.name().toLowerCase().replace("_", " ") +
                ", and " + drink.getFlavor().toString().toLowerCase() + " drink. Total:" + String.format("%.2f", price() * quantity) + ")";
    }
}