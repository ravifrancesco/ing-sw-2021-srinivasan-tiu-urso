package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.ProductionPower;
import it.polimi.ingsw.model.specialAbilities.SpecialAbility;
import it.polimi.ingsw.model.specialAbilities.WarehouseExtraSpace;
import it.polimi.ingsw.model.specialAbilities.WhiteMarbleResource;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

public class DashboardTest {

    private static Dashboard dashboard;

    @BeforeClass
    static public void createDashboard() {

        GameSettings gameSettings = new GameSettingsTest().buildGameSettings();
        dashboard = new Dashboard(gameSettings);

        Assert.assertNotNull(dashboard);

    }

    @Test
    public void moveFaithMarkerTest() {

        dashboard.reset();

        int faithMarkerPosition = dashboard.getFaithMarkerPosition();

        dashboard.moveFaithMarker(4);

        Assert.assertEquals(4, dashboard.getFaithMarkerPosition());

        dashboard.moveFaithMarker(40);

        Assert.assertEquals(24, dashboard.getFaithMarkerPosition());

    }

    @Test
    public void placeLeaderCardTest() {

        dashboard.reset();

        LeaderCard leaderCard1 = new LeaderCard(0, 1, new HashMap<>(), new HashMap<>(), null);
        LeaderCard leaderCard2 = new LeaderCard(1, 2, new HashMap<>(), new HashMap<>(), null);
        LeaderCard leaderCard3 = new LeaderCard(2, 3, new HashMap<>(), new HashMap<>(), null);

        dashboard.placeLeaderCard(leaderCard1);
        dashboard.placeLeaderCard(leaderCard2);
        try {
            dashboard.placeLeaderCard(leaderCard3);
            fail();
        } catch (IllegalStateException e) {
            Assert.assertTrue(true);
        }

    }

    @Test
    public void placeDevelopmentCard() {

        dashboard.reset();

        DevelopmentCard developmentCard1 = new DevelopmentCard(0, 1, new HashMap<>(), null, new Banner(BannerEnum.BLUE, 1));
        DevelopmentCard developmentCard2 = new DevelopmentCard(1, 2, new HashMap<>(), null, new Banner(BannerEnum.GREEN, 1));
        DevelopmentCard developmentCard3 = new DevelopmentCard(2, 3, new HashMap<>(), null, new Banner(BannerEnum.BLUE, 2));
        DevelopmentCard developmentCard4 = new DevelopmentCard(3, 4, new HashMap<>(), null, new Banner(BannerEnum.BLUE, 1));

        try {
            dashboard.placeDevelopmentCard(developmentCard1, -1);
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        } catch (IllegalStateException e) {
            fail();
        }

        dashboard.placeDevelopmentCard(developmentCard1, 0);

        try {
            dashboard.placeDevelopmentCard(developmentCard2, 0);
            fail();
        } catch (IllegalArgumentException e) {
            fail();
        } catch (IllegalStateException e) {
            Assert.assertTrue(true);
        }

        dashboard.placeDevelopmentCard(developmentCard2, 1);

        try {
            dashboard.placeDevelopmentCard(developmentCard3, 2);
            fail();
        } catch (IllegalArgumentException e) {
            fail();
        } catch (IllegalStateException e) {
            Assert.assertTrue(true);
        }

        dashboard.placeDevelopmentCard(developmentCard3, 0);

        try {
            dashboard.placeDevelopmentCard(developmentCard4, 0);
            fail();
        } catch (IllegalArgumentException e) {
            fail();
        } catch (IllegalStateException e) {
            Assert.assertTrue(true);
        }

        dashboard.placeDevelopmentCard(developmentCard4, 2);


    }

