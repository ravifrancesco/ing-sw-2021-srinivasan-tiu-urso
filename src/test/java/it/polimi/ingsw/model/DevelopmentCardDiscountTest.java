package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

public class DevelopmentCardDiscountTest {

    @Test
    public void constructorTest() {
        DevelopmentCardDiscount discount = new DevelopmentCardDiscount(Resource.GOLD, 1);

        Assert.assertEquals(discount.getResource(), Resource.GOLD);
        Assert.assertEquals(discount.getQuantity(), 1);
    }

    @Test
    public void activateTest() {
        DevelopmentCardDiscount discount = new DevelopmentCardDiscount(Resource.GOLD, 1);

        Player p = new Player("test", "1");

        discount.activate(p);

        Assert.assertEquals((p.getActiveDiscounts())[0], discount);
    }
}
