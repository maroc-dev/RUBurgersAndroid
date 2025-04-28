package model;

/**
 * Class for the Beverage.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class Beverage extends MenuItem {
    private Size size;
    private Flavor flavor;


    public Beverage(Size size, Flavor flavor, int quantity) {
        this.size = size;
        this.flavor = flavor;
        this.quantity = quantity;
    }

    public Size getSize() {
        return size;
    }

    public Flavor getFlavor() {
        return flavor;
    }

    @Override
    public double price() {
        double basePrice = switch (size) {
            case SMALL -> 1.99;
            case MEDIUM -> 2.49;
            case LARGE -> 2.99;
        };

        return basePrice * quantity;
    }

    @Override
    public String toString() {
        return quantity + " x " + size + " " + flavor + " Beverage ($" + String.format("%.2f", price()) + ")";
    }
}