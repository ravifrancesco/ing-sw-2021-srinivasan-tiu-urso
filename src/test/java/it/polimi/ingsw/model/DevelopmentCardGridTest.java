package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.DevelopmentCardDiscount;
import it.polimi.ingsw.model.specialAbilities.ProductionPower;
import it.polimi.ingsw.model.specialAbilities.SpecialAbility;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.IntStream;

import static it.polimi.ingsw.model.DevelopmentCardGrid.DEVELOPMENT_CARD_NUM;

public class DevelopmentCardGridTest {

    @Test
    public void constructorTest() {
        DevelopmentCardGrid dvGrid = new DevelopmentCardGrid();
        int cont = 0;

        for(int i=0; i < DevelopmentCardGrid.GRID_ROW_LENGTH; i++) {
            for(int j=0; j < DevelopmentCardGrid.GRID_COL_LENGTH; j++) {
                try {
                    dvGrid.isBuyable(i, j, new HashMap<>(), new ArrayList<>());
                } catch(IllegalArgumentException e) { cont++; }
            }
        }

        Assert.assertEquals(cont, DevelopmentCardGrid.GRID_ROW_LENGTH * DevelopmentCardGrid.GRID_COL_LENGTH);
    }

    @Test
    public void isBuyableTrueTest() {
        Deck developmentCardDeck = new Deck();
        developmentCardDeck.reset(Arrays.asList(developmentCardDeckBuilder()));

        DevelopmentCardGrid dvGrid = new DevelopmentCardGrid();
        dvGrid.reset();
        dvGrid.fillCardGrid(developmentCardDeck);

        Map<Resource, Integer> playerResources = new HashMap<>();
        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.SERVANT, 3);
        playerResources.put(Resource.SHIELD, 1);
        playerResources.put(Resource.STONE, 4);

