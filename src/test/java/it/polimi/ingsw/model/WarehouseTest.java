package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
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
                            wh.storeInDeposit(resToAdd);

                            System.out.println("STONE -- found: " + (wh.getDeposit().get(Resource.STONE) == null ? 0 : wh.getDeposit().get(Resource.STONE)) + " expected:" + i);
                            Assert.assertEquals(wh.getDeposit().get(Resource.STONE) == null ? 0 : wh.getDeposit().get(Resource.STONE), i);

                            System.out.println("SHIELD -- found: " + (wh.getDeposit().get(Resource.SHIELD) == null ? 0 : wh.getDeposit().get(Resource.SHIELD)) + " expected:" + i);
                            Assert.assertEquals(wh.getDeposit().get(Resource.SHIELD) == null ? 0 : wh.getDeposit().get(Resource.SHIELD), j);

                            System.out.println("GOLD -- found: " + (wh.getDeposit().get(Resource.GOLD) == null ? 0 : wh.getDeposit().get(Resource.GOLD)) + " expected:" + i);
                            Assert.assertEquals(wh.getDeposit().get(Resource.GOLD) == null ? 0 : wh.getDeposit().get(Resource.GOLD), k);

                            System.out.println("SERVANT -- found: " + (wh.getDeposit().get(Resource.SERVANT) == null ? 0 : wh.getDeposit().get(Resource.SERVANT)) + " expected:" + i);
                            Assert.assertEquals(wh.getDeposit().get(Resource.SERVANT) == null ? 0 : wh.getDeposit().get(Resource.SERVANT), z);

                        }

                        // test più esplicito delle add illegali?

                        resToAdd.clear();
                        wh.clearDeposit();
                    }
                }
            }
        }


    }

    @Test
    public void storeInLocker() {
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
    public void removeFromDeposit() {
        Warehouse wh = new Warehouse();
        ArrayList<Resource> resToAdd = new ArrayList<>();
        Map<Resource, Integer> resToRemove = new HashMap<>();

        IntStream.range(0, 1).forEach(l -> resToAdd.add(Resource.STONE));
        IntStream.range(0, 2).forEach(l -> resToAdd.add(Resource.SHIELD));
        IntStream.range(0, 3).forEach(l -> resToAdd.add(Resource.GOLD));

        // move is definitely legal, no check needed
        wh.storeInDeposit(resToAdd);


        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 3; k++) {
                    resToRemove.put(Resource.STONE, i);
                    resToRemove.put(Resource.SHIELD, j);
                    resToRemove.put(Resource.GOLD, k);
                }
            }
        }
    }
}







