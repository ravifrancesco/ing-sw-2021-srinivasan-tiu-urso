package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class WhiteMarbleResourceTest {
    @Test
    public void activateTest() {
        Player p = new Player("rbta-svg", "001");
        Player p2 = new Player("no-wmr", "002");
        SpecialAbility sa = new WhiteMarbleResource(Resource.GOLD);

        Assert.assertEquals(p.getNumActiveWMR(), 0);

        sa.activate(p);

        Assert.assertEquals(p.getNumActiveWMR(), 1);
        Assert.assertEquals(p.getActivatedWMR().length, 1);
        Assert.assertEquals(p.getActivatedWMR()[0].getRes(), Resource.GOLD);
        WhiteMarble m = new WhiteMarble();
        Assert.assertEquals(m.getResource(p), Resource.GOLD);
        Assert.assertNull(m.getResource(p2));
    }
}
