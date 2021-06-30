package it.polimi.ingsw.model.full.cards;

import it.polimi.ingsw.model.full.specialAbilities.*;
import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.full.table.VaticanReport;
import it.polimi.ingsw.model.utils.GameSettings;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class LeaderCardTest {

    @Test
    public void constructorTest() {
        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.GOLD, 3);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, resourceProduced, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, resourceCost, sa);

        Assert.assertEquals(leaderCard.getId(), 1);
        Assert.assertEquals(leaderCard.getVictoryPoints(), 5);
        Assert.assertEquals(leaderCard.getBannerCost(), bannerCost);
        Assert.assertEquals(leaderCard.getSpecialAbility(), sa);
    }

    @Test
    public void toStringTestWithNullRP() {

        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.GOLD, 3);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.ANY, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, new HashMap<>(), 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, resourceCost, sa);

        String s1 = "ID=1;VP=5;BC=BLUE:2:1,GREEN:1:2;RC=GOLD:3;SA=PP;RR=ANY:1;RP=;FP=1;";
        String s2 = "ID=1;VP=5;BC=GREEN:1:2,BLUE:2:1;RC=GOLD:3;SA=PP;RR=ANY:1;RP=;FP=1;";

        Assert.assertTrue(s1.equals(leaderCard.toString()) || s2.equals(leaderCard.toString()));

    }

    @Test
    public void equalsTrueTest() {
        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.GOLD, 3);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, resourceProduced, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, resourceCost, sa);
        LeaderCard leaderCard2 = new LeaderCard(1, 5, bannerCost, resourceCost, sa);

        Assert.assertEquals(leaderCard, leaderCard2);
    }

    @Test
    public void equalsFalseTest() {
        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, resourceProduced, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, new HashMap<>(), sa);

        Map<Banner, Integer> bannerCost2 = new HashMap<>();
        bannerCost2.put(new Banner(BannerEnum.YELLOW, 1), 3);

        LeaderCard leaderCard2 = new LeaderCard(2, 3, bannerCost2, new HashMap<>(), sa);

        Assert.assertNotEquals(leaderCard, leaderCard2);
    }

    @Test
    public void trueIsPlayableTest() {
        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.GOLD, 3);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, resourceProduced, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, resourceCost, sa);

        Map<Banner, Integer> playerBanners = new HashMap<>();

        playerBanners.put(new Banner(BannerEnum.GREEN, 1), 3);
        playerBanners.put(new Banner(BannerEnum.YELLOW, 3), 1);
        playerBanners.put(new Banner(BannerEnum.BLUE, 2), 1);
        playerBanners.put(new Banner(BannerEnum.PURPLE, 2), 1);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 5);
        playerResources.put(Resource.SHIELD, 3);

        Assert.assertTrue(leaderCard.isPlayable(playerBanners, playerResources));
    }

    @Test
    public void falseIsPlayableTest1() {
        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.GOLD, 3);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, resourceProduced, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, resourceCost, sa);

        Map<Banner, Integer> playerBanners = new HashMap<>();

        playerBanners.put(new Banner(BannerEnum.GREEN, 1), 2);
        playerBanners.put(new Banner(BannerEnum.BLUE, 1), 1);
        playerBanners.put(new Banner(BannerEnum.YELLOW, 2), 1);
        playerBanners.put(new Banner(BannerEnum.BLUE, 3), 1);
        playerBanners.put(new Banner(BannerEnum.PURPLE, 2), 1);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 5);
        playerResources.put(Resource.SHIELD, 3);

        Assert.assertFalse(leaderCard.isPlayable(playerBanners, playerResources));
    }

    @Test
    public void falseIsPlayableTest2() {
        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.GOLD, 3);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, resourceProduced, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, resourceCost, sa);

        Map<Banner, Integer> playerBanners = new HashMap<>();

        playerBanners.put(new Banner(BannerEnum.YELLOW, 1), 1);
        playerBanners.put(new Banner(BannerEnum.PURPLE, 1), 1);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 5);
        playerResources.put(Resource.SHIELD, 3);

        Assert.assertFalse(leaderCard.isPlayable(playerBanners, playerResources));
    }

    @Test
    public void falseIsPlayableTest3() {
        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.GOLD, 3);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, resourceProduced, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, resourceCost, sa);

        Map<Banner, Integer> playerBanners = new HashMap<>();

        playerBanners.put(new Banner(BannerEnum.GREEN, 1), 3);
        playerBanners.put(new Banner(BannerEnum.YELLOW, 3), 1);
        playerBanners.put(new Banner(BannerEnum.BLUE, 2), 1);
        playerBanners.put(new Banner(BannerEnum.PURPLE, 2), 1);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.SHIELD, 3);

        Assert.assertFalse(leaderCard.isPlayable(playerBanners, playerResources));
    }

    @Test
    public void activateProductionPowerTest() {
        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        ProductionPower sa = new ProductionPower(resourceRequired, resourceProduced, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, new HashMap<>(), sa);

        GameSettings gameSettings = buildGameSettings();
        Player player = new Player(gameSettings, "test");
        player.reset();

        Map<Resource, Integer> resourceSelectable = new HashMap<>();

        resourceSelectable.put(Resource.GOLD, 1);

        sa.setSelectableResource(resourceSelectable, resourceSelectable);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.STONE, 3);
        playerResources.put(Resource.SHIELD, 1);
        playerResources.put(Resource.SERVANT, 0);

        playerResources.forEach((key, value) -> player.getDashboard().storeResourceInLocker(key, value));

        leaderCard.activate(player);

        playerResources = player.getDashboard().getAllPlayerResources();

        Assert.assertEquals(player.getDashboard().getFaithMarkerPosition(), 1);
        Assert.assertEquals((int) playerResources.get(Resource.SERVANT), 0);
        Assert.assertEquals((int) playerResources.get(Resource.GOLD), 3);
        Assert.assertEquals((int) playerResources.get(Resource.STONE), 3);
        Assert.assertEquals((int) playerResources.get(Resource.SHIELD), 1);
        Assert.assertFalse(sa.isActivatable());
    }

    @Test
    public void activateDevelopmentCardDiscountTest() {
        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);


        SpecialAbility sa = new DevelopmentCardDiscount(Resource.GOLD, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, new HashMap<>(), sa);

        GameSettings gameSettings = buildGameSettings();
        Player player = new Player(gameSettings, "test");
        player.reset();

        leaderCard.activate(player);

        Assert.assertEquals(player.getActiveDiscounts().size(), 1);
        Assert.assertEquals(player.getActiveDiscounts().get(0), sa);
    }

    @Test
    public void activateWarehouseExtraSpaceTest() {
        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        WarehouseExtraSpace warehouseExtraSpace = new WarehouseExtraSpace(Resource.GOLD);
        warehouseExtraSpace.setLeaderCardPos(0);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, new HashMap<>(), warehouseExtraSpace);

        GameSettings gameSettings = buildGameSettings();
        Player player = new Player(gameSettings, "test");
        player.reset();

        player.getDashboard().placeLeaderCard(leaderCard);

        leaderCard.activate(player);

        ArrayList<Resource> res = new ArrayList<>();
        res.add(Resource.GOLD);
        player.getDashboard().addResourcesToSupply(res);

        player.getDashboard().addResourcesToSupply(res);
        player.getDashboard().storeFromSupplyInExtraDeposit(0, 0, 0);
        player.getDashboard().storeResourceInLocker(Resource.GOLD, 1);
        player.getDashboard().storeResourceInLocker(Resource.SHIELD, 1);

        Map<Resource, Integer> resources = player.getDashboard().getAllPlayerResources();

        Assert.assertEquals((int) resources.get(Resource.GOLD), 2);
        Assert.assertEquals((int) resources.get(Resource.STONE), 0);
        Assert.assertEquals((int) resources.get(Resource.SERVANT), 0);
        Assert.assertEquals((int) resources.get(Resource.SHIELD), 1);
    }

    @Test
    public void activateWhiteMarbleResourceTest() {
        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        WhiteMarbleResource whiteMarbleResource = new WhiteMarbleResource(Resource.GOLD);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, new HashMap<>(), whiteMarbleResource);

        GameSettings gameSettings = buildGameSettings();
        Player player = new Player(gameSettings, "test");
        player.reset();

        leaderCard.activate(player);

        Assert.assertEquals(player.getNumActiveWMR(), 1);
        Assert.assertEquals(player.getActivatedWMR().length, 1);
        Assert.assertEquals(player.getActivatedWMR()[0], Resource.GOLD);
    }

    private GameSettings buildGameSettings() {


        DevelopmentCard[] developmentCards = developmentCardDeckBuilder();

        int leaderCardNum = 16;
        LeaderCard[] leaderCards = leaderCardDeckBuilder(leaderCardNum);

        ProductionPower dashboardProductionPower = productionPowerBuilder();

        int vaticanReportsNum = 3;
        List<VaticanReport> vaticanReports = vaticanReportsListBuilder();

        int[] faithTrackVictoryPoints = {0, 0, 0, 1, 0, 0, 2, 0, 0, 4, 0, 0, 6, 0, 0, 9, 0, 0, 12, 0, 0, 16, 0, 0, 0};

        return new GameSettings(developmentCards, leaderCardNum, leaderCards,
                dashboardProductionPower, vaticanReports, faithTrackVictoryPoints);

    }

    private DevelopmentCard[] developmentCardDeckBuilder() {

        Map<Resource, Integer> resourceCost = new HashMap<>();
        resourceCost.put(Resource.SERVANT, 2);
        resourceCost.put(Resource.GOLD, 1);
        resourceCost.put(Resource.SHIELD, 1);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced, 2);

        return IntStream.range(0, GameSettings.DEVELOPMENT_CARD_NUM)
                .boxed()
                .map(i -> new DevelopmentCard(i, 4, resourceCost, p, chooseBanner(i)))
                .toArray(DevelopmentCard[]::new);

    }

    private LeaderCard[] leaderCardDeckBuilder(int leaderCardNum) {

        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.SHIELD, 1);

        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        SpecialAbility[] SAs = new SpecialAbility[4];
        SAs[0] = new ProductionPower(resourceRequired, resourceProduced, 1);
        SAs[1] = new DevelopmentCardDiscount(Resource.GOLD, 3);
        SAs[2] = new WarehouseExtraSpace(Resource.SERVANT);
        SAs[3] = new WhiteMarbleResource(Resource.SHIELD);

        return IntStream.range(0, leaderCardNum)
                .boxed()
                .map(i -> new LeaderCard(i, 2, bannerCost, resourceCost, SAs[i % 4]))
                .toArray(LeaderCard[]::new);

    }

    private ProductionPower productionPowerBuilder() {

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        return new ProductionPower(resourceRequired, resourceProduced, 2);

    }

    private List<VaticanReport> vaticanReportsListBuilder() {

        List<VaticanReport> vaticanReportsList = new ArrayList<>();
        vaticanReportsList.add(new VaticanReport(5, 8, 2));
        vaticanReportsList.add(new VaticanReport(12, 16, 3));
        vaticanReportsList.add(new VaticanReport(19, 24, 4));

        return vaticanReportsList;


    }

    private Banner chooseBanner(int val) {
        return switch (val % 12) {
            case 0 -> new Banner(BannerEnum.GREEN, 1);
            case 1 -> new Banner(BannerEnum.GREEN, 2);
            case 2 -> new Banner(BannerEnum.GREEN, 3);
            case 3 -> new Banner(BannerEnum.YELLOW, 1);
            case 4 -> new Banner(BannerEnum.YELLOW, 2);
            case 5 -> new Banner(BannerEnum.YELLOW, 3);
            case 6 -> new Banner(BannerEnum.BLUE, 1);
            case 7 -> new Banner(BannerEnum.BLUE, 2);
            case 8 -> new Banner(BannerEnum.BLUE, 3);
            case 9 -> new Banner(BannerEnum.PURPLE, 1);
            case 10 -> new Banner(BannerEnum.PURPLE, 2);
            case 11 -> new Banner(BannerEnum.PURPLE, 3);
            default -> null;
        };
    }
}