package it.polimi.ingsw.model.specialAbilities;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.marbles.WhiteMarble;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WhiteMarbleResourceTest {
    // TODO fix after market change

    GameSettings gs;
    public void setGs () {
        int[] faithTrackVictoryPoints = {0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,0};

        List<VaticanReport> vaticanReports = new ArrayList<>();

        vaticanReports.add(new VaticanReport(5,8,2));
        vaticanReports.add(new VaticanReport(12,15,2));
        vaticanReports.add(new VaticanReport(19,24,4));


        gs = new GameSettings(null, 0, null, null, vaticanReports, faithTrackVictoryPoints);

    }


    @Test
    public void activateTest() {
        setGs();
        Player p = new Player(gs, "test");
        Player p2 = new Player(gs, "test");
        SpecialAbility sa = new WhiteMarbleResource(Resource.GOLD);

        Assert.assertEquals(p.getNumActiveWMR(), 0);

        sa.activate(p);

        Assert.assertEquals(p.getNumActiveWMR(), 1);
        Assert.assertEquals(p.getActivatedWMR().length, 1);
        Assert.assertEquals(p.getActivatedWMR()[0], Resource.GOLD);
        WhiteMarble m = new WhiteMarble();
        Assert.assertEquals(m.getResource(p), Resource.ANY);
        Assert.assertNull(m.getResource(p2));
    }

    @Test
    public void getResTest() {
        setGs();
        Player p = new Player(gs, "test");
        WhiteMarbleResource sa = new WhiteMarbleResource(Resource.GOLD);
        sa.activate(p);

        Assert.assertEquals(sa.getRes(), Resource.GOLD);
    }

    @Test
    public void getTypeTest() {
        WhiteMarbleResource sa = new WhiteMarbleResource(Resource.SHIELD);
        Assert.assertEquals(sa.getType(), SpecialAbilityType.WHITE_MARBLE_RESOURCES);
    }

    @Test
    public void toStringTest() {
        WhiteMarbleResource sa1 = new WhiteMarbleResource(Resource.SHIELD);
        WhiteMarbleResource sa2 = new WhiteMarbleResource(Resource.GOLD);
        WhiteMarbleResource sa3 = new WhiteMarbleResource(Resource.SERVANT);
        WhiteMarbleResource sa4 = new WhiteMarbleResource(Resource.STONE);

        Assert.assertEquals(sa1.toString(),"SA=WMR;R=SHIELD;");
        Assert.assertEquals(sa2.toString(),"SA=WMR;R=GOLD;");
        Assert.assertEquals(sa3.toString(),"SA=WMR;R=SERVANT;");
        Assert.assertEquals(sa4.toString(),"SA=WMR;R=STONE;");

    }


}
