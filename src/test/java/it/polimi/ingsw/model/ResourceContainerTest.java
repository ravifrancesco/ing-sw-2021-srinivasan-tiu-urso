package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Optional;

// TODO add check to not select empty slots when adding


public class ResourceContainerTest {
    @Test
    public void constructorTest() {
        ResourceContainer rc = new ResourceContainer();
        Assert.assertEquals(rc.getContainedDepositResources().size(), 0);
        Assert.assertEquals(rc.getContainedExtraDepositResources().size(), 2);
        Assert.assertEquals(rc.getContainedLockerResources().size(), 4);
    }

    @Test
    public void everythingTest() {
        ResourceContainer rc = new ResourceContainer();
        Warehouse wh = new Warehouse();
        rc.addDepositSelectedResource(1, wh);

        int fuckYouJava;
        fuckYouJava = rc.getContainedDepositResources().get(0);
        Assert.assertEquals(fuckYouJava, 1);

        rc.addExtraDepositSelectedResource(1, 0, wh);
        fuckYouJava = rc.getContainedExtraDepositResources().get(1).get(0);
        Assert.assertEquals(fuckYouJava, 0);

        rc.addLockerSelectedResource(Resource.STONE, 5, wh);
        Assert.assertEquals(Optional.ofNullable(rc.getContainedLockerResources().get(Resource.STONE)), Optional.of(5));
        Assert.assertEquals(Optional.ofNullable(rc.getContainedLockerResources().get(Resource.SHIELD)), Optional.of(0));
        Assert.assertEquals(Optional.ofNullable(rc.getContainedLockerResources().get(Resource.GOLD)), Optional.of(0));
        Assert.assertEquals(Optional.ofNullable(rc.getContainedLockerResources().get(Resource.SERVANT)), Optional.of(0));

        wh.storeInDeposit(Resource.SERVANT, 1);
        wh.activateExtraDeposit(1);
        wh.storeInExtraDeposit(1, Resource.GOLD, 0);

        Map<Resource, Integer> resources = rc.getAllResources(wh);

        Assert.assertEquals(Optional.ofNullable(resources.get(Resource.STONE)), Optional.of(5));
        Assert.assertEquals(Optional.ofNullable(resources.get(Resource.SHIELD)), Optional.of(0));
        Assert.assertEquals(Optional.ofNullable(resources.get(Resource.GOLD)), Optional.of(1));
        Assert.assertEquals(Optional.ofNullable(resources.get(Resource.SERVANT)), Optional.of(1));

    }
}
