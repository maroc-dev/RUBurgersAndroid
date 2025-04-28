package test;

import static org.junit.Assert.assertEquals;
import model.AddOns;
import model.Protein;
import model.Burger;
import model.Bread;

import java.util.ArrayList;
public class BurgerTest {
    @org.junit.Test
    public void testSinglePattyBurgerPrice() {
        ArrayList<AddOns> addOns = new ArrayList<>();
        addOns.add(AddOns.LETTUCE); // $0.30
        addOns.add(AddOns.CHEESE);  // $1.00

        Bread bread = Bread.WHEAT; // Assume $0.00
        Protein protein = Protein.BEEF_PATTY; // $6.99
        Burger burger = new Burger(bread, addOns, 1, false); // single patty

        double expected = (protein.price(false) + 0.30 + 1.00);
        assertEquals(expected, burger.price(), 0.01);
    }

    @org.junit.Test
    public void testDoublePattyBurgerPrice() {
        ArrayList<AddOns> addOns = new ArrayList<>();
        addOns.add(AddOns.AVOCADOS); // $0.50
        addOns.add(AddOns.TOMATOES); // $0.30

        Bread bread = Bread.PRETZEL; // Assume $0.00
        Protein protein = Protein.BEEF_PATTY; // $6.99 + $2.50 for double
        Burger burger = new Burger(bread, addOns, 1, true); // double patty

        double expected = (protein.price(true) + 0.50 + 0.30);
        assertEquals(expected, burger.price(), 0.01);
    }
}
