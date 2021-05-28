package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.specialAbilities.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.IntStream;

public class DevelopmentCardTest {

    @Test
    public void constructorTest() {
        Banner banner = new Banner(BannerEnum.BLUE, 2);

        Map<Resource, Integer> resourceCost = new HashMap<>();
        resourceCost.put(Resource.SERVANT, 3);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        DevelopmentCard c = new DevelopmentCard(1, 5, resourceCost, p, banner);

        Assert.assertEquals(c.getId(), 1);
        Assert.assertEquals(c.getVictoryPoints(), 5);
        Assert.assertEquals(c.getBanner().getColor(), BannerEnum.BLUE);
        Assert.assertEquals(c.getBanner().getLevel(), 2);
        Assert.assertEquals(c.getResourceCost(), resourceCost);
        Assert.assertEquals(c.getProductionPower(), p);
    }

    @Test
    public void toStringTest() {
        Banner banner = new Banner(BannerEnum.BLUE, 2);

        Map<Resource, Integer> resourceCost = new HashMap<>();
        resourceCost.put(Resource.SERVANT, 3);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        DevelopmentCard c = new DevelopmentCard(1, 5, resourceCost, p, banner);

        String s1 = "ID=1;VP=5;BA=BLUE:2;RC=SERVANT:3;SA=PP;RR=STONE:1,GOLD:1;RP=SHIELD:1;FP=2;";
        String s2 = "ID=1;VP=5;BA=BLUE:2;RC=SERVANT:3;SA=PP;RR=GOLD:1,STONE:1;RP=SHIELD:1;FP=2;";

        Assert.assertTrue(s1.equals(c.toString()) || s2.equals(c.toString()));
    }

    @Test
    public void equalsTrueTest() {
        Banner banner = new Banner(BannerEnum.BLUE, 2);

        Map<Resource, Integer> resourceCost = new HashMap<>();
        resourceCost.put(Resource.SERVANT, 3);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        DevelopmentCard c = new DevelopmentCard(1, 5, resourceCost, p, banner);
        DevelopmentCard c2 = new DevelopmentCard(1, 5, resourceCost, p, banner);

        Assert.assertEquals(c, c2);
    }

    @Test
    public void equalsFalseTest() {
        Banner banner = new Banner(BannerEnum.BLUE, 2);

        Map<Resource, Integer> resourceCost = new HashMap<>();
        resourceCost.put(Resource.SERVANT, 3);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        DevelopmentCard c = new DevelopmentCard(1, 5, resourceCost, p, banner);

        Banner banner2 = new Banner(BannerEnum.GREEN, 1);

        Map<Resource, Integer> resourceProduced2 = new HashMap<>();
        resourceProduced2.put(Resource.SHIELD, 1);

        ProductionPower p2 = new ProductionPower(resourceRequired, resourceProduced2,1);

        DevelopmentCard c2 = new DevelopmentCard(2, 3, resourceCost, p2, banner2);

        Assert.assertNotEquals(c, c2);
    }

    @Test
    public void activateTest() {
        Banner banner = new Banner(BannerEnum.BLUE, 2);

        Map<Resource, Integer> resourceCost = new HashMap<>();
        resourceCost.put(Resource.SERVANT, 3);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        DevelopmentCard c = new DevelopmentCard(1, 5, resourceCost, p, banner);

        GameSettings gameSettings = buildGameSettings();
        Player player = new Player(gameSettings, "test");
        player.reset();

        p.setSelectableResource(new HashMap<>(), new HashMap<>());

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.STONE, 3);
        playerResources.put(Resource.SHIELD, 1);
        playerResources.put(Resource.SERVANT, 0);

        playerResources.forEach((key, value) -> player.getDashboard().storeResourceInLocker(key, value));

        c.activate(player);

        playerResources = player.getDashboard().getAllPlayerResources();

        Assert.assertEquals(player.getDashboard().getFaithMarkerPosition(), 2);
        Assert.assertEquals((int) playerResources.get(Resource.SERVANT), 0);
        Assert.assertEquals((int) playerResources.get(Resource.GOLD), 2);
        Assert.assertEquals((int) playerResources.get(Resource.STONE), 3);
        Assert.assertEquals((int) playerResources.get(Resource.SHIELD), 2);
    }

    @Test
    public void resetProductionPowerTest() {
        Banner banner = new Banner(BannerEnum.BLUE, 2);

        Map<Resource, Integer> resourceCost = new HashMap<>();
        resourceCost.put(Resource.SERVANT, 3);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        DevelopmentCard c = new DevelopmentCard(1, 5, resourceCost, p, banner);

        GameSettings gameSettings = buildGameSettings();
        Player player = new Player(gameSettings, "test");
        player.reset();

        p.setSelectableResource(new HashMap<>(), new HashMap<>());

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.STONE, 3);
        playerResources.put(Resource.SHIELD, 1);
        playerResources.put(Resource.SERVANT, 0);

        playerResources.forEach((key, value) -> player.getDashboard().storeResourceInLocker(key, value));

        c.activate(player);

        c.resetProductionPower();

        c.activate(player);

        playerResources = player.getDashboard().getAllPlayerResources();

        Assert.assertEquals(player.getDashboard().getFaithMarkerPosition(), 4);
        Assert.assertEquals((int) playerResources.get(Resource.SERVANT), 0);
        Assert.assertEquals((int) playerResources.get(Resource.GOLD), 2);
        Assert.assertEquals((int) playerResources.get(Resource.STONE), 3);
        Assert.assertEquals((int) playerResources.get(Resource.SHIELD), 3);
    }

    private GameSettings buildGameSettings() {


        DevelopmentCard[] developmentCards = developmentCardDeckBuilder();

        int leaderCardNum = 16;
        LeaderCard[] leaderCards = leaderCardDeckBuilder(leaderCardNum);

        ProductionPower dashboardProductionPower = productionPowerBuilder();

        int vaticanReportsNum = 3;
        List<VaticanReport> vaticanReports = vaticanReportsListBuilder();

        int[] faithTrackVictoryPoints = {0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,0};

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

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

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

        return  IntStream.range(0, leaderCardNum)
                .boxed()
                .map(i -> new LeaderCard(i, 2, bannerCost, resourceCost, SAs[i%4]))
                .toArray(LeaderCard[]::new);

    }

    private ProductionPower productionPowerBuilder() {

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        return new ProductionPower(resourceRequired, resourceProduced,2);

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