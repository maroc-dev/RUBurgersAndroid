package model;

/**
 * Enum representing different types of protein options available for sandwiches and burgers.
 *
 * Each protein type has an associated base price.
 * Provides methods to calculate price adjustments, especially for double patty burgers.
 *
 * Used primarily in Sandwich and Burger menu items.
 *
 * Available proteins include Roast Beef, Salmon, Chicken, and Beef Patty.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public enum Protein {
    ROAST_BEEF(10.99),
    SALMON(9.99),
    CHICKEN(8.99),
    BEEF_PATTY(6.99);
    private final double basePrice;

    /**
     * Constructs a Protein enum constant with the specified base price.
     *
     * @param basePrice the base price for the protein
     */
    Protein(double basePrice) {
        this.basePrice = basePrice;
    }

    /**
     * Calculates the price of the protein, adjusting for double patties if applicable.
     * Only applies an additional charge for double beef patties.
     *
     * @param doublePatty true if a double patty is selected; false otherwise
     * @return the price of the protein based on patty selection
     */
    public double price(boolean doublePatty) {
        if (this == BEEF_PATTY && doublePatty) {
            return basePrice + 2.50;
        }
        return basePrice;
    }

    /**
     * Retrieves the base price of the protein without any adjustments.
     *
     * @return the base price of the protein
     */
    public double price() {
        return basePrice;
    }
}