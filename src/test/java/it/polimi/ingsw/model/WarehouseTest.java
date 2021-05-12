package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;


public class WarehouseTest {
    @Test
    public void WarehouseCreationTest() {
        Warehouse wh = new Warehouse();
        Assert.assertEquals(wh.getLocker().size(), 4);
        Assert.assertEquals(wh.getDeposit().length, 6);
        Assert.assertEquals(wh.getExtraDeposits().length, 2);
    }

    @Test
    public void storeInDepositTest() {
        int thrownExceptions = 0;
        Warehouse wh = new Warehouse();

        // SECTION 1: error cases
        // Same resource in two different depots: shelve 1 and 2
        wh.storeInDeposit(Resource.STONE, 0);
        try {
            wh.storeInDeposit(Resource.STONE, 1);
        } catch (IllegalStateException e) {
            if(e.getMessage().equals("Deposit positioning is illegal")) { thrownExceptions += 1; }
        }
        wh.reset();


        // Same resource in two different depots: shelve 2 and 3
        wh.storeInDeposit(Resource.SERVANT, 1);
        try {
            wh.storeInDeposit(Resource.SERVANT, 3);
        } catch (IllegalStateException e) {
            if(e.getMessage().equals("Deposit positioning is illegal")) { thrownExceptions += 1; }
        }
        wh.reset();

        // Same resource in two different depots: shelve 1 and 3
        wh.storeInDeposit(Resource.GOLD, 0);
        try {
            wh.storeInDeposit(Resource.GOLD, 3);
        } catch (IllegalStateException e) {
            if(e.getMessage().equals("Deposit positioning is illegal")) { thrownExceptions += 1; }
        }
        wh.reset();



        // Different resources in same depot: shelve 2
        wh.storeInDeposit(Resource.SHIELD, 1);
        try {
            wh.storeInDeposit(Resource.GOLD, 2);
        } catch (IllegalStateException e) {
            if(e.getMessage().equals("Deposit positioning is illegal")) { thrownExceptions += 1; }
        }
        wh.reset();


        // Different resources in same depot: shelve 2
        wh.storeInDeposit(Resource.STONE, 3);
        wh.storeInDeposit(Resource.STONE, 4);

        try {
            wh.storeInDeposit(Resource.SHIELD, 5);
        } catch (IllegalStateException e) {
            thrownExceptions += 1;
        }
        wh.reset();

        Assert.assertEquals(thrownExceptions, 5);
        // SECTION 2: correct positions
        Assert.assertNull(wh.getDeposit()[0]);
        Assert.assertNull(wh.getDeposit()[1]);
        Assert.assertNull(wh.getDeposit()[2]);
        Assert.assertNull(wh.getDeposit()[3]);
        Assert.assertNull(wh.getDeposit()[4]);
        Assert.assertNull(wh.getDeposit()[5]);

        wh.storeInDeposit(Resource.SHIELD, 0);
        Assert.assertEquals(wh.getDeposit()[0], Resource.SHIELD);

        wh.storeInDeposit(Resource.STONE, 1);
        Assert.assertEquals(wh.getDeposit()[1], Resource.STONE);
        wh.storeInDeposit(Resource.STONE, 2);
        Assert.assertEquals(wh.getDeposit()[2], Resource.STONE);

        wh.storeInDeposit(Resource.GOLD, 3);
        Assert.assertEquals(wh.getDeposit()[3], Resource.GOLD);
        wh.storeInDeposit(Resource.GOLD, 4);
        Assert.assertEquals(wh.getDeposit()[4], Resource.GOLD);
        wh.storeInDeposit(Resource.GOLD, 5);
        Assert.assertEquals(wh.getDeposit()[5], Resource.GOLD);

        // SECTION 3: depositing on an already full position
        wh.reset();
        wh.storeInDeposit(Resource.GOLD, 0);
        try {
            wh.storeInDeposit(Resource.GOLD, 0);
        } catch (IllegalArgumentException e) {
            thrownExceptions += 1;
        }

        Assert.assertEquals(thrownExceptions, 6);
    }

