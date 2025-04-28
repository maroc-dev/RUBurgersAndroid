package test;

import model.AddOns;
import model.Protein;
import model.Sandwich;
import model.Bread;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class SandwichTest {
    @org.junit.Test
    public void testPriceWithChickenAndNoAddOns() {
        ArrayList<AddOns> addOns = new ArrayList<>();
        Sandwich sandwich = new Sandwich(Bread.BRIOCHE, Protein.CHICKEN, addOns, 1);
        double expected = Protein.CHICKEN.price(); // 8.99
        assertEquals(expected, sandwich.price(), 0.01);
    }

    @org.junit.Test
    public void testPriceWithRoastBeefAndCheeseLettuce() {
        ArrayList<AddOns> addOns = new ArrayList<>();
        addOns.add(AddOns.CHEESE);   // +1.00
        addOns.add(AddOns.LETTUCE);  // +0.30

        Sandwich sandwich = new Sandwich(Bread.WHEAT, Protein.ROAST_BEEF, addOns, 1);
        double expected = Protein.ROAST_BEEF.price() + AddOns.CHEESE.getPrice() + AddOns.LETTUCE.getPrice(); // 10.99 + 1.00 + 0.30
        assertEquals(expected, sandwich.price(), 0.01);
    }

    @org.junit.Test
    public void testPriceWithSalmonAndMultipleAddOnsDoubleQuantity() {
        ArrayList<AddOns> addOns = new ArrayList<>();
        addOns.add(AddOns.CHEESE);     // 1.00
        addOns.add(AddOns.TOMATOES);     // 0.30
        addOns.add(AddOns.AVOCADOS);    // 0.50

        Sandwich sandwich = new Sandwich(Bread.PRETZEL, Protein.SALMON, addOns, 2);
        double single = Protein.SALMON.price() + 1.00 + 0.30 + 0.50; // 9.99 + 1.80
        double expected = single * 2;
        assertEquals(expected, sandwich.price(), 0.01);
    }
    }
