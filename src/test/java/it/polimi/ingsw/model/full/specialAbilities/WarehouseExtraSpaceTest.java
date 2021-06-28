package it.polimi.ingsw.model.full.specialAbilities;

import it.polimi.ingsw.model.full.cards.Banner;
import it.polimi.ingsw.model.full.cards.BannerEnum;
import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.full.table.VaticanReport;
import it.polimi.ingsw.model.utils.GameSettings;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class WarehouseExtraSpaceTest  {

    @Test
    public void activateTest() {
        // copy paste from Giuseppe's DevelopmentCardTest
        Map<Resource, Integer> resourceCostPP = new HashMap<>();
        resourceCostPP.put(Resource.GOLD, 1);
        resourceCostPP.put(Resource.STONE, 1);
        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);
        ProductionPower p = new ProductionPower(resourceCostPP, resourceProduced,2);
        Map<Resource, Integer> resCost = new HashMap<>();
        DevelopmentCard c = new DevelopmentCard(1, 5, resCost, p, null);
        int[] faithTrackVictoryPoints = {0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,0};

        List<VaticanReport> vaticanReports = new ArrayList<>();

        vaticanReports.add(new VaticanReport(5,8,2));
        vaticanReports.add(new VaticanReport(12,15,2));
        vaticanReports.add(new VaticanReport(19,24,4));


        GameSettings gameSettings = new GameSettings(null, 0, null, null, vaticanReports, faithTrackVictoryPoints);

        int thrownExceptions = 0;
        Player player = new Player(gameSettings, "test");
        ArrayList<Resource> res = new ArrayList<>();
        res.add(Resource.STONE);
        // no wes, shit shouldn't work
        Assert.assertEquals(thrownExceptions, 0);
        try {
            player.getDashboard().addResourcesToSupply(res);
            player.getDashboard().storeFromSupplyInExtraDeposit(0, 0, 1);
        } catch (Exception ignored) {
            thrownExceptions += 1;
        }
        Assert.assertEquals(thrownExceptions, 1);

        WarehouseExtraSpace wes = new WarehouseExtraSpace(Resource.GOLD);
        wes.setLeaderCardPos(0);
        wes.activate(player);
        player.getDashboard().placeLeaderCard(createLeaderCard(wes));
        // shouldn't work, wrong resource
        try {
            player.getDashboard().storeFromSupplyInExtraDeposit(0, 0, 1);
        } catch (IllegalStateException e) {
            thrownExceptions += 1;
        }

        Assert.assertEquals(thrownExceptions, 2);

        ArrayList<Resource> res2 = new ArrayList<>();
        res2.add(Resource.GOLD);
        player.getDashboard().addResourcesToSupply(res2);

        player.getDashboard().storeFromSupplyInExtraDeposit(0, 1, 0);


        Map<Resource, Integer> resources = player.getDashboard().getAllPlayerResources();

        Assert.assertEquals(java.util.Optional.ofNullable(resources.get(Resource.GOLD)), Optional.of(1));
        Assert.assertEquals(java.util.Optional.ofNullable(resources.get(Resource.STONE)), Optional.of(0));
        Assert.assertEquals(java.util.Optional.ofNullable(resources.get(Resource.SERVANT)), Optional.of(0));
        Assert.assertEquals(java.util.Optional.ofNullable(resources.get(Resource.SHIELD)), Optional.of(0));
    }

    public LeaderCard createLeaderCard(WarehouseExtraSpace wes) {
        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        return new LeaderCard(1, 5, bannerCost, new HashMap<>(), wes);
    }

    @Test
    public void getTypeTest() {
        WarehouseExtraSpace wes = new WarehouseExtraSpace(Resource.GOLD);
        Assert.assertEquals(wes.getType(), SpecialAbilityType.WAREHOUSE_EXTRA_SPACE);
    }

    @Test
    public void getStoredResources() {
        WarehouseExtraSpace wes = new WarehouseExtraSpace(Resource.GOLD);
        Assert.assertEquals(wes.getStoredResource(), Resource.GOLD);
    }

    @Test
    public void toStringTest() {
        WarehouseExtraSpace wes1 = new WarehouseExtraSpace(Resource.GOLD);
        WarehouseExtraSpace wes2 = new WarehouseExtraSpace(Resource.STONE);
        WarehouseExtraSpace wes3 = new WarehouseExtraSpace(Resource.SHIELD);
        WarehouseExtraSpace wes4 = new WarehouseExtraSpace(Resource.SERVANT);

        Assert.assertEquals(wes1.toString(),"SA=WES;R=GOLD;");
        Assert.assertEquals(wes2.toString(),"SA=WES;R=STONE;");
        Assert.assertEquals(wes3.toString(),"SA=WES;R=SHIELD;");
        Assert.assertEquals(wes4.toString(),"SA=WES;R=SERVANT;");
    }



}