    @Test
    public void computePlayerPointsTest() {

        dashboard.reset();

        dashboard.moveFaithMarker(4);

        LeaderCard leaderCard1 = new LeaderCard(0, 1, new HashMap<>(), new HashMap<>(), new WarehouseExtraSpace(Resource.GOLD));
        LeaderCard leaderCard2 = new LeaderCard(1, 2, new HashMap<>(), new HashMap<>(), null);

        dashboard.placeLeaderCard(leaderCard1);
        dashboard.activateExtraDeposit(0);
        dashboard.placeLeaderCard(leaderCard2);

        DevelopmentCard developmentCard1 = new DevelopmentCard(0, 1, new HashMap<>(), null, new Banner(BannerEnum.BLUE, 1));
        DevelopmentCard developmentCard2 = new DevelopmentCard(1, 2, new HashMap<>(), null, new Banner(BannerEnum.GREEN, 1));
        DevelopmentCard developmentCard3 = new DevelopmentCard(2, 3, new HashMap<>(), null, new Banner(BannerEnum.BLUE, 2));
        DevelopmentCard developmentCard4 = new DevelopmentCard(3, 4, new HashMap<>(), null, new Banner(BannerEnum.BLUE, 1));

        dashboard.placeDevelopmentCard(developmentCard1, 0);
        dashboard.placeDevelopmentCard(developmentCard2, 1);
        dashboard.placeDevelopmentCard(developmentCard3, 0);
        dashboard.placeDevelopmentCard(developmentCard4, 2);

        ArrayList<Resource> res = new ArrayList<>();
        res.add(Resource.GOLD);
        dashboard.addResourcesToSupply(res);
        dashboard.storeFromSupplyInExtraDeposit(0, 0, 1);
        dashboard.storeResourceInLocker(Resource.SERVANT, 4);
        dashboard.storeResourceInDeposit(Resource.GOLD, 0);

        Assert.assertEquals(15, dashboard.computePlayerPoints());

    }

    @Test
    public void storeResourceInDepositTest() {

        dashboard.reset();

        try {
            dashboard.storeResourceInDeposit(Resource.STONE, 7);
            fail();
        } catch (IllegalStateException e) {
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }

        dashboard.storeResourceInDeposit(Resource.STONE, 2);

        try {
            dashboard.storeResourceInDeposit(Resource.STONE, 2);
            fail();
        } catch (IllegalStateException e) {
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }

        try {
            dashboard.storeResourceInDeposit(Resource.GOLD, 1);
            fail();
        } catch (IllegalArgumentException e) {
            fail();
        } catch (IllegalStateException e) {
            Assert.assertTrue(true);
        }

    }

    @Test
    public void getBannersTest() {

        dashboard.reset();

        ArrayList<Banner> bannersList = new ArrayList<>();
        bannersList.add(new Banner(BannerEnum.BLUE, 1));
        bannersList.add(new Banner(BannerEnum.GREEN, 1));
        bannersList.add(new Banner(BannerEnum.BLUE, 2));
        bannersList.add(new Banner(BannerEnum.BLUE, 1));

        DevelopmentCard developmentCard1 = new DevelopmentCard(0, 1, new HashMap<>(), null, bannersList.get(0));
        DevelopmentCard developmentCard2 = new DevelopmentCard(1, 2, new HashMap<>(), null, bannersList.get(1));
        DevelopmentCard developmentCard3 = new DevelopmentCard(2, 3, new HashMap<>(), null, bannersList.get(2));
        DevelopmentCard developmentCard4 = new DevelopmentCard(3, 4, new HashMap<>(), null, bannersList.get(3));

        dashboard.placeDevelopmentCard(developmentCard1, 0);
        dashboard.placeDevelopmentCard(developmentCard2, 1);
        dashboard.placeDevelopmentCard(developmentCard3, 0);
        dashboard.placeDevelopmentCard(developmentCard4, 2);

        Map<Banner, Integer> banners = dashboard.getBanners();

        Assert.assertEquals(2, (int) banners.get(bannersList.get(0)));
        Assert.assertEquals(1, (int) banners.get(bannersList.get(1)));
        Assert.assertEquals(1, (int) banners.get(bannersList.get(2)));

    }

    @Test
    public void getLeaderCardTest() {

        dashboard.reset();

        LeaderCard leaderCard1 = new LeaderCard(0, 1, new HashMap<>(), new HashMap<>(), null);
        dashboard.placeLeaderCard(leaderCard1);

        try {
            dashboard.getLeaderCard(3);
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }

        Assert.assertEquals(leaderCard1, dashboard.getLeaderCard(0));

    }

