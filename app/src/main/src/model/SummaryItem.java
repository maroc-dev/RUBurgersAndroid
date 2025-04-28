package model;

/**
 * Class for the Item summarys.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class SummaryItem {
    private String label;
    private double amount;

    public SummaryItem(String label, double amount) {
        this.label = label;
        this.amount = amount;
    }

    public String getLabel() {
        return label;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%s: $%.2f", label, amount);
    }
}