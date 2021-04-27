package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.specialAbilities.ProductionPower;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevelopmentCardTest {
    /*

    @Test
    public void toStringTest() {
        Banner banner = new Banner(BannerEnum.BLUE, 2);

        Map<Resource, Integer> resourceCost = new HashMap<>();
        resourceCost.put(Resource.SERVANT, 3);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2, false);

        Map<Resource, Integer> resourceCostDC = new HashMap<>();
        resourceCostDC.put(Resource.SERVANT, 3);

        DevelopmentCard c = new DevelopmentCard(1, 5, resourceCost, p, banner);

        String s1 = "ID=1;VP=5;BA=BLUE:2;RC=SERVANT:3;SA=PP;RR=STONE:1,GOLD:1;RP=SHIELD:1;FP=2;SR=n;";
        String s2 = "ID=1;VP=5;BA=BLUE:2;RC=SERVANT:3;SA=PP;RR=GOLD:1,STONE:1;RP=SHIELD:1;FP=2;SR=n;";

        Assert.assertTrue(s1.equals(c.toString()) || s2.equals(c.toString()));
    }

    @Test
    public void getterTest() {
        Banner banner = new Banner(BannerEnum.BLUE, 2);

        Map<Resource, Integer> resourceCost = new HashMap<>();
        resourceCost.put(Resource.SERVANT, 3);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2, false);

        Map<Resource, Integer> resourceCostDC = new HashMap<>();
        resourceCostDC.put(Resource.SERVANT, 3);

        DevelopmentCard c = new DevelopmentCard(1, 5, resourceCost, p, banner);

        Assert.assertEquals(c.getId(), 1);
        Assert.assertEquals(c.getVictoryPoints(), 5);
        Assert.assertEquals(c.getBanner().getColor(), BannerEnum.BLUE);
        Assert.assertEquals(c.getBanner().getLevel(), 2);
        Assert.assertEquals(c.getResourceCost(), resourceCost);
        Assert.assertEquals(c.getProductionPower(), p);
    }

    @Test
    public void playTest() {
        // TODO after implementing Dashboard class
    }

    @Test
    public void movingFaithMarkerTest() {
        Map<Resource, Integer> resourceCostPP = new HashMap<>();
        resourceCostPP.put(Resource.GOLD, 1);
        resourceCostPP.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceCostPP, resourceProduced,2, false);

        Map<Resource, Integer> resourceCostDC = new HashMap<>();
        resourceCostDC.put(Resource.SERVANT, 3);

        DevelopmentCard c = new DevelopmentCard(1, 5, resourceCostDC, p, null);

        int[] faithTrackVictoryPoints = {0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,0};

        List<VaticanReport> vaticanReports = new ArrayList<>();

        vaticanReports.add(new VaticanReport(5,8,2));
        vaticanReports.add(new VaticanReport(12,15,2));
        vaticanReports.add(new VaticanReport(19,24,4));


        GameSettings gameSettings = new GameSettings(null, 0, null, null, vaticanReports, faithTrackVictoryPoints);

        FaithTrack faithTrack = new FaithTrack(gameSettings);

        Dashboard dashboard = new Dashboard(gameSettings, null);
        Player player = new Player("nickname", "AAA123");

        player.setDashboard(dashboard);
        dashboard.setPlayer(player);

        c.play(dashboard, 0);
        c.activate(player);

        Assert.assertEquals(dashboard.getFaithMarkerPosition(),2);
    }

    @Test
    public void storeResourcesTest() {
        // TODO after implementing Warehouse class
    }

    @Test
    public void storingAndFMTest() {
        // TODO after implementing Warehouse class
    }

    @Test
    public void giveSelectableResourceTest() {
        // TODO after implementing Dashboard and Warehouse classes
    }

    @Test
    public void trueIsActivatableTest() {
        Banner banner = new Banner(BannerEnum.BLUE, 2);

        Map<Resource, Integer> resourceCost = new HashMap<>();
        resourceCost.put(Resource.SERVANT, 3);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2, false);

        Map<Resource, Integer> resourceCostDC = new HashMap<>();
        resourceCostDC.put(Resource.SERVANT, 3);

        DevelopmentCard c = new DevelopmentCard(1, 5, resourceCost, p, banner);

        Map<Resource, Integer> playerResources = new HashMap<>();
        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.SERVANT, 1);
        playerResources.put(Resource.STONE, 3);

        Assert.assertTrue(c.isActivatable(playerResources));
    }

    @Test
    public void falseIsActivatableTest() {
        Banner banner = new Banner(BannerEnum.BLUE, 2);

        Map<Resource, Integer> resourceCost = new HashMap<>();
        resourceCost.put(Resource.SERVANT, 3);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2, false);

        Map<Resource, Integer> resourceCostDC = new HashMap<>();
        resourceCostDC.put(Resource.SERVANT, 3);

        DevelopmentCard c = new DevelopmentCard(1, 5, resourceCost, p, banner);

        Map<Resource, Integer> playerResources = new HashMap<>();
        playerResources.put(Resource.GOLD, 1);
        playerResources.put(Resource.SERVANT, 1);
        playerResources.put(Resource.SHIELD, 3);

        Assert.assertFalse(c.isActivatable(playerResources));
    }

     */
}