    @Test
    public void getDevelopmentCardTest() {

        dashboard.reset();

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        DevelopmentCard developmentCard1 = new DevelopmentCard(0, 1, new HashMap<>(), p, new Banner(BannerEnum.BLUE, 1));
        dashboard.placeDevelopmentCard(developmentCard1, 0);

        Assert.assertNull(dashboard.getDevelopmentCard(1));
        Assert.assertEquals(developmentCard1, dashboard.getDevelopmentCard(0));

    }

    @Test
    public void discardResourcesTest() {

        dashboard.reset();

        Assert.assertEquals(0, dashboard.discardResources());

        ArrayList<Resource> res = new ArrayList<>();
        res.add(Resource.GOLD);
        dashboard.addResourcesToSupply(res);

        Assert.assertEquals(1, dashboard.discardResources());

    }

    @Test
    public void resetProductionPowerTest() {

        dashboard.reset();

        Map<Resource, Integer> resourceRequired = new HashMap<>();

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        ProductionPower p1 = new ProductionPower(resourceRequired, resourceProduced,2);
        ProductionPower p2 = new ProductionPower(resourceRequired, resourceProduced,2);

        DevelopmentCard developmentCard1 = new DevelopmentCard(0, 1, new HashMap<>(), p1, new Banner(BannerEnum.BLUE, 1));

        dashboard.placeDevelopmentCard(developmentCard1, 0);

        LeaderCard leaderCard1 = new LeaderCard(0, 2, new HashMap<>(), new HashMap<>(), p2);
        LeaderCard leaderCard2 = new LeaderCard(1, 2, new HashMap<>(), new HashMap<>(), new WhiteMarbleResource(Resource.SHIELD));
        dashboard.placeLeaderCard(leaderCard1);
        dashboard.placeLeaderCard(leaderCard2);

        dashboard.resetProductionPowers();

    }

    @Test
    public void moveDepositExtraDepositTest() {

        dashboard.reset();

        LeaderCard leaderCard1 = new LeaderCard(1, 2, new HashMap<>(), new HashMap<>(), new WarehouseExtraSpace(Resource.STONE));
        dashboard.placeLeaderCard(leaderCard1);

        dashboard.activateExtraDeposit(0);
        dashboard.getWarehouse().getExtraDeposits()[0][0] = Resource.STONE;
        dashboard.storeResourceInDeposit(Resource.STONE, 3);
        dashboard.storeResourceInDeposit(Resource.STONE, 4);

        Assert.assertEquals(dashboard.getWarehouse().getExtraDeposits()[0][0], Resource.STONE);
        Assert.assertNull(dashboard.getWarehouse().getExtraDeposits()[0][1]);
        Assert.assertEquals(dashboard.getWarehouse().getDeposit()[3], Resource.STONE);
        Assert.assertEquals(dashboard.getWarehouse().getDeposit()[4], Resource.STONE);

        Resource[] newDeposit = new Resource[6];
        Resource[] newExtraDeposit = new Resource[2];
        int leaderCardIndex = 0;
        newDeposit[3] = Resource.STONE;
        newDeposit[4] = Resource.STONE;
        newDeposit[5] = Resource.STONE;

        dashboard.moveDepositExtraDeposit(newDeposit, newExtraDeposit, leaderCardIndex);

        Assert.assertNull(dashboard.getWarehouse().getExtraDeposits()[0][0]);
        Assert.assertNull(dashboard.getWarehouse().getExtraDeposits()[0][1]);
        Assert.assertEquals(dashboard.getWarehouse().getDeposit()[3], Resource.STONE);
        Assert.assertEquals(dashboard.getWarehouse().getDeposit()[4], Resource.STONE);
        Assert.assertEquals(dashboard.getWarehouse().getDeposit()[5], Resource.STONE);

        newExtraDeposit[0] = Resource.STONE;

        int thrownExceptions = 0;
        try {
            dashboard.moveDepositExtraDeposit(newDeposit, newExtraDeposit, leaderCardIndex);
        } catch (Exception e) {
            thrownExceptions += 1;
        }
        Assert.assertEquals(thrownExceptions, 1);

        newExtraDeposit[0] = null;
        try {
            dashboard.moveDepositExtraDeposit(newDeposit, newExtraDeposit, 5);
        } catch (Exception e) {
            thrownExceptions += 1;
        }
        Assert.assertEquals(thrownExceptions, 2);




    }

