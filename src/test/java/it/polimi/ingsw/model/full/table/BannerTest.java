package it.polimi.ingsw.model.full.table;

import it.polimi.ingsw.model.full.cards.Banner;
import it.polimi.ingsw.model.full.cards.BannerEnum;
import org.junit.Assert;
import org.junit.Test;

public class BannerTest {

    @Test
    public void constructorTest() {
        Banner banner = new Banner(BannerEnum.BLUE, 3);

        Assert.assertEquals(banner.getColor(), BannerEnum.BLUE);
        Assert.assertEquals(banner.getLevel(), 3);
    }

    @Test
    public void equalsColorTest() {
        Banner banner1 = new Banner(BannerEnum.BLUE, 3);
        Banner banner2 = new Banner(BannerEnum.BLUE, 2);
        Banner banner3 = new Banner(BannerEnum.GREEN, 1);

        Assert.assertTrue(banner1.equalsColor(banner2));
        Assert.assertFalse(banner1.equalsColor(banner3));
        Assert.assertFalse(banner2.equalsColor(banner3));
    }

    @Test
    public void equalsLevelTest() {
        Banner banner1 = new Banner(BannerEnum.GREEN, 3);
        Banner banner2 = new Banner(BannerEnum.BLUE, 1);
        Banner banner3 = new Banner(BannerEnum.GREEN, 1);

        Assert.assertFalse(banner1.equalsLevel(banner2));
        Assert.assertFalse(banner1.equalsLevel(banner3));
        Assert.assertTrue(banner2.equalsLevel(banner3));
    }

    @Test
    public void equalsTest() {
        Banner banner1 = new Banner(BannerEnum.BLUE, 3);
        Banner banner2 = new Banner(BannerEnum.BLUE, 1);
        Banner banner3 = new Banner(BannerEnum.GREEN, 1);
        Banner banner4 = new Banner(BannerEnum.BLUE, 3);

        Assert.assertFalse(banner1.equals(banner2));
        Assert.assertFalse(banner1.equals(banner3));
        Assert.assertTrue(banner1.equals(banner4));
        Assert.assertFalse(banner2.equals(banner3));
        Assert.assertFalse(banner2.equals(banner4));
        Assert.assertFalse(banner3.equals(banner4));
    }

    @Test
    public void toStringTest() {
        Banner banner1 = new Banner(BannerEnum.BLUE, 3);
        String string = "BA=BLUE:3;";

        Assert.assertEquals(banner1.toString(), string);
    }

    @Test
    public void isOneLessTest() {
        Banner banner1 = new Banner(BannerEnum.BLUE, 1);
        Banner banner2 = new Banner(BannerEnum.BLUE, 2);
        Banner banner3 = new Banner(BannerEnum.GREEN, 3);
        Banner banner4 = new Banner(BannerEnum.YELLOW, 3);

        Assert.assertTrue(banner1.isOneLess(banner2));
        Assert.assertFalse(banner1.isOneLess(banner3));
        Assert.assertFalse(banner1.isOneLess(banner4));
        Assert.assertTrue(banner2.isOneLess(banner3));
        Assert.assertTrue(banner2.isOneLess(banner4));
        Assert.assertFalse(banner3.isOneLess(banner4));
    }
}