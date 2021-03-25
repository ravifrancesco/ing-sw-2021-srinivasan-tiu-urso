package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevelopmentCardTest {

    @Test
    public void constructorTest(){
        Map<Resource, Integer> resourceCostPP = new HashMap<>();
        resourceCostPP.put(Resource.GOLD, 1);
        resourceCostPP.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceCostPP, resourceProduced,2, false);

        Map<Resource, Integer> resourceCostDC = new HashMap<>();
        resourceCostDC.put(Resource.SERVANT, 3);

        DevelopmentCard c = new DevelopmentCard(5, null, null);

        Assert.assertFalse(c.isPlayed());
        Assert.assertFalse(c.isActivatable());
    }

    @Test
    public void playTest(){
        Map<Resource, Integer> resourceCostPP = new HashMap<>();
        resourceCostPP.put(Resource.GOLD, 1);
        resourceCostPP.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceCostPP, resourceProduced,2, false);

        Map<Resource, Integer> resourceCostDC = new HashMap<>();
        resourceCostDC.put(Resource.SERVANT, 3);

        DevelopmentCard c = new DevelopmentCard(5, null, null);

        c.play(null, 0);

        Assert.assertTrue(c.isPlayed());
        Assert.assertTrue(c.isActivatable());
    }

    @Test
    public void substitutionTest(){
        Map<Resource, Integer> resourceCostPP = new HashMap<>();
        resourceCostPP.put(Resource.GOLD, 1);
        resourceCostPP.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceCostPP, resourceProduced,2, false);

        Map<Resource, Integer> resourceCostDC = new HashMap<>();
        resourceCostDC.put(Resource.SERVANT, 3);

        DevelopmentCard c = new DevelopmentCard(5, null, null);

        c.play(null, 0);

        c.substitute();

        Assert.assertTrue(c.isPlayed());
        Assert.assertFalse(c.isActivatable());
    }

    @Test
    public void resetTest(){
        Map<Resource, Integer> resourceCostPP = new HashMap<>();
        resourceCostPP.put(Resource.GOLD, 1);
        resourceCostPP.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceCostPP, resourceProduced,2, false);

        Map<Resource, Integer> resourceCostDC = new HashMap<>();
        resourceCostDC.put(Resource.SERVANT, 3);

        DevelopmentCard c = new DevelopmentCard(5, null, null);

        c.play(null, 0);

        c.substitute();

        c.reset();

        Assert.assertFalse(c.isPlayed());
        Assert.assertFalse(c.isActivatable());
    }

    @Test
    public void movingFaithMarkerTest(){
        Map<Resource, Integer> resourceCostPP = new HashMap<>();
        resourceCostPP.put(Resource.GOLD, 1);
        resourceCostPP.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceCostPP, resourceProduced,2, false);

        Map<Resource, Integer> resourceCostDC = new HashMap<>();
        resourceCostDC.put(Resource.SERVANT, 3);

        DevelopmentCard c = new DevelopmentCard(5, null, p);

        int[] faithTrackVictoryPoints = {0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,0};

        List<VaticanReport> vaticanReports = new ArrayList<>();

        vaticanReports.add(new VaticanReport(5,8,2));
        vaticanReports.add(new VaticanReport(12,15,2));
        vaticanReports.add(new VaticanReport(19,24,4));


        GameSettings gameSettings = new GameSettings(null, 0, null, null, vaticanReports, faithTrackVictoryPoints);

        FaithTrack faithTrack = new FaithTrack(gameSettings);

        Dashboard dashboard = new Dashboard(null, faithTrack, null, null);
        Player player = new Player("nickname", "AAA123", dashboard);

        dashboard.setPlayer(player);

        c.play(dashboard, 0);
        c.activate(dashboard);

        Assert.assertEquals(faithTrack.getPosition(),2);
        Assert.assertTrue(c.isPlayed());
        Assert.assertTrue(c.isActivatable());
    }
}