    @Test
    public void moveDepositTest() {

        dashboard.reset();

        dashboard.storeResourceInDeposit(Resource.STONE, 1);
        dashboard.storeResourceInDeposit(Resource.STONE, 2);

        Resource[] newDeposit = new Resource[6];
        newDeposit[3] = Resource.STONE;
        newDeposit[4] = Resource.STONE;

        Assert.assertEquals(dashboard.getWarehouse().getDeposit()[1], Resource.STONE);
        Assert.assertEquals(dashboard.getWarehouse().getDeposit()[2], Resource.STONE);

        System.out.println(Arrays.toString(dashboard.getWarehouse().getDeposit()));
        dashboard.moveDepositResources(newDeposit);
        System.out.println(Arrays.toString(dashboard.getWarehouse().getDeposit()));

        Assert.assertEquals(dashboard.getWarehouse().getDeposit()[3], Resource.STONE);
        Assert.assertEquals(dashboard.getWarehouse().getDeposit()[3], Resource.STONE);
        Assert.assertEquals(dashboard.getWarehouse().getDeposit()[3], Resource.STONE);
        Assert.assertEquals(dashboard.getWarehouse().getDeposit()[4], Resource.STONE);

        newDeposit[5] = Resource.STONE;

        int thrownExceptions = 0;
        Assert.assertEquals(thrownExceptions, 0);

        try {
            dashboard.moveDepositResources(newDeposit);
        } catch (Exception e) {
            thrownExceptions += 1;
        }
        Assert.assertEquals(thrownExceptions, 1);

        Resource[] newDeposit2 = new Resource[7];
        try {
            dashboard.moveDepositResources(newDeposit2);
        } catch (Exception e) {
            thrownExceptions += 1;
        }
        Assert.assertEquals(thrownExceptions, 2);
    }

    @Test
    public void storeFromSupply() {

        dashboard.reset();

        ArrayList<Resource> res = new ArrayList<>();
        res.add(Resource.GOLD);
        dashboard.addResourcesToSupply(res);

        dashboard.storeResourceInDeposit(Resource.GOLD, 5);

        try {
            dashboard.storeFromSupply(5, 2);
            fail();
        } catch (IllegalStateException e) {
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }

        try {
            dashboard.storeFromSupply(1, 4);
            fail();
        } catch (IllegalStateException e) {
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }

        try {
            dashboard.storeFromSupply(0, 1);
            fail();
        } catch (IllegalArgumentException e) {
            fail();
        } catch (IllegalStateException e) {
            Assert.assertTrue(true);
        }

        dashboard.storeFromSupply(0,4);

    }

    @Test
    public void storeFromSupplyInExtraDepositTest() {

        dashboard.reset();

        dashboard.reset();

        LeaderCard leaderCard1 = new LeaderCard(1, 2, new HashMap<>(), new HashMap<>(), new WarehouseExtraSpace(Resource.SHIELD));
        LeaderCard leaderCard2 = new LeaderCard(1, 2, new HashMap<>(), new HashMap<>(), new WhiteMarbleResource(Resource.SHIELD));
        dashboard.placeLeaderCard(leaderCard1);
        dashboard.placeLeaderCard(leaderCard2);
        dashboard.activateExtraDeposit(0);

        ArrayList<Resource> res = new ArrayList<>();
        res.add(Resource.SHIELD);
        res.add(Resource.SHIELD);
        res.add(Resource.GOLD);
        dashboard.addResourcesToSupply(res);

        try {
            dashboard.storeFromSupplyInExtraDeposit(3, 5, 2);
            fail();
        } catch (IllegalStateException e) {
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }

        try {
            dashboard.storeFromSupplyInExtraDeposit(1, 0, 0);
            fail();
        } catch (IllegalStateException e) {
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }

        try {
            dashboard.storeFromSupplyInExtraDeposit(0, 3, 0);
            fail();
        } catch (IllegalStateException e) {
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }

        try {
            dashboard.storeFromSupplyInExtraDeposit(0,2, 1);
            fail();
        } catch (IllegalArgumentException e) {
            fail();
        } catch (IllegalStateException e) {
            Assert.assertTrue(true);
        }

        dashboard.storeFromSupplyInExtraDeposit(0,1, 0);

        try {
            dashboard.storeFromSupplyInExtraDeposit(0,0, 0);
            fail();
        } catch (IllegalStateException e) {
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }

    }

