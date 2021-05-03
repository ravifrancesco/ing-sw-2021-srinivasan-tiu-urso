package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.ProductionPower;
import it.polimi.ingsw.model.specialAbilities.WarehouseExtraSpace;
import it.polimi.ingsw.model.specialAbilities.WhiteMarbleResource;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.params.converter.ConvertWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.fail;

public class DashboardTest {

    private static Dashboard dashboard;
    private static GameSettings gameSettings;

    @BeforeClass
    static public void constructorTest() {

        gameSettings =  new GameSettingsTest().buildGameSettings();
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
        } catch (IllegalArgumentException e) {
            fail();
        } catch (IllegalStateException e) {
            Assert.assertTrue(true);
        }

        dashboard.placeDevelopmentCard(developmentCard3, 0);

        try {
            dashboard.placeDevelopmentCard(developmentCard4, 0);
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

}
