package it.polimi.ingsw.model.marbles;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.marbles.*;
import org.junit.Assert;
import org.junit.Test;

public class MarbleTest {

    @Test
    public void greyGetResources() {
        Marble a = new GreyMarble();
        Player p = new Player("test", "0");

        Assert.assertEquals(a.getResource(p), Resource.STONE);
    }

    @Test
    public void blueGetResources() {
        Marble a = new BlueMarble();
        Player p = new Player("test", "0");

        Assert.assertEquals(a.getResource(p), Resource.SHIELD);
    }

    @Test
    public void yellowGetResources() {
        Marble a = new YellowMarble();
        Player p = new Player("test", "0");

        Assert.assertEquals(a.getResource(p), Resource.GOLD);
    }

    @Test
    public void purpleGetResources() {
        Marble a = new PurpleMarble();
        Player p = new Player("test", "0");

        Assert.assertEquals(a.getResource(p), Resource.SERVANT);
    }

    // TODO add test for white and red marble after implementation

}