    @Test
    public void checkGameEndTest() {

        dashboard.reset();

        Assert.assertFalse(dashboard.checkGameEnd());

        dashboard.moveFaithMarker(54);

        Assert.assertTrue(dashboard.checkGameEnd());

        dashboard.reset();

        DevelopmentCard developmentCard1  = new DevelopmentCard(0,1, new HashMap<>(),null, new Banner(BannerEnum.BLUE, 1));
        DevelopmentCard developmentCard2 = new DevelopmentCard(0,1, new HashMap<>(),null, new Banner(BannerEnum.BLUE, 2));
        DevelopmentCard developmentCard3 = new DevelopmentCard(0,1, new HashMap<>(),null, new Banner(BannerEnum.BLUE, 3));

        dashboard.placeDevelopmentCard(developmentCard1, 0);
        dashboard.placeDevelopmentCard(developmentCard2, 0);
        dashboard.placeDevelopmentCard(developmentCard3, 0);

        DevelopmentCard developmentCard4  = new DevelopmentCard(0,1, new HashMap<>(),null, new Banner(BannerEnum.GREEN, 1));
        DevelopmentCard developmentCard5 = new DevelopmentCard(0,1, new HashMap<>(),null, new Banner(BannerEnum.GREEN, 2));
        DevelopmentCard developmentCard6 = new DevelopmentCard(0,1, new HashMap<>(),null, new Banner(BannerEnum.GREEN, 3));

        dashboard.placeDevelopmentCard(developmentCard4, 1);
        dashboard.placeDevelopmentCard(developmentCard5, 1);
        dashboard.placeDevelopmentCard(developmentCard6, 1);

        Assert.assertFalse(dashboard.checkGameEnd());

        DevelopmentCard developmentCard7  = new DevelopmentCard(0,1,new HashMap<>(),null,new Banner(BannerEnum.YELLOW, 1));

        dashboard.placeDevelopmentCard(developmentCard7, 2);

        Assert.assertTrue(dashboard.checkGameEnd());

    }