        Assert.assertTrue(dvGrid.isBuyable(1, 1, playerResources, new ArrayList<>()));
    }

    @Test
    public void isBuyableFalseTest() {
        Deck developmentCardDeck = new Deck();
        developmentCardDeck.reset(Arrays.asList(developmentCardDeckBuilder()));

        DevelopmentCardGrid dvGrid = new DevelopmentCardGrid();
        dvGrid.reset();
        dvGrid.fillCardGrid(developmentCardDeck);

        Map<Resource, Integer> playerResources = new HashMap<>();
        playerResources.put(Resource.GOLD, 1);
        playerResources.put(Resource.SERVANT, 1);
        playerResources.put(Resource.SHIELD, 1);
        playerResources.put(Resource.STONE, 4);

        Assert.assertFalse(dvGrid.isBuyable(1, 1, playerResources, new ArrayList<>()));
    }

    @Test
    public void isBuyableTrueWithDiscountTest() {
        GameSettings gameSettings = buildGameSettings();

        Deck developmentCardDeck = new Deck();
        developmentCardDeck.reset(Arrays.asList(gameSettings.getDevelopmentCards()));

        DevelopmentCardGrid dvGrid = new DevelopmentCardGrid();
        dvGrid.reset();
        dvGrid.fillCardGrid(developmentCardDeck);

        Map<Resource, Integer> playerResources = new HashMap<>();
        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.SERVANT, 3);

        Player p = new Player(gameSettings);
        p.reset();

        DevelopmentCardDiscount discount1 = new DevelopmentCardDiscount(Resource.SHIELD, 1);

        discount1.activate(p);

        ArrayList<DevelopmentCardDiscount> activeDiscounts = p.getActiveDiscounts();

        Assert.assertTrue(dvGrid.isBuyable(1, 1, playerResources, activeDiscounts));
    }

    @Test
    public void isBuyableFalseWithDiscountTest() {
        GameSettings gameSettings = buildGameSettings();

        Deck developmentCardDeck = new Deck();
        developmentCardDeck.reset(Arrays.asList(gameSettings.getDevelopmentCards()));

        DevelopmentCardGrid dvGrid = new DevelopmentCardGrid();
        dvGrid.reset();
        dvGrid.fillCardGrid(developmentCardDeck);

        Map<Resource, Integer> playerResources = new HashMap<>();
        playerResources.put(Resource.GOLD, 2);

        Player p = new Player(gameSettings);
        p.reset();

        DevelopmentCardDiscount discount1 = new DevelopmentCardDiscount(Resource.SHIELD, 1);
        DevelopmentCardDiscount discount2 = new DevelopmentCardDiscount(Resource.SERVANT, 1);

        discount1.activate(p);
        discount2.activate(p);

        ArrayList<DevelopmentCardDiscount> activeDiscounts = p.getActiveDiscounts();

        Assert.assertFalse(dvGrid.isBuyable(1, 1, playerResources, activeDiscounts));
    }

    @Test
    public void peekTest() {
        Deck developmentCardDeck = new Deck();
        developmentCardDeck.reset(Arrays.asList(developmentCardDeckBuilder()));

        DevelopmentCardGrid dvGrid = new DevelopmentCardGrid();
        dvGrid.reset();
        dvGrid.fillCardGrid(developmentCardDeck);

        Map<Resource, Integer> playerResources = new HashMap<>();
        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.SERVANT, 3);
        playerResources.put(Resource.SHIELD, 1);
        playerResources.put(Resource.STONE, 4);

        DevelopmentCard developmentCard = dvGrid.peek(1, 1);

        Assert.assertEquals(developmentCard, dvGrid.buy(1, 1));
    }

    @Test
    public void invalidPositionTest() {
        int thrownExceptions = 0;
        Deck developmentCardDeck = new Deck();
        developmentCardDeck.reset(Arrays.asList(developmentCardDeckBuilder()));

        DevelopmentCardGrid dvGrid = new DevelopmentCardGrid();
        dvGrid.reset();
        dvGrid.fillCardGrid(developmentCardDeck);

        Map<Resource, Integer> playerResources = new HashMap<>();
        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.SERVANT, 3);
        playerResources.put(Resource.SHIELD, 1);
        playerResources.put(Resource.STONE, 4);

        try{
            dvGrid.isBuyable(0, 0, playerResources, new ArrayList<>());
        }
        catch (IllegalArgumentException e) {
            thrownExceptions += 1;
        }

        Assert.assertEquals(thrownExceptions, 1);
    }

    @Test
    public void emptyStackTest() {
        int thrownExceptions = 0;
        Deck developmentCardDeck = new Deck();
        developmentCardDeck.reset(Arrays.asList(developmentCardDeckBuilder()));

        DevelopmentCardGrid dvGrid = new DevelopmentCardGrid();
        dvGrid.reset();
        dvGrid.fillCardGrid(developmentCardDeck);

        Map<Resource, Integer> playerResources = new HashMap<>();
        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.SERVANT, 3);
        playerResources.put(Resource.SHIELD, 1);
        playerResources.put(Resource.STONE, 4);

        for(int i=0; i<DEVELOPMENT_CARD_NUM/12; i++) {
            dvGrid.buy(1, 1);
        }

        try{
            dvGrid.isBuyable(1, 1, playerResources, new ArrayList<>());
        }
        catch (IllegalArgumentException e) {
            thrownExceptions += 1;
        }

        Assert.assertEquals(thrownExceptions, 1);
    }

    @Test
    public void checkValidPositions() {
        Deck developmentCardDeck = new Deck();
        developmentCardDeck.reset(Arrays.asList(developmentCardDeckBuilder()));

        DevelopmentCardGrid dvGrid = new DevelopmentCardGrid();
        dvGrid.reset();
        dvGrid.fillCardGrid(developmentCardDeck);

        DevelopmentCard[] dvCards = new DevelopmentCard[DEVELOPMENT_CARD_NUM];

        IntStream.range(0, DEVELOPMENT_CARD_NUM)
                .forEach(i -> dvCards[i] = dvGrid.buy(getRow(i), getColumn(i)));

        long result = IntStream.range(0, DEVELOPMENT_CARD_NUM)
                .filter(i -> dvCards[i].getBanner().equalsColor(getBanner(i)) && dvCards[i].getBanner().equalsLevel(getBanner(i)))
                .count();

        Assert.assertEquals(result, DEVELOPMENT_CARD_NUM);
    }

    @Test
    public void checkAllCardsInStartingDeck() {
        Deck developmentCardDeck = new Deck();
        developmentCardDeck.reset(Arrays.asList(developmentCardDeckBuilder()));

        List<DevelopmentCard> dvStartingDeck = Arrays.asList(developmentCardDeckBuilder());

        DevelopmentCardGrid dvGrid = new DevelopmentCardGrid();
        dvGrid.reset();
        dvGrid.fillCardGrid(developmentCardDeck);

        List<DevelopmentCard> dvCardsFromGrid = new ArrayList<>();

        IntStream.range(0, DEVELOPMENT_CARD_NUM)
                .forEach(i -> dvCardsFromGrid.add(dvGrid.buy(getRow(i), getColumn(i))));

        Assert.assertTrue(dvStartingDeck.containsAll(dvCardsFromGrid));
        Assert.assertTrue(dvCardsFromGrid.containsAll(dvStartingDeck));
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

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);
        SpecialAbility sa = new ProductionPower(resourceRequired, resourceProduced, 1);

        return  IntStream.range(0, leaderCardNum)
                .boxed()
                .map(i -> new LeaderCard(i, 2, bannerCost, sa))
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

    private int getRow(int val) {
        return switch (val % 12) {
            case 0 -> 1;
            case 1 -> 1;
            case 2 -> 1;
            case 3 -> 1;
            case 4 -> 2;
            case 5 -> 2;
            case 6 -> 2;
            case 7 -> 2;
            case 8 -> 3;
            case 9 -> 3;
            case 10 -> 3;
            case 11 -> 3;
            default -> -1;
        };
    }

    private int getColumn(int val) {
        return switch (val % 12) {
            case 0 -> 1;
            case 1 -> 2;
            case 2 -> 3;
            case 3 -> 4;
            case 4 -> 1;
            case 5 -> 2;
            case 6 -> 3;
            case 7 -> 4;
            case 8 -> 1;
            case 9 -> 2;
            case 10 -> 3;
            case 11 -> 4;
            default -> -1;
        };
    }

    private Banner getBanner(int val) {
        return switch (val % 12) {
            case 0 -> new Banner(BannerEnum.GREEN, 1);
            case 1 -> new Banner(BannerEnum.BLUE, 1);
            case 2 -> new Banner(BannerEnum.YELLOW, 1);
            case 3 -> new Banner(BannerEnum.PURPLE, 1);
            case 4 -> new Banner(BannerEnum.GREEN, 2);
            case 5 -> new Banner(BannerEnum.BLUE, 2);
            case 6 -> new Banner(BannerEnum.YELLOW, 2);
            case 7 -> new Banner(BannerEnum.PURPLE, 2);
            case 8 -> new Banner(BannerEnum.GREEN, 3);
            case 9 -> new Banner(BannerEnum.BLUE, 3);
            case 10 -> new Banner(BannerEnum.YELLOW, 3);
            case 11 -> new Banner(BannerEnum.PURPLE, 3);
            default -> null;
        };
    }

}