    @Test
    public void removeFromDepositTest() {
        Warehouse wh = new Warehouse();
        // Adding and removing from every position
        wh.storeInDeposit(Resource.STONE, 0);
        wh.removeFromDeposit(0);
        Assert.assertNull(wh.getDeposit()[0]);

        wh.storeInDeposit(Resource.STONE, 1);
        wh.removeFromDeposit(1);
        Assert.assertNull(wh.getDeposit()[1]);

        wh.storeInDeposit(Resource.STONE, 2);
        wh.removeFromDeposit(2);
        Assert.assertNull(wh.getDeposit()[2]);

        wh.storeInDeposit(Resource.STONE, 3);
        wh.removeFromDeposit(3);
        Assert.assertNull(wh.getDeposit()[3]);

        wh.storeInDeposit(Resource.STONE, 4);
        wh.removeFromDeposit(4);
        Assert.assertNull(wh.getDeposit()[4]);

        wh.storeInDeposit(Resource.STONE, 5);
        wh.removeFromDeposit(5);
        Assert.assertNull(wh.getDeposit()[5]);

    }

    @Test
    public void doDepositMoveTest() {
        Warehouse wh = new Warehouse();
        // Switch two full positions
        wh.storeInDeposit(Resource.STONE, 0);
        wh.storeInDeposit(Resource.GOLD, 1);
        Assert.assertEquals(wh.getDeposit()[0], Resource.STONE);
        Assert.assertEquals(wh.getDeposit()[1], Resource.GOLD);
        wh.doDepositMove(1, 0);
        Assert.assertEquals(wh.getDeposit()[1], Resource.STONE);
        Assert.assertEquals(wh.getDeposit()[0], Resource.GOLD);
        wh.reset();
        // Switch a full with an empty position
        wh.storeInDeposit(Resource.SHIELD, 0);
        Assert.assertEquals(wh.getDeposit()[0], Resource.SHIELD);
        Assert.assertNull(wh.getDeposit()[1]);
        wh.doDepositMove(1, 0);
        Assert.assertEquals(wh.getDeposit()[1], Resource.SHIELD);
        Assert.assertNull(wh.getDeposit()[0]);
        wh.reset();
    }

    @Test
    public void getDepositResourceQtyTest() {
        Warehouse wh = new Warehouse();
        Assert.assertEquals(wh.getDepositResourceQty(), 0);
        wh.storeInDeposit(Resource.STONE, 0);
        Assert.assertEquals(wh.getDepositResourceQty(), 1);
        wh.storeInDeposit(Resource.SHIELD, 1);
        Assert.assertEquals(wh.getDepositResourceQty(), 2);
        wh.storeInDeposit(Resource.SHIELD, 2);
        Assert.assertEquals(wh.getDepositResourceQty(), 3);
        wh.storeInDeposit(Resource.GOLD, 3);
        Assert.assertEquals(wh.getDepositResourceQty(), 4);
        wh.storeInDeposit(Resource.GOLD, 4);
        Assert.assertEquals(wh.getDepositResourceQty(), 5);
        wh.storeInDeposit(Resource.GOLD, 5);
        Assert.assertEquals(wh.getDepositResourceQty(), 6);
    }

    @Test
    public void storeInLockerTest() {
        Warehouse wh = new Warehouse();
        int fuckYouJava;

        fuckYouJava = wh.getLocker().get(Resource.STONE);
        Assert.assertEquals(fuckYouJava, 0);
        // WHY CAN'T I CAST INSIDE THE ASSERTION?!!
        wh.storeInLocker(Resource.STONE, 3);
        fuckYouJava = wh.getLocker().get(Resource.STONE);
        Assert.assertEquals(fuckYouJava, 3);
    }

    @Test
    public void removeFromLockerTest() {
        Warehouse wh = new Warehouse();
        int fuckYouJava;

        wh.storeInLocker(Resource.STONE, 3);
        fuckYouJava = wh.getLocker().get(Resource.STONE);
        Assert.assertEquals(fuckYouJava, 3);
        wh.removeFromLocker(Resource.STONE, 2);
        fuckYouJava = wh.getLocker().get(Resource.STONE);
        Assert.assertEquals(fuckYouJava, 1);
    }

    @Test
    public void activateExtraDepositTest() {
        Warehouse wh = new Warehouse();
        Assert.assertNull(wh.getExtraDeposits()[0]);
        Assert.assertNull(wh.getExtraDeposits()[1]);
        wh.activateExtraDeposit(0);
        Assert.assertNotNull(wh.getExtraDeposits()[0]);
        Assert.assertNull(wh.getExtraDeposits()[1]);
        wh.activateExtraDeposit(1);
        Assert.assertNotNull(wh.getExtraDeposits()[0]);
        Assert.assertNotNull(wh.getExtraDeposits()[1]);
        wh.reset();
        Assert.assertNull(wh.getExtraDeposits()[0]);
        Assert.assertNull(wh.getExtraDeposits()[1]);
    }

