package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

public class MarbleTest {

    @Test
    public void greyGetResources() {
        Marble a = new GreyMarble();
        Player p = new Player();

        Assert.assertEquals(a.getResource(p), Resource.STONE);
    }

    @Test
    public void blueGetResources() {
        Marble a = new BlueMarble();
        Player p = new Player();

        Assert.assertEquals(a.getResource(p), Resource.SHIELD);
    }

    @Test
    public void yellowGetResources() {
        Marble a = new YellowMarble();
        Player p = new Player();

        Assert.assertEquals(a.getResource(p), Resource.GOLD);
    }

    @Test
    public void purpleGetResources() {
        Marble a = new PurpleMarble();
        Player p = new Player();

        Assert.assertEquals(a.getResource(p), Resource.SERVANT);
    }

}
