package model;

/**
 * Enum for the Sides.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public enum Side {
    CHIPS(1.99),
    FRIES(2.49),
    ONION_RINGS(3.29),
    APPLE_SLICES(1.29);

    private final double basePrice;

    Side(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getPriceBySize(Size size) {
        return switch (size) {
            case SMALL -> basePrice;
            case MEDIUM -> basePrice + 0.50;
            case LARGE -> basePrice + 1.00;
        };
    }
}
