package it.polimi.ingsw.model.specialAbilities;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.ProductionPower;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.IntStream;

public class ProductionPowerTest {

    @Test
    public void constructorTest() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Assert.assertEquals(p.getNumberFaithPoints(), 2);
        Assert.assertEquals(p.getResourceRequired(), resourceRequired);
        Assert.assertEquals(p.getResourceProduced(), resourceProduced);
        Assert.assertTrue(p.isActivatable());
        Assert.assertEquals(p.getType(), SpecialAbilityType.PRODUCTION_POWER);
    }

    @Test
    public void toStringTest() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        String s1 = "SA=PP;RR=STONE:1,GOLD:1;RP=SHIELD:1;FP=2;";
        String s2 = "SA=PP;RR=GOLD:1,STONE:1;RP=SHIELD:1;FP=2;";

        Assert.assertTrue(s1.equals(p.toString()) || s2.equals(p.toString()));
    }

    @Test
    public void toStringWithoutRPTest() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        ProductionPower p = new ProductionPower(resourceRequired, null,2);

        String s1 = "SA=PP;RR=STONE:1,GOLD:1;FP=2;";
        String s2 = "SA=PP;RR=GOLD:1,STONE:1;FP=2;";

        Assert.assertTrue(s1.equals(p.toString()) || s2.equals(p.toString()));
    }

    @Test
    public void isActivatableTrueWithoutSelectableResources() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        p.setSelectableResource(emptyOptional, emptyOptional);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.STONE, 3);
        playerResources.put(Resource.SHIELD, 1);

        Assert.assertTrue(p.isActivatable(playerResources));
    }

    @Test
    public void isActivatableFalseWithoutSelectableResources() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        p.setSelectableResource(emptyOptional, emptyOptional);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.SERVANT, 3);
        playerResources.put(Resource.SHIELD, 1);

        Assert.assertFalse(p.isActivatable(playerResources));
    }

    @Test
    public void isActivatableTrueWithSelectableResources() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.SHIELD, 2);
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SERVANT, 3);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Map<Resource, Integer> resourceRequiredSelectable = new HashMap<>();

        resourceRequiredSelectable.put(Resource.GOLD, 1);

        Optional<Map<Resource, Integer>> resourceRequiredOptional = Optional.of(resourceRequiredSelectable);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        p.setSelectableResource(resourceRequiredOptional, emptyOptional);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.SHIELD, 2);

        Assert.assertTrue(p.isActivatable(playerResources));
    }

    @Test
    public void isActivatableFalseWithSelectableResources() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.SHIELD, 2);
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SERVANT, 3);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Map<Resource, Integer> resourceRequiredSelectable = new HashMap<>();

        resourceRequiredSelectable.put(Resource.GOLD, 1);

        Optional<Map<Resource, Integer>> resourceRequiredOptional = Optional.of(resourceRequiredSelectable);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        p.setSelectableResource(resourceRequiredOptional, emptyOptional);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.SHIELD, 1);

        Assert.assertFalse(p.isActivatable(playerResources));
    }

    @Test
    public void isActivatableExceptionTest1() {
        int thrownExceptions = 0;

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.SHIELD, 2);
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SERVANT, 3);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Map<Resource, Integer> resourceRequiredSelectable = new HashMap<>();

        resourceRequiredSelectable.put(Resource.GOLD, 1);
        resourceRequiredSelectable.put(Resource.STONE, 1);

        Optional<Map<Resource, Integer>> resourceRequiredOptional = Optional.of(resourceRequiredSelectable);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        p.setSelectableResource(resourceRequiredOptional, emptyOptional);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.SHIELD, 1);

        try{
            p.isActivatable(playerResources);
        } catch(IllegalArgumentException e) {
            thrownExceptions++;
        }

        Assert.assertEquals(thrownExceptions, 1);
    }

    @Test
    public void isActivatableExceptionTest2() {
        int thrownExceptions = 0;

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.SHIELD, 2);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Map<Resource, Integer> resourceProducedSelectable = new HashMap<>();

        resourceProducedSelectable.put(Resource.GOLD, 1);
        resourceProducedSelectable.put(Resource.STONE, 1);

        Optional<Map<Resource, Integer>> resourceProducedOptional = Optional.of(resourceProducedSelectable);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        p.setSelectableResource(emptyOptional, resourceProducedOptional);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 3);
        playerResources.put(Resource.SHIELD, 3);
        playerResources.put(Resource.STONE, 1);
        playerResources.put(Resource.SERVANT, 2);

        try{
            p.isActivatable(playerResources);
        } catch(IllegalArgumentException e) {
            thrownExceptions++;
        }

        Assert.assertEquals(thrownExceptions, 1);
    }

    @Test
    public void invalidRequiredMapTest() {
        int thrownExceptions = 0;

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.SHIELD, 2);
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SERVANT, 3);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        try{
            p.setSelectableResource(emptyOptional, emptyOptional);
        } catch(IllegalStateException e) {
            thrownExceptions++;
        }

        Assert.assertEquals(thrownExceptions, 1);
    }

    @Test
    public void invalidProducedMapTest() {
        int thrownExceptions = 0;

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.SHIELD, 2);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        try{
            p.setSelectableResource(emptyOptional, emptyOptional);
        } catch(IllegalStateException e) {
            thrownExceptions++;
        }

        Assert.assertEquals(thrownExceptions, 1);
    }

    @Test
    public void getModifiedMapTest() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 3);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Map<Resource, Integer> resourceRequiredSelectable = new HashMap<>();
        Map<Resource, Integer> resourceProducedSelectable = new HashMap<>();

        resourceRequiredSelectable.put(Resource.GOLD, 1);
        resourceProducedSelectable.put(Resource.SHIELD, 1);

        Optional<Map<Resource, Integer>> resourceRequiredOptional = Optional.of(resourceRequiredSelectable);
        Optional<Map<Resource, Integer>> resourceProducedOptional = Optional.of(resourceProducedSelectable);

        p.setSelectableResource(resourceRequiredOptional, resourceProducedOptional);

        Map<Resource, Integer> resourceRequiredModified = p.getResourceRequiredModified();
        Map<Resource, Integer> resourceProducedModified = p.getResourceProducedModified();

        Assert.assertEquals(resourceRequiredSelectable, resourceRequiredModified);
        Assert.assertEquals(resourceProducedSelectable, resourceProducedModified);
    }

    @Test
    public void activateTest() {
        GameSettings gameSettings = buildGameSettings();
        Player player = new Player(gameSettings);
        player.reset();

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SERVANT, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        p.setSelectableResource(emptyOptional, emptyOptional);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.STONE, 3);
        playerResources.put(Resource.SHIELD, 1);

        playerResources.forEach((key, value) -> player.getDashboard().storeResourceInLocker(key, value));

        p.activate(player);

        playerResources = player.getDashboard().getAllPlayerResources();

        Assert.assertEquals(player.getDashboard().getFaithMarkerPosition(), 2);
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.SERVANT)), Optional.of(1));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.GOLD)), Optional.of(2));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.STONE)), Optional.of(3));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.SHIELD)), Optional.of(1));
    }

    @Test
    public void activateWithouFaithPointsTest() {
        GameSettings gameSettings = buildGameSettings();
        Player player = new Player(gameSettings);
        player.reset();

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SERVANT, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,0);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        p.setSelectableResource(emptyOptional, emptyOptional);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.STONE, 3);
        playerResources.put(Resource.SHIELD, 1);

        playerResources.forEach((key, value) -> player.getDashboard().storeResourceInLocker(key, value));

        p.activate(player);

        playerResources = player.getDashboard().getAllPlayerResources();

        Assert.assertEquals(player.getDashboard().getFaithMarkerPosition(), 0);
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.SERVANT)), Optional.of(1));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.GOLD)), Optional.of(2));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.STONE)), Optional.of(3));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.SHIELD)), Optional.of(1));
    }

    @Test
    public void activateWithoutResourceProducedTest() {
        GameSettings gameSettings = buildGameSettings();
        Player player = new Player(gameSettings);
        player.reset();

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        ProductionPower p = new ProductionPower(resourceRequired, new HashMap<>(),2);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        p.setSelectableResource(emptyOptional, emptyOptional);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.STONE, 3);
        playerResources.put(Resource.SHIELD, 1);

        playerResources.forEach((key, value) -> player.getDashboard().storeResourceInLocker(key, value));

        p.activate(player);

        playerResources = player.getDashboard().getAllPlayerResources();

        Assert.assertEquals(player.getDashboard().getFaithMarkerPosition(), 2);
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.GOLD)), Optional.of(2));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.STONE)), Optional.of(3));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.SHIELD)), Optional.of(1));
    }

    @Test
    public void activateFailNotEnoughResourcesTest() {
        int thrownExceptions = 0;

        GameSettings gameSettings = buildGameSettings();
        Player player = new Player(gameSettings);
        player.reset();

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SERVANT, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        p.setSelectableResource(emptyOptional, emptyOptional);

        try {
            p.activate(player);
        } catch (IllegalStateException e) { thrownExceptions++; }

        Map<Resource, Integer> playerResources = player.getDashboard().getAllPlayerResources();

        Assert.assertEquals(player.getDashboard().getFaithMarkerPosition(), 0);
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.SERVANT)), Optional.of(0));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.GOLD)), Optional.of(0));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.STONE)), Optional.of(0));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.SHIELD)), Optional.of(0));
        Assert.assertEquals(thrownExceptions, 1);
    }

    @Test
    public void doubleActivationTest() {
        int thrownExceptions = 0;
        GameSettings gameSettings = buildGameSettings();
        Player player = new Player(gameSettings);
        player.reset();

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SERVANT, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        p.setSelectableResource(emptyOptional, emptyOptional);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 1);
        playerResources.put(Resource.STONE, 1);

        playerResources.forEach((key, value) -> player.getDashboard().storeResourceInLocker(key, value));

        p.activate(player);

        try{
            p.activate(player);
        } catch (UnsupportedOperationException e) { thrownExceptions++; }

        playerResources = player.getDashboard().getAllPlayerResources();

        Assert.assertEquals(player.getDashboard().getFaithMarkerPosition(), 2);
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.SERVANT)), Optional.of(1));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.GOLD)), Optional.of(1));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.STONE)), Optional.of(1));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.SHIELD)), Optional.of(0));
        Assert.assertEquals(thrownExceptions, 1);
    }

    @Test
    public void resetTest() {
        GameSettings gameSettings = buildGameSettings();
        Player player = new Player(gameSettings);
        player.reset();

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SERVANT, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        p.setSelectableResource(emptyOptional, emptyOptional);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 1);
        playerResources.put(Resource.STONE, 1);

        playerResources.forEach((key, value) -> player.getDashboard().storeResourceInLocker(key, value));

        p.activate(player);

        p.reset();

        p.activate(player);

        playerResources = player.getDashboard().getAllPlayerResources();

        Assert.assertEquals(player.getDashboard().getFaithMarkerPosition(), 2);
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.SERVANT)), Optional.of(2));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.GOLD)), Optional.of(1));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.STONE)), Optional.of(1));
        Assert.assertEquals(Optional.ofNullable(playerResources.get(Resource.SHIELD)), Optional.of(0));
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

    private int getRow(int val) {
        return switch (val % 12) {
            case 0, 1, 2, 3 -> 1;
            case 4, 5, 6, 7 -> 2;
            case 8, 9, 10, 11 -> 3;
            default -> -1;
        };
    }

    private int getColumn(int val) {
        return switch (val % 12) {
            case 0, 4, 8 -> 1;
            case 1, 5, 9 -> 2;
            case 2, 6, 10 -> 3;
            case 3, 7, 11 -> 4;
            default -> -1;
        };
    }
}
