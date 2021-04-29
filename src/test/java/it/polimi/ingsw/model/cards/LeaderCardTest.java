package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.IntStream;

public class LeaderCardTest{

    @Test
    public void constructorTest() {
        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, resourceProduced, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, sa);

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

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.ANY, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, new HashMap<>(), 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, sa);

        String s1 = "ID=1;VP=5;RC=ANY:1;BC=BLUE:2:1,GREEN:1:2;SA=PP;RR=GOLD:1;FP=1;";
        String s2 = "ID=1;VP=5;RC=ANY:1;BC=GREEN:1:2,BLUE:2:1;SA=PP;RR=GOLD:1;FP=1;";

        Assert.assertTrue(s1.equals(leaderCard.toString()) || s2.equals(leaderCard.toString()));

    }

    @Test
    public void equalsTrueTest() {
        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, resourceProduced, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, sa);
        LeaderCard leaderCard2 = new LeaderCard(1, 5, bannerCost, sa);

        Assert.assertTrue(leaderCard.equals(leaderCard2));
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

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, sa);

        Map<Banner, Integer> bannerCost2 = new HashMap<>();
        bannerCost2.put(new Banner(BannerEnum.YELLOW, 1), 3);

        LeaderCard leaderCard2 = new LeaderCard(2, 3, bannerCost2, sa);

        Assert.assertFalse(leaderCard.equals(leaderCard2));
    }

    @Test
    public void trueIsPlayableTest() {
        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, resourceProduced, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, sa);

        Map<Banner, Integer> playerBanners = new HashMap<>();

        playerBanners.put(new Banner(BannerEnum.GREEN, 1), 3);
        playerBanners.put(new Banner(BannerEnum.YELLOW, 3), 1);
        playerBanners.put(new Banner(BannerEnum.BLUE, 2), 1);
        playerBanners.put(new Banner(BannerEnum.PURPLE, 2), 1);

        Assert.assertTrue(leaderCard.isPlayable(playerBanners));
    }

    @Test
    public void falseIsPlayableTest1() {
        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, resourceProduced, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, sa);

        Map<Banner, Integer> playerBanners = new HashMap<>();

        playerBanners.put(new Banner(BannerEnum.GREEN, 1), 2);
        playerBanners.put(new Banner(BannerEnum.BLUE, 1), 1);
        playerBanners.put(new Banner(BannerEnum.YELLOW, 2), 1);
        playerBanners.put(new Banner(BannerEnum.BLUE, 3), 1);
        playerBanners.put(new Banner(BannerEnum.PURPLE, 2), 1);

        Assert.assertFalse(leaderCard.isPlayable(playerBanners));
    }

    @Test
    public void falseIsPlayableTest2() {
        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, resourceProduced, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, sa);

        Map<Banner, Integer> playerBanners = new HashMap<>();

        playerBanners.put(new Banner(BannerEnum.YELLOW, 1), 1);
        playerBanners.put(new Banner(BannerEnum.PURPLE, 1), 1);

        Assert.assertFalse(leaderCard.isPlayable(playerBanners));
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

        SpecialAbility sa = new ProductionPower(resourceRequired, resourceProduced, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, sa);

        GameSettings gameSettings = buildGameSettings();
        Player player = new Player(gameSettings);
        player.reset();

        Map<Resource, Integer> resourceSelectable = new HashMap<>();

        resourceSelectable.put(Resource.GOLD, 1);

        Optional<Map<Resource, Integer>> resourceOptional = Optional.of(resourceSelectable);

        ((ProductionPower) sa).setSelectableResource(resourceOptional, resourceOptional);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.STONE, 3);
        playerResources.put(Resource.SHIELD, 1);

        playerResources.forEach((key, value) -> player.getDashboard().storeResourceInLocker(key, value));

        leaderCard.activate(player);

        playerResources = player.getDashboard().getAllPlayerResources();

        Assert.assertEquals(player.getDashboard().getFaithMarkerPosition(), 2);
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.SERVANT)), Optional.of(0));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.GOLD)), Optional.of(3));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.STONE)), Optional.of(3));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.SHIELD)), Optional.of(1));
        Assert.assertFalse(((ProductionPower) sa).isActivatable());
    }

    @Test
    public void activateDevelopmentCardDiscountTest() {
        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);


        SpecialAbility sa = new DevelopmentCardDiscount(Resource.GOLD, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, bannerCost, sa);

        GameSettings gameSettings = buildGameSettings();
        Player player = new Player(gameSettings);
        player.reset();

        leaderCard.activate(player);

        Assert.assertEquals(player.getActiveDiscounts().size(), 1);
        Assert.assertEquals(player.getActiveDiscounts().get(0), sa);
    }

    @Test
    public void activateWarehouseExtraSpaceTest() {
        //TODO
    }

    @Test
    public void activateWhiteMarbleResourceTest() {
        //TODO
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

        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        return  IntStream.range(0, leaderCardNum)
                .boxed()
                .map(i -> new LeaderCard(i, 2, bannerCost, specialAbilityBuilder(i)))
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

    private SpecialAbility specialAbilityBuilder(int i) {

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        return switch (i % 4) {
            case 1 ->
                    new DevelopmentCardDiscount(Resource.GOLD, 1);
            case 2 ->
                    new WarehouseExtraSpace(Resource.SHIELD);
            case 3 ->
                    new WhiteMarbleResource(Resource.STONE);
            default ->
                    new ProductionPower(resourceRequired, resourceProduced, 1);
        };
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