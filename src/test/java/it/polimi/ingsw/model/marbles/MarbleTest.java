package it.polimi.ingsw.model.marbles;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.marbles.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MarbleTest {

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
    public void greyGetResources() {
        setGs();
        Marble a = new GreyMarble();
        Player p = new Player(gs, "test");

        Assert.assertEquals(a.getResource(p), Resource.STONE);
    }

    @Test
    public void blueGetResources() {
        setGs();

        Marble a = new BlueMarble();
        Player p = new Player(gs, "test");

        Assert.assertEquals(a.getResource(p), Resource.SHIELD);
    }

    @Test
    public void yellowGetResources() {
        setGs();

        Marble a = new YellowMarble();
        Player p = new Player(gs, "test");

        Assert.assertEquals(a.getResource(p), Resource.GOLD);
    }

    @Test
    public void purpleGetResources() {
        setGs();

        Marble a = new PurpleMarble();
        Player p = new Player(gs, "test");

        Assert.assertEquals(a.getResource(p), Resource.SERVANT);
    }

    @Test
    public void whiteGetResources() {
        setGs();

        Marble w = new WhiteMarble();
        Player p = new Player(gs, "test");

        // case 1: no WMR
        Assert.assertNull(w.getResource(p));

    }

    @Test
    public void redGetResources() {

        int[] faithTrackVictoryPoints = {0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,0};

        List<VaticanReport> vaticanReports = new ArrayList<>();

        vaticanReports.add(new VaticanReport(5,8,2));
        vaticanReports.add(new VaticanReport(12,15,2));
        vaticanReports.add(new VaticanReport(19,24,4));


        GameSettings gameSettings = new GameSettings(null, 0, null, null, vaticanReports, faithTrackVictoryPoints);

        Marble w = new RedMarble();
        Player p = new Player(gameSettings, "test");

        Assert.assertEquals(p.getDashboard().getFaithMarkerPosition(), 0);

        w.getResource(p);

        Assert.assertEquals(p.getDashboard().getFaithMarkerPosition(), 1);

    }


}
