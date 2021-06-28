package it.polimi.ingsw.model.full.table;

import it.polimi.ingsw.model.full.table.FaithTrack;
import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.full.table.VaticanReport;
import it.polimi.ingsw.model.utils.GameSettings;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assumptions;

import java.util.ArrayList;
import java.util.List;

public class FaithTrackTest {

    @Test
    public void constructorTest() {

        int[] faithTrackVictoryPoints = {0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,0};

        List<VaticanReport> vaticanReports = new ArrayList<>();

        vaticanReports.add(new VaticanReport(5,8,2));
        vaticanReports.add(new VaticanReport(12,15,2));
        vaticanReports.add(new VaticanReport(19,24,4));


        GameSettings gameSettings = new GameSettings(null, 0, null, null, vaticanReports, faithTrackVictoryPoints);

        Player player = new Player(gameSettings, "test");

        FaithTrack faithTrack = new FaithTrack(gameSettings, player.getDashboard());

        Assert.assertEquals(0, faithTrack.getVictoryPoints());
        Assert.assertEquals(0, faithTrack.getPosition());

    }

    @Test
    public void moveFaithMarkerTest() {

        int[] faithTrackVictoryPoints = {0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,0};

        List<VaticanReport> vaticanReports = new ArrayList<>();

        vaticanReports.add(new VaticanReport(5,8,2));
        vaticanReports.add(new VaticanReport(12,15,2));
        vaticanReports.add(new VaticanReport(19,24,4));


        GameSettings gameSettings = new GameSettings(null, 0, null, null, vaticanReports, faithTrackVictoryPoints);

        Player player = new Player(gameSettings, "test");

        FaithTrack faithTrack = new FaithTrack(gameSettings, player.getDashboard());

        faithTrack.moveFaithMarker(15);

        Assert.assertEquals(15, faithTrack.getPosition());

        faithTrack.moveFaithMarker(5);

        Assert.assertEquals(20, faithTrack.getPosition());

        //Assumptions.assumeTrue(faithTrack.moveFaithMarker(4)); // TODO change

    }

    @Test
    public void getVictoryPointsTest() {

        int[] faithTrackVictoryPoints = {0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,0};

        List<VaticanReport> vaticanReports = new ArrayList<>();

        vaticanReports.add(new VaticanReport(5,8,2));
        vaticanReports.add(new VaticanReport(12,16,3));
        vaticanReports.add(new VaticanReport(19,24,4));


        GameSettings gameSettings = new GameSettings(null, 0, null, null, vaticanReports, faithTrackVictoryPoints);

        Player player = new Player(gameSettings, "test");

        FaithTrack faithTrack = new FaithTrack(gameSettings, player.getDashboard());

        faithTrack.moveFaithMarker(4);

        Assert.assertEquals(1, faithTrack.getVictoryPoints());

        faithTrack.moveFaithMarker(11);

        Assert.assertEquals(24, faithTrack.getVictoryPoints());

    }

    @Test
    public void checkVaticanVictoryTest() {

        int[] faithTrackVictoryPoints = {0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,0};

        List<VaticanReport> vaticanReports = new ArrayList<>();

        vaticanReports.add(new VaticanReport(5,8,2));
        vaticanReports.add(new VaticanReport(12,15,2));
        vaticanReports.add(new VaticanReport(19,24,4));


        GameSettings gameSettings = new GameSettings(null, 0, null, null, vaticanReports, faithTrackVictoryPoints);

        Player player = new Player(gameSettings, "test");

        FaithTrack faithTrack = new FaithTrack(gameSettings, player.getDashboard());

        Assumptions.assumeFalse(faithTrack.getVaticanReports().get(8).isAchieved());
        Assumptions.assumeFalse(faithTrack.getVaticanReports().get(8).isMissed());
        Assumptions.assumeFalse(faithTrack.getVaticanReports().get(15).isAchieved());
        Assumptions.assumeFalse(faithTrack.getVaticanReports().get(15).isMissed());
        Assumptions.assumeFalse(faithTrack.getVaticanReports().get(24).isAchieved());
        Assumptions.assumeFalse(faithTrack.getVaticanReports().get(24).isMissed());

        faithTrack.moveFaithMarker(5);
        faithTrack.checkVaticanVictoryPoints(8);
        Assert.assertEquals(faithTrack.getVictoryPoints(), 3);
        Assumptions.assumeTrue(faithTrack.getVaticanReports().get(8).isAchieved());
        Assumptions.assumeFalse(faithTrack.getVaticanReports().get(8).isMissed());
        Assumptions.assumeFalse(faithTrack.getVaticanReports().get(15).isAchieved());
        Assumptions.assumeFalse(faithTrack.getVaticanReports().get(15).isMissed());
        Assumptions.assumeFalse(faithTrack.getVaticanReports().get(24).isAchieved());
        Assumptions.assumeFalse(faithTrack.getVaticanReports().get(24).isMissed());

        faithTrack.checkVaticanVictoryPoints(15);
        Assumptions.assumeTrue(faithTrack.getVaticanReports().get(8).isAchieved());
        Assumptions.assumeFalse(faithTrack.getVaticanReports().get(8).isMissed());
        Assumptions.assumeFalse(faithTrack.getVaticanReports().get(15).isAchieved());
        Assumptions.assumeTrue(faithTrack.getVaticanReports().get(15).isMissed());
        Assumptions.assumeFalse(faithTrack.getVaticanReports().get(24).isAchieved());
        Assumptions.assumeFalse(faithTrack.getVaticanReports().get(24).isMissed());

    }

}
