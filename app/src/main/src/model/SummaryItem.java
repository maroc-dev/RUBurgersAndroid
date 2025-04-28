package model;

/**
 * Class representing a summary line item, typically used for displaying financial summaries
 * such as subtotals, taxes, and totals in an order receipt.
 *
 * Each summary item contains a descriptive label and an associated monetary amount.
 *
 * Provides access to label and amount fields and a formatted string representation.
 *
 * Used to help format and display order summaries or payment breakdowns.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class SummaryItem {
    /** The label describing the summary item (e.g., "Subtotal", "Tax", "Total") */
    private String label;

    /** The monetary amount associated with the summary item */
    private double amount;

    /**
     * Constructs a new SummaryItem with the specified label and amount.
     *
     * @param label the description for the summary item
     * @param amount the monetary value of the summary item
     */
    public SummaryItem(String label, double amount) {
        this.label = label;
        this.amount = amount;
    }

    /**
     * Retrieves the label of the summary item.
     *
     * @return the label describing the summary item
     */
    public String getLabel() {
        return label;
    }

    /**
     * Retrieves the amount associated with the summary item.
     *
     * @return the monetary amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns a formatted string representation of the summary item,
     * displaying the label followed by the amount in currency format.
     *
     * @return formatted string with label and amount
     */
    @Override
    public String toString() {
        return String.format("%s: $%.2f", label, amount);
    }
}