    @Test
    public void storeInExtraDepositTest() {
        Warehouse wh = new Warehouse();
        wh.activateExtraDeposit(0);
        wh.storeInExtraDeposit(0, Resource.STONE, 0);
        Assert.assertNull(wh.getExtraDeposits()[1]);
        Assert.assertEquals(wh.getExtraDeposits()[0][0], Resource.STONE);
        Assert.assertNull(wh.getExtraDeposits()[0][1]);

        wh.activateExtraDeposit(1);
        wh.storeInExtraDeposit(1, Resource.GOLD, 1);
        Assert.assertEquals(wh.getExtraDeposits()[0][0], Resource.STONE);
        Assert.assertNull(wh.getExtraDeposits()[0][1]);
        Assert.assertNull(wh.getExtraDeposits()[1][0]);
        Assert.assertEquals(wh.getExtraDeposits()[1][1], Resource.GOLD);

        // Testing for mistakes
        wh.reset();
        int thrownExceptions = 0;
        wh.activateExtraDeposit(1);
        wh.storeInExtraDeposit(1, Resource.STONE, 0);
        try {
            wh.storeInExtraDeposit(1, Resource.STONE, 0);
        } catch (IllegalArgumentException e) {
            thrownExceptions += 1;
        }
        Assert.assertEquals(thrownExceptions, 1);
    }

    @Test
    public void swapExtraDepositTest() {
        Warehouse wh = new Warehouse();

        wh.activateExtraDeposit(0);
        wh.storeInExtraDeposit(0, Resource.STONE, 0);
        Assert.assertEquals(wh.getExtraDeposits()[0][0], Resource.STONE);
        Assert.assertNull(wh.getExtraDeposits()[0][1]);
        Assert.assertNull(wh.getExtraDeposits()[1]);

        wh.swapExtraDeposit(0, 0, 1);
        Assert.assertEquals(wh.getExtraDeposits()[0][1], Resource.STONE);
        Assert.assertNull(wh.getExtraDeposits()[0][0]);
        Assert.assertNull(wh.getExtraDeposits()[1]);
    }

    @Test
    public void doExtraDepositMoveTest() {
        Warehouse wh = new Warehouse();


        wh.activateExtraDeposit(0);
        wh.storeInExtraDeposit(0, Resource.STONE, 0);

        // Happy flow
        IntStream.range(0, Warehouse.MAX_DEPOSIT_SLOTS).forEach(i -> Assert.assertNull(wh.getDeposit()[i]));
        Assert.assertNull(wh.getExtraDeposits()[0][1]);
        Assert.assertEquals(wh.getExtraDeposits()[0][0], Resource.STONE);
        wh.doExtraDepositMove(0, 1, 0, 0);
        IntStream.range(0, Warehouse.MAX_DEPOSIT_SLOTS).filter(i -> i != 1).forEach(i -> Assert.assertNull(wh.getDeposit()[i]));
        Assert.assertEquals(wh.getDeposit()[1], Resource.STONE);
        Assert.assertNull(wh.getExtraDeposits()[0][1]);
        Assert.assertNull(wh.getExtraDeposits()[0][1]);

        wh.reset();
        wh.activateExtraDeposit(1);
        wh.storeInExtraDeposit(1, Resource.GOLD, 1);
        IntStream.range(0, Warehouse.MAX_DEPOSIT_SLOTS).forEach(i -> Assert.assertNull(wh.getDeposit()[i]));
        Assert.assertNull(wh.getExtraDeposits()[1][0]);
        Assert.assertEquals(wh.getExtraDeposits()[1][1], Resource.GOLD);
        wh.doExtraDepositMove(2, 1, 1, 1);
        IntStream.range(0, Warehouse.MAX_DEPOSIT_SLOTS).filter(i -> i != 2).forEach(i -> Assert.assertNull(wh.getDeposit()[i]));
        Assert.assertEquals(wh.getDeposit()[2], Resource.GOLD);
        Assert.assertNull(wh.getExtraDeposits()[1][0]);
        Assert.assertNull(wh.getExtraDeposits()[1][1]);
    }