    @Test
    public void payPriceTest() {
        // TODO change when we have the view
        dashboard.reset();

        dashboard.storeResourceInLocker(Resource.GOLD, 3);
        dashboard.storeResourceInDeposit(Resource.STONE, 1);
        dashboard.storeResourceInDeposit(Resource.STONE, 2);

        ResourceContainer rc = new ResourceContainer();
        rc.addDepositSelectedResource(1, new Warehouse());
        rc.addDepositSelectedResource(2, new Warehouse());
        rc.addLockerSelectedResource(Resource.GOLD, 3, new Warehouse());

        HashMap<Resource, Integer> cost = new HashMap<>();
        cost.put(Resource.GOLD, 3);
        cost.put(Resource.STONE, 2);

        Map<Resource, Integer> allResources = dashboard.getAllPlayerResources();
        Assert.assertEquals(java.util.Optional.ofNullable(allResources.get(Resource.GOLD)), Optional.of(3));
        Assert.assertEquals(java.util.Optional.ofNullable(allResources.get(Resource.STONE)), Optional.of(2));
        Assert.assertEquals(java.util.Optional.ofNullable(allResources.get(Resource.SHIELD)), Optional.of(0));
        Assert.assertEquals(java.util.Optional.ofNullable(allResources.get(Resource.SERVANT)), Optional.of(0));

        // HAPPY FLOW
        dashboard.payPrice(rc, cost);

        allResources = dashboard.getAllPlayerResources();

        Assert.assertEquals(java.util.Optional.ofNullable(allResources.get(Resource.GOLD)), Optional.of(0));
        Assert.assertEquals(java.util.Optional.ofNullable(allResources.get(Resource.STONE)), Optional.of(0));
        Assert.assertEquals(java.util.Optional.ofNullable(allResources.get(Resource.SHIELD)), Optional.of(0));
        Assert.assertEquals(java.util.Optional.ofNullable(allResources.get(Resource.SERVANT)), Optional.of(0));

        int thrownExceptions = 0;


        ResourceContainer rc2 = new ResourceContainer();
        rc2.addDepositSelectedResource(1, new Warehouse());
        rc2.addDepositSelectedResource(2, new Warehouse());
        rc2.addLockerSelectedResource(Resource.GOLD, 3, new Warehouse());


        cost = new HashMap<>();
        cost.put(Resource.GOLD, 3);
        cost.put(Resource.STONE, 3);
        cost.put(Resource.SHIELD, 0);
        cost.put(Resource.SERVANT, 0);


        try {
            dashboard.payPrice(rc2, cost);
        } catch (IllegalArgumentException e) {
            thrownExceptions += 1;
        }

        Assert.assertEquals(thrownExceptions, 1);


        dashboard.reset();

        dashboard.storeResourceInDeposit(Resource.SERVANT, 3);
        dashboard.storeResourceInDeposit(Resource.SERVANT, 4);
        dashboard.storeResourceInDeposit(Resource.SERVANT, 5);

        ArrayList<Resource> res = new ArrayList<>();
        res.add(Resource.GOLD);
        res.add(Resource.GOLD);
        dashboard.addResourcesToSupply(res);

        SpecialAbility sa = new WarehouseExtraSpace(Resource.GOLD);
        LeaderCard lc = new LeaderCard(0, 10, new HashMap<>(), new HashMap<>(), sa);
        dashboard.activateExtraDeposit(0);


        dashboard.placeLeaderCard(lc);

        dashboard.storeFromSupplyInExtraDeposit(0, 0, 0);
        dashboard.storeFromSupplyInExtraDeposit(0, 0, 1);


        System.out.println(dashboard.getAllPlayerResources());


        Map<Resource, Integer> cost2 = new HashMap<>();

        cost2.put(Resource.GOLD, 2);
        cost2.put(Resource.SERVANT, 3);

        ResourceContainer rcc = new ResourceContainer();
        rcc.addDepositSelectedResource(3, new Warehouse());
        rcc.addDepositSelectedResource(4, new Warehouse());
        rcc.addDepositSelectedResource(5, new Warehouse());
        rcc.addExtraDepositSelectedResource(0, 0, new Warehouse());
        rcc.addExtraDepositSelectedResource(0, 1, new Warehouse());

        dashboard.payPrice(rcc, cost2);

        allResources = dashboard.getAllPlayerResources();

        Assert.assertEquals(java.util.Optional.ofNullable(allResources.get(Resource.GOLD)), Optional.of(0));
        Assert.assertEquals(java.util.Optional.ofNullable(allResources.get(Resource.STONE)), Optional.of(0));
        Assert.assertEquals(java.util.Optional.ofNullable(allResources.get(Resource.SHIELD)), Optional.of(0));
        Assert.assertEquals(java.util.Optional.ofNullable(allResources.get(Resource.SERVANT)), Optional.of(0));









    }

    @Test
    public void getDepositResourceQtyTest() {
        dashboard.reset();
        dashboard.storeResourceInDeposit(Resource.STONE, 0);
        dashboard.storeResourceInDeposit(Resource.GOLD, 1);
        dashboard.storeResourceInDeposit(Resource.GOLD, 2);

        Assert.assertEquals(dashboard.getDepositResourceQty(), 3);
    }

}
