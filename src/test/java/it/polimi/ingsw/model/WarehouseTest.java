package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;



public class WarehouseTest {

    @Test
    public void storeInDepositTest() {
        Warehouse wh = new Warehouse();
        ArrayList<Resource> resToAdd = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    for (int z = 0; z < 4; z++) {
                        IntStream.range(0, i).forEach(l -> resToAdd.add(Resource.STONE));
                        IntStream.range(0, j).forEach(l -> resToAdd.add(Resource.SHIELD));
                        IntStream.range(0, k).forEach(l -> resToAdd.add(Resource.GOLD));
                        IntStream.range(0, z).forEach(l -> resToAdd.add(Resource.SERVANT));


                        if (wh.checkAddLegality(resToAdd)) {
                            wh.storeInDeposit(resToAdd);

                            Assert.assertEquals(wh.getDeposit().get(Resource.STONE) == null ? 0 : wh.getDeposit().get(Resource.STONE), i);

                            Assert.assertEquals(wh.getDeposit().get(Resource.SHIELD) == null ? 0 : wh.getDeposit().get(Resource.SHIELD), j);

                            Assert.assertEquals(wh.getDeposit().get(Resource.GOLD) == null ? 0 : wh.getDeposit().get(Resource.GOLD), k);

                            Assert.assertEquals(wh.getDeposit().get(Resource.SERVANT) == null ? 0 : wh.getDeposit().get(Resource.SERVANT), z);

                        }

                        resToAdd.clear();
                        wh.clearDeposit();
                    }
                }
            }
        }

        // more testing for the checkAddLegality
        // This add is illegal because we are trying to add 4 SERVANT, whereas the limit for a single type of resource
        // is 3.
        IntStream.range(0, 1).forEach(l -> resToAdd.add(Resource.SHIELD));
        IntStream.range(0, 2).forEach(l -> resToAdd.add(Resource.GOLD));
        IntStream.range(0, 4).forEach(l -> resToAdd.add(Resource.SERVANT));
        if(wh.checkAddLegality(resToAdd)) { wh.storeInDeposit(resToAdd); }
        // We expect that the warehouse remains unchanged (empty)
        Assert.assertEquals(wh.getDeposit().get(Resource.STONE) == null ? 0 : wh.getDeposit().get(Resource.STONE), 0);
        Assert.assertEquals(wh.getDeposit().get(Resource.SHIELD) == null ? 0 : wh.getDeposit().get(Resource.SHIELD), 0);
        Assert.assertEquals(wh.getDeposit().get(Resource.GOLD) == null ? 0 : wh.getDeposit().get(Resource.GOLD), 0);
        Assert.assertEquals(wh.getDeposit().get(Resource.SERVANT) == null ? 0 : wh.getDeposit().get(Resource.SERVANT), 0);


    }

    @Test
    public void storeInLockerTest() {
        Warehouse wh = new Warehouse();
        ArrayList<Resource> resToAdd = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    for (int z = 0; z < 4; z++) {
                        IntStream.range(0, i).forEach(l -> resToAdd.add(Resource.STONE));
                        IntStream.range(0, j).forEach(l -> resToAdd.add(Resource.SHIELD));
                        IntStream.range(0, k).forEach(l -> resToAdd.add(Resource.GOLD));
                        IntStream.range(0, z).forEach(l -> resToAdd.add(Resource.SERVANT));

                        wh.storeInLocker(resToAdd);

                        Assert.assertEquals(wh.getLocker().get(Resource.STONE) == null ? 0 : wh.getLocker().get(Resource.STONE), i);

                        Assert.assertEquals(wh.getLocker().get(Resource.SHIELD) == null ? 0 : wh.getLocker().get(Resource.SHIELD), j);

                        Assert.assertEquals(wh.getLocker().get(Resource.GOLD) == null ? 0 : wh.getLocker().get(Resource.GOLD), k);

                        Assert.assertEquals(wh.getLocker().get(Resource.SERVANT) == null ? 0 : wh.getLocker().get(Resource.SERVANT), z);

                        resToAdd.clear();
                        wh.clearLocker();
                    }
                }
            }
        }
    }


    @Test
    public void removeFromWarehouseTest() {

        Warehouse wh = new Warehouse();
        ArrayList<Resource> resToAdd = new ArrayList<>();
        Map<Resource, Integer> resToRemove = new HashMap<>();

        IntStream.range(0, 1).forEach(l -> resToAdd.add(Resource.STONE));
        IntStream.range(0, 2).forEach(l -> resToAdd.add(Resource.SHIELD));
        IntStream.range(0, 3).forEach(l -> resToAdd.add(Resource.GOLD));

        // move is definitely legal, no check needed
        wh.storeInLocker(resToAdd);
        wh.storeInDeposit(resToAdd);
        resToAdd.clear();

        /*
        Locker and warehouse are both now filled with 1 stone, 2 shields, 3 gold
         */

        // Test 1: remove 3 gold, locker remains the same and they are withdrawn from the deposit
        resToRemove.put(Resource.GOLD, 3);
        if(wh.checkRemoveLegality(resToRemove)) { wh.removeFromWarehouse(resToRemove); }
        Assert.assertEquals(wh.getDeposit().get(Resource.STONE) == null ? 0 : wh.getDeposit().get(Resource.STONE), 1);
        Assert.assertEquals(wh.getDeposit().get(Resource.SHIELD) == null ? 0 : wh.getDeposit().get(Resource.SHIELD), 2 );
        Assert.assertEquals(wh.getDeposit().get(Resource.GOLD) == null ? 0 : wh.getDeposit().get(Resource.GOLD), 0);
        Assert.assertEquals(wh.getLocker().get(Resource.STONE) == null ? 0 : wh.getLocker().get(Resource.STONE), 1);
        Assert.assertEquals(wh.getLocker().get(Resource.SHIELD) == null ? 0 : wh.getLocker().get(Resource.SHIELD), 2 );
        Assert.assertEquals(wh.getLocker().get(Resource.GOLD) == null ? 0 : wh.getLocker().get(Resource.GOLD), 3);

        resToRemove.clear();

        // Test 2: remove 2 shield, 1 stone, locker remains the same and deposit is  now completely emptied
        resToRemove.put(Resource.SHIELD, 2);
        resToRemove.put(Resource.STONE, 1);
        if(wh.checkRemoveLegality(resToRemove)) { wh.removeFromWarehouse(resToRemove); }
        Assert.assertEquals(wh.getDeposit().get(Resource.STONE) == null ? 0 : wh.getDeposit().get(Resource.STONE), 0);
        Assert.assertEquals(wh.getDeposit().get(Resource.SHIELD) == null ? 0 : wh.getDeposit().get(Resource.SHIELD), 0 );
        Assert.assertEquals(wh.getDeposit().get(Resource.GOLD) == null ? 0 : wh.getDeposit().get(Resource.GOLD), 0);
        Assert.assertEquals(wh.getLocker().get(Resource.STONE) == null ? 0 : wh.getLocker().get(Resource.STONE), 1);
        Assert.assertEquals(wh.getLocker().get(Resource.SHIELD) == null ? 0 : wh.getLocker().get(Resource.SHIELD), 2 );
        Assert.assertEquals(wh.getLocker().get(Resource.GOLD) == null ? 0 : wh.getLocker().get(Resource.GOLD), 3);
        resToRemove.clear();

        // Test 3: remove 1 shield, 1 stone, 1 gold: since deposit was emptied on test 2, they now have to be
        // withdrawn from the locker
        resToRemove.put(Resource.GOLD, 1);
        resToRemove.put(Resource.SHIELD, 1);
        resToRemove.put(Resource.STONE, 1);
        if(wh.checkRemoveLegality(resToRemove)) { wh.removeFromWarehouse(resToRemove); }
        Assert.assertEquals(wh.getDeposit().get(Resource.STONE) == null ? 0 : wh.getDeposit().get(Resource.STONE), 0);
        Assert.assertEquals(wh.getDeposit().get(Resource.SHIELD) == null ? 0 : wh.getDeposit().get(Resource.SHIELD), 0 );
        Assert.assertEquals(wh.getDeposit().get(Resource.GOLD) == null ? 0 : wh.getDeposit().get(Resource.GOLD), 0);
        Assert.assertEquals(wh.getLocker().get(Resource.STONE) == null ? 0 : wh.getLocker().get(Resource.STONE), 0);
        Assert.assertEquals(wh.getLocker().get(Resource.SHIELD) == null ? 0 : wh.getLocker().get(Resource.SHIELD), 1 );
        Assert.assertEquals(wh.getLocker().get(Resource.GOLD) == null ? 0 : wh.getLocker().get(Resource.GOLD), 2);
        resToRemove.clear();

        // Test 4: we ask for too many resources -> the checkRemoveLegality returns false and nothing changes in the warehouse.
        resToRemove.put(Resource.GOLD, 1);
        resToRemove.put(Resource.SHIELD, 1);
        resToRemove.put(Resource.STONE, 1);
        if(wh.checkRemoveLegality(resToRemove)) { wh.removeFromWarehouse(resToRemove); }
        Assert.assertEquals(wh.getDeposit().get(Resource.STONE) == null ? 0 : wh.getDeposit().get(Resource.STONE), 0);
        Assert.assertEquals(wh.getDeposit().get(Resource.SHIELD) == null ? 0 : wh.getDeposit().get(Resource.SHIELD), 0 );
        Assert.assertEquals(wh.getDeposit().get(Resource.GOLD) == null ? 0 : wh.getDeposit().get(Resource.GOLD), 0);
        Assert.assertEquals(wh.getLocker().get(Resource.STONE) == null ? 0 : wh.getLocker().get(Resource.STONE), 0);
        Assert.assertEquals(wh.getLocker().get(Resource.SHIELD) == null ? 0 : wh.getLocker().get(Resource.SHIELD), 1 );
        Assert.assertEquals(wh.getLocker().get(Resource.GOLD) == null ? 0 : wh.getLocker().get(Resource.GOLD), 2);
        resToRemove.clear();

        // Test 5: testing addition with also extra deposits
        Player p = new Player("rbta-svg", "01");
        Dashboard d = getRandDashboard(p);
        p.setDashboard(d);
        d.setWarehouse(wh);
        // current status:
        // DEPOSIT: {GOLD=0, SERVANT=0, SHIELD=0, STONE=0}
        // LOCKER: {GOLD=2, SERVANT=0, SHIELD=1, STONE=0}
        SpecialAbility sa = new WarehouseExtraSpace(Resource.GOLD);
        sa.activate(p);

        ArrayList<Resource> resToAddEWH = new ArrayList<>();
        IntStream.range(0, 3).forEach(i -> resToAddEWH.add(Resource.GOLD));
        // too much of one kind of resource for the extra deposit
        Assert.assertFalse(wh.checkExtraDepositAddLegality(resToAddEWH));
        resToAddEWH.clear();
        // resource that doesn't have an activated extra deposit
        resToAddEWH.add(Resource.SERVANT);
        Assert.assertFalse(wh.checkExtraDepositAddLegality(resToAddEWH));
        resToAddEWH.clear();
        // activating another extra storage
        SpecialAbility sa2 = new WarehouseExtraSpace(Resource.SHIELD);
        sa2.activate(p);
        Assert.assertEquals(wh.getExtraDepositResources().size(), 2);

        HashMap<Resource, Integer> resExtraWh = new HashMap<>();
        resExtraWh.put(Resource.GOLD, 3);
        resExtraWh.put(Resource.SHIELD, 2);

        Assert.assertFalse(wh.checkRemoveLegality(resExtraWh));

        resToAddEWH.add(Resource.GOLD);
        resToAddEWH.add(Resource.SHIELD);


        wh.storeInExtraDeposit(resToAddEWH);

        Assert.assertTrue(wh.checkRemoveLegality(resExtraWh));

        resToRemove.put(Resource.GOLD, 3);
        resToRemove.put(Resource.SHIELD, 2);

        wh.removeFromWarehouse(resToRemove);


        // anybody care to explain why using the expression for sumExtraWh inside the assert doesn't work?
        // ty java
        int sumExtraWh = wh.getExtraDeposit().values().stream().reduce(0, Integer::sum);
        Assert.assertEquals(sumExtraWh, 0 );
        int sumDeposit = wh.getDeposit().values().stream().reduce(0, Integer::sum);
        Assert.assertEquals(sumDeposit, 0 );
        int sumLocker = wh.getLocker().values().stream().reduce(0, Integer::sum);
        Assert.assertEquals(sumLocker, 0 );

        if(wh.isActivatedExtraDeposit()) { wh.clearExtraDeposits(); }









    }

    public Dashboard getRandDashboard(Player player) {
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

        return new Dashboard(gameSettings, player);
    }


}







