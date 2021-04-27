package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.specialAbilities.ProductionPower;
import it.polimi.ingsw.model.specialAbilities.SpecialAbility;
import it.polimi.ingsw.model.specialAbilities.WarehouseExtraSpace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarehouseExtraSpaceTest  {
    /*
    @Test
    public void activateTest() {
        // copy paste from Giuseppe's DevelopmentCardTest
        Map<Resource, Integer> resourceCostPP = new HashMap<>();
        resourceCostPP.put(Resource.GOLD, 1);
        resourceCostPP.put(Resource.STONE, 1);
        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);
        ProductionPower p = new ProductionPower(resourceCostPP, resourceProduced,2, false);
        Map<Resource, Integer> resCost = new HashMap<>();
        DevelopmentCard c = new DevelopmentCard(1, 5, resCost, p, null);
        int[] faithTrackVictoryPoints = {0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,0};

        List<VaticanReport> vaticanReports = new ArrayList<>();

        vaticanReports.add(new VaticanReport(5,8,2));
        vaticanReports.add(new VaticanReport(12,15,2));
        vaticanReports.add(new VaticanReport(19,24,4));


        GameSettings gameSettings = new GameSettings(null, 0, null, null, vaticanReports, faithTrackVictoryPoints);

        Player player = new Player("rbta-svg", "RRT00");

        Dashboard dashboard = new Dashboard(gameSettings, player);
        player.setDashboard(dashboard);

        Warehouse wh = new Warehouse();
        dashboard.setWarehouse(wh);

        ArrayList<Resource> res = new ArrayList<>();
        res.add(Resource.STONE);

        wh.storeInDeposit(res);

        SpecialAbility sa = new WarehouseExtraSpace(Resource.GOLD);
        Warehouse whDoubleCheck = player.getDashboard().getWarehouse();

        // no special abilities activated means no extra deposit
        Assert.assertFalse(whDoubleCheck.hasExtraDeposit());

        sa.activate(player);

        // extra deposit flag has been activated
        Assert.assertTrue(whDoubleCheck.hasExtraDeposit());
        // only one resource should have an extra deposit for now
        Assert.assertEquals(whDoubleCheck.getExtraDepositResources().size(), 1);
        // that one resource has to be gold
        Assert.assertEquals(whDoubleCheck.getExtraDepositResources().get(0), Resource.GOLD);

        SpecialAbility sa2 = new WarehouseExtraSpace(Resource.SHIELD);

        // activating a second extra storage for shields
        sa2.activate(player);

        // resources with extra storages must be two now
        Assert.assertEquals(whDoubleCheck.getExtraDepositResources().size(), 2);
        // gold extra storage remains unchanged
        Assert.assertEquals(whDoubleCheck.getExtraDepositResources().get(0), Resource.GOLD);
        // additional shield extra storage is created
        Assert.assertEquals(whDoubleCheck.getExtraDepositResources().get(1), Resource.SHIELD);



    }

     */
}