    @Test
    public void removeFromExtraDepositTest() {
        Warehouse wh = new Warehouse();

        wh.activateExtraDeposit(0);
        wh.storeInExtraDeposit(0, Resource.STONE, 0);
        Assert.assertEquals(wh.getExtraDeposits()[0][0], Resource.STONE);
        Assert.assertNull(wh.getExtraDeposits()[0][1]);

        wh.removeFromExtraDeposit(0, 0);
        Assert.assertNull(wh.getExtraDeposits()[0][0]);
        Assert.assertNull(wh.getExtraDeposits()[0][1]);
    }

    @Test
    public void getAllResourcesTest() {
        Warehouse wh = new Warehouse();


        // case 1: no extraDeposits
        wh.storeInDeposit(Resource.STONE, 0);
        wh.storeInDeposit(Resource.GOLD, 1);
        wh.storeInLocker(Resource.SHIELD, 3);

        Map<Resource, Integer> res = wh.getAllResources();

        int fuckYouJava;
        fuckYouJava = res.get(Resource.STONE);
        Assert.assertEquals(fuckYouJava, 1);
        fuckYouJava = res.get(Resource.GOLD);
        Assert.assertEquals(fuckYouJava, 1);
        fuckYouJava = res.get(Resource.SHIELD);
        Assert.assertEquals(fuckYouJava, 3);
        fuckYouJava = res.get(Resource.SERVANT);
        Assert.assertEquals(fuckYouJava, 0);

        // case 2: adding one extra warehouse

        wh.activateExtraDeposit(0);
        wh.storeInExtraDeposit(0, Resource.SERVANT, 0);
        wh.storeInExtraDeposit(0, Resource.SERVANT, 1);
        res = wh.getAllResources();
        fuckYouJava = res.get(Resource.STONE);
        Assert.assertEquals(fuckYouJava, 1);
        fuckYouJava = res.get(Resource.GOLD);
        Assert.assertEquals(fuckYouJava, 1);
        fuckYouJava = res.get(Resource.SHIELD);
        Assert.assertEquals(fuckYouJava, 3);
        fuckYouJava = res.get(Resource.SERVANT);
        Assert.assertEquals(fuckYouJava, 2);

        // case 3: adding another extra warehouse for a total of two
        wh.activateExtraDeposit(1);
        wh.storeInExtraDeposit(1, Resource.GOLD, 0);
        res = wh.getAllResources();
        fuckYouJava = res.get(Resource.STONE);
        Assert.assertEquals(fuckYouJava, 1);
        fuckYouJava = res.get(Resource.GOLD);
        Assert.assertEquals(fuckYouJava, 2);
        fuckYouJava = res.get(Resource.SHIELD);
        Assert.assertEquals(fuckYouJava, 3);
        fuckYouJava = res.get(Resource.SERVANT);
        Assert.assertEquals(fuckYouJava, 2);


    }

    @Test
    public void changeDeposit() {
        Warehouse wh = new Warehouse();
        Resource[] newDeposit = new Resource[6];

        wh.storeInDeposit(Resource.STONE, 1);
        wh.storeInDeposit(Resource.STONE, 2);

        newDeposit[3] = Resource.STONE;
        newDeposit[4] = Resource.STONE;

        System.out.println(Arrays.toString(wh.getDeposit()));
        System.out.println(Arrays.toString(newDeposit));

        Assert.assertEquals(wh.getDeposit()[1], Resource.STONE);
        Assert.assertEquals(wh.getDeposit()[2], Resource.STONE);
        wh.changeDeposit(newDeposit);
        Assert.assertNull(wh.getDeposit()[1]);
        Assert.assertNull(wh.getDeposit()[2]);
        Assert.assertEquals(wh.getDeposit()[3], Resource.STONE);
        Assert.assertEquals(wh.getDeposit()[4], Resource.STONE);
    }

    @Test
    public void changeDepositExtraDepositTest() {
        Warehouse wh = new Warehouse();
        wh.storeInDeposit(Resource.STONE, 3);
        wh.storeInDeposit(Resource.STONE, 4);
        wh.activateExtraDeposit(0);
        wh.storeInExtraDeposit(0, Resource.STONE, 0);

        Resource[] newExtraDeposit = new Resource[2];
        int leaderCardIndex = 0;


        Assert.assertEquals(wh.getExtraDeposits()[0][0], Resource.STONE);
        Assert.assertNull(wh.getExtraDeposits()[0][1]);
        wh.changeExtraDeposit(newExtraDeposit, leaderCardIndex);
        Assert.assertNull(wh.getExtraDeposits()[0][0]);
        Assert.assertNull(wh.getExtraDeposits()[0][1]);









    }


}







