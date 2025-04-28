package model;

/**
 * Enum for the Protein.
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

    Protein(double basePrice) {
        this.basePrice = basePrice;
    }

    public double price(boolean doublePatty) {
        if (this == BEEF_PATTY && doublePatty) {
            return basePrice + 2.50;
        }
        return basePrice;
    }

    public double price() {
        return basePrice;
    }
}
