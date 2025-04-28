package model;

/**
 * Enum representing the side item options available for orders and combo meals.
 *
 * Each side has a base price, and the price can vary based on the selected size (small, medium, or large).
 *
 * Calculates side pricing based on size selection.
 *
 * Used in menu customization and combo meal building.
 *
 * Available sides include Chips, Fries, Onion Rings, and Apple Slices.
 *
 * @see Size
 * @see Combo
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

    /**
     * Constructs a Side enum constant with the specified base price.
     *
     * @param basePrice the base price of the side item
     */
    Side(double basePrice) {
        this.basePrice = basePrice;
    }

    /**
     * Calculates the price of the side based on the selected size.
     *
     * The price increases by $0.50 for medium and $1.00 for large compared to small.
     *
     * @param size the selected size (SMALL, MEDIUM, LARGE)
     * @return the calculated price based on size
     */
    public double getPriceBySize(Size size) {
        return switch (size) {
            case SMALL -> basePrice;
            case MEDIUM -> basePrice + 0.50;
            case LARGE -> basePrice + 1.00;
        };
    }
}