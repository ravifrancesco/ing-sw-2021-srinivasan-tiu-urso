package it.polimi.ingsw.model;

import it.polimi.ingsw.DefaultSettingsBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Optional;

// TODO add check to not select empty slots when adding

public class ResourceContainerTest {
    @Test
    public void constructorTest() {
        ResourceContainer rc = new ResourceContainer();
        Assert.assertEquals(rc.getSelectedDepositIndexes().size(), 0);
        Assert.assertEquals(rc.getSelectedExtraDepositIndexes().size(), 2);
        Assert.assertEquals(rc.getSelectedLockerResources().size(), 4);
    }

    @Test
    public void everythingTest() {
        DefaultSettingsBuilder defaultSettingsBuilder = new DefaultSettingsBuilder();
        GameSettings gameSettings = defaultSettingsBuilder.getGameSettings();

        Player player = new Player(gameSettings, "test");

        ResourceContainer rc = new ResourceContainer();
        Warehouse wh = new Warehouse(player.getDashboard());
        wh.storeInDeposit(Resource.GOLD, 1);
        rc.addDepositSelectedResource(1, wh);

        int fuckYouJava;
        fuckYouJava = rc.getSelectedDepositIndexes().get(0);
        Assert.assertEquals(fuckYouJava, 1);
        wh.activateExtraDeposit(1);
        wh.storeInExtraDeposit(1, Resource.GOLD, 0);
        rc.addExtraDepositSelectedResource(1, 0, wh);
        fuckYouJava = rc.getSelectedExtraDepositIndexes().get(1).get(0);
        Assert.assertEquals(fuckYouJava, 0);
        wh.storeInLocker(Resource.STONE, 6);
        rc.addLockerSelectedResource(Resource.STONE, 5, wh);
        Assert.assertEquals(Optional.ofNullable(rc.getSelectedLockerResources().get(Resource.STONE)), Optional.of(5));
        Assert.assertEquals(Optional.ofNullable(rc.getSelectedLockerResources().get(Resource.SHIELD)), Optional.of(0));
        Assert.assertEquals(Optional.ofNullable(rc.getSelectedLockerResources().get(Resource.GOLD)), Optional.of(0));
        Assert.assertEquals(Optional.ofNullable(rc.getSelectedLockerResources().get(Resource.SERVANT)), Optional.of(0));


        wh.storeInDeposit(Resource.SERVANT, 5);
        rc.addDepositSelectedResource(5, wh);
        wh.activateExtraDeposit(1);
        wh.storeInExtraDeposit(1, Resource.GOLD, 0);

        Map<Resource, Integer> resources = rc.getAllResources(wh);
        Assert.assertEquals(Optional.ofNullable(resources.get(Resource.STONE)), Optional.of(5));
        Assert.assertEquals(Optional.ofNullable(resources.get(Resource.SHIELD)), Optional.of(0));
        Assert.assertEquals(Optional.ofNullable(resources.get(Resource.GOLD)), Optional.of(2));
        Assert.assertEquals(Optional.ofNullable(resources.get(Resource.SERVANT)), Optional.of(1));

        int thrownExceptions = 0;

        Warehouse newWarehouse = new Warehouse(player.getDashboard());
        ResourceContainer r_c = new ResourceContainer();
        newWarehouse.storeInDeposit(Resource.STONE, 1);
        newWarehouse.storeInDeposit(Resource.STONE, 2);
        r_c.addDepositSelectedResource(1, newWarehouse);
        try {
            r_c.addDepositSelectedResource(3, newWarehouse);
        } catch (Exception e) {
            thrownExceptions = 1;
        }
        Assert.assertEquals(thrownExceptions, 1);

        newWarehouse.activateExtraDeposit(0);
        try {
            r_c.addExtraDepositSelectedResource(0, 0, newWarehouse);
        } catch (Exception e) {
            thrownExceptions = 2;
        }

        newWarehouse.storeInLocker(Resource.GOLD, 6);
        try {
            r_c.addLockerSelectedResource(Resource.GOLD, 7, newWarehouse);
        } catch (Exception e) {
            thrownExceptions = 3;
        }
        Assert.assertEquals(thrownExceptions, 3);

    }
}
