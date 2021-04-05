package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
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
                            System.out.println("ACCEPTED with " + i + j + k + z);
                            wh.storeInDeposit(resToAdd);

                             System.out.println("STONE -- found: " + (wh.getDeposit().get(Resource.STONE) == null ? 0 : wh.getDeposit().get(Resource.STONE)) + " expected:" + i);
                            Assert.assertEquals(wh.getDeposit().get(Resource.STONE) == null ? 0 : wh.getDeposit().get(Resource.STONE), i);

                             System.out.println("SHIELD -- found: " + (wh.getDeposit().get(Resource.SHIELD) == null ? 0 : wh.getDeposit().get(Resource.SHIELD)) + " expected:" + j);
                            Assert.assertEquals(wh.getDeposit().get(Resource.SHIELD) == null ? 0 : wh.getDeposit().get(Resource.SHIELD), j);

                             System.out.println("GOLD -- found: " + (wh.getDeposit().get(Resource.GOLD) == null ? 0 : wh.getDeposit().get(Resource.GOLD)) + " expected:" + k);
                            Assert.assertEquals(wh.getDeposit().get(Resource.GOLD) == null ? 0 : wh.getDeposit().get(Resource.GOLD), k);

                             System.out.println("SERVANT -- found: " + (wh.getDeposit().get(Resource.SERVANT) == null ? 0 : wh.getDeposit().get(Resource.SERVANT)) + " expected:" + z);
                            Assert.assertEquals(wh.getDeposit().get(Resource.SERVANT) == null ? 0 : wh.getDeposit().get(Resource.SERVANT), z);

                        } else {
                            System.out.println("DENIED with " + i + j + k + z);
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

                        System.out.println("STONE -- found: " + (wh.getLocker().get(Resource.STONE) == null ? 0 : wh.getLocker().get(Resource.STONE)) + " expected:" + i);
                        Assert.assertEquals(wh.getLocker().get(Resource.STONE) == null ? 0 : wh.getLocker().get(Resource.STONE), i);

                        System.out.println("SHIELD -- found: " + (wh.getLocker().get(Resource.SHIELD) == null ? 0 : wh.getLocker().get(Resource.SHIELD)) + " expected:" + i);
                        Assert.assertEquals(wh.getLocker().get(Resource.SHIELD) == null ? 0 : wh.getLocker().get(Resource.SHIELD), j);

                        System.out.println("GOLD -- found: " + (wh.getLocker().get(Resource.GOLD) == null ? 0 : wh.getLocker().get(Resource.GOLD)) + " expected:" + i);
                        Assert.assertEquals(wh.getLocker().get(Resource.GOLD) == null ? 0 : wh.getLocker().get(Resource.GOLD), k);

                        System.out.println("SERVANT -- found: " + (wh.getLocker().get(Resource.SERVANT) == null ? 0 : wh.getLocker().get(Resource.SERVANT)) + " expected:" + i);
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
        System.out.println(wh.getDeposit());
        Assert.assertEquals(wh.getDeposit().get(Resource.STONE) == null ? 0 : wh.getDeposit().get(Resource.STONE), 1);
        Assert.assertEquals(wh.getDeposit().get(Resource.SHIELD) == null ? 0 : wh.getDeposit().get(Resource.SHIELD), 2 );
        Assert.assertEquals(wh.getDeposit().get(Resource.GOLD) == null ? 0 : wh.getDeposit().get(Resource.GOLD), 0);
        System.out.println(wh.getLocker());
        Assert.assertEquals(wh.getLocker().get(Resource.STONE) == null ? 0 : wh.getLocker().get(Resource.STONE), 1);
        Assert.assertEquals(wh.getLocker().get(Resource.SHIELD) == null ? 0 : wh.getLocker().get(Resource.SHIELD), 2 );
        Assert.assertEquals(wh.getLocker().get(Resource.GOLD) == null ? 0 : wh.getLocker().get(Resource.GOLD), 3);
        resToRemove.clear();

        // Test 2: remove 2 shield, 1 stone, locker remains the same and deposit is  now completely emptied
        resToRemove.put(Resource.SHIELD, 2);
        resToRemove.put(Resource.STONE, 1);
        if(wh.checkRemoveLegality(resToRemove)) { wh.removeFromWarehouse(resToRemove); }
        System.out.println(wh.getDeposit());
        Assert.assertEquals(wh.getDeposit().get(Resource.STONE) == null ? 0 : wh.getDeposit().get(Resource.STONE), 0);
        Assert.assertEquals(wh.getDeposit().get(Resource.SHIELD) == null ? 0 : wh.getDeposit().get(Resource.SHIELD), 0 );
        Assert.assertEquals(wh.getDeposit().get(Resource.GOLD) == null ? 0 : wh.getDeposit().get(Resource.GOLD), 0);
        System.out.println(wh.getLocker());
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
        System.out.println(wh.getDeposit());
        Assert.assertEquals(wh.getDeposit().get(Resource.STONE) == null ? 0 : wh.getDeposit().get(Resource.STONE), 0);
        Assert.assertEquals(wh.getDeposit().get(Resource.SHIELD) == null ? 0 : wh.getDeposit().get(Resource.SHIELD), 0 );
        Assert.assertEquals(wh.getDeposit().get(Resource.GOLD) == null ? 0 : wh.getDeposit().get(Resource.GOLD), 0);
        System.out.println(wh.getLocker());
        Assert.assertEquals(wh.getLocker().get(Resource.STONE) == null ? 0 : wh.getLocker().get(Resource.STONE), 0);
        Assert.assertEquals(wh.getLocker().get(Resource.SHIELD) == null ? 0 : wh.getLocker().get(Resource.SHIELD), 1 );
        Assert.assertEquals(wh.getLocker().get(Resource.GOLD) == null ? 0 : wh.getLocker().get(Resource.GOLD), 2);
        resToRemove.clear();

        // Test 4: we ask for too many resources -> the checkRemoveLegality returns false and nothing changes in the warehouse.
        resToRemove.put(Resource.GOLD, 1);
        resToRemove.put(Resource.SHIELD, 1);
        resToRemove.put(Resource.STONE, 1);
        if(wh.checkRemoveLegality(resToRemove)) { wh.removeFromWarehouse(resToRemove); }
        System.out.println(wh.getDeposit());
        Assert.assertEquals(wh.getDeposit().get(Resource.STONE) == null ? 0 : wh.getDeposit().get(Resource.STONE), 0);
        Assert.assertEquals(wh.getDeposit().get(Resource.SHIELD) == null ? 0 : wh.getDeposit().get(Resource.SHIELD), 0 );
        Assert.assertEquals(wh.getDeposit().get(Resource.GOLD) == null ? 0 : wh.getDeposit().get(Resource.GOLD), 0);
        System.out.println(wh.getLocker());
        Assert.assertEquals(wh.getLocker().get(Resource.STONE) == null ? 0 : wh.getLocker().get(Resource.STONE), 0);
        Assert.assertEquals(wh.getLocker().get(Resource.SHIELD) == null ? 0 : wh.getLocker().get(Resource.SHIELD), 1 );
        Assert.assertEquals(wh.getLocker().get(Resource.GOLD) == null ? 0 : wh.getLocker().get(Resource.GOLD), 2);
        resToRemove.clear();
    }
}







