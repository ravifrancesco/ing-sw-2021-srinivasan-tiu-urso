package it.polimi.ingsw.model.reduced;

import it.polimi.ingsw.model.full.cards.Banner;
import it.polimi.ingsw.model.full.cards.BannerEnum;
import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.model.full.specialAbilities.*;
import it.polimi.ingsw.model.full.table.*;
import it.polimi.ingsw.model.utils.GameSettings;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static it.polimi.ingsw.model.full.table.DevelopmentCardGrid.DEVELOPMENT_CARD_NUM;

public class ReducedGameBoardTest {

    @Test
    public void constructorTest() {
        GameBoard gameBoard = new GameBoard();

        Deck leaderCardDeck = gameBoard.getLeaderDeck();
        Deck developmentCardDeck = gameBoard.getDevelopmentDeck();
        Deck discardDeck = gameBoard.getDiscardDeck();
        Market market = gameBoard.getMarket();
        DevelopmentCardGrid dvGrid = gameBoard.getDevelopmentCardGrid();

        Assert.assertEquals(leaderCardDeck.getSize(), 0);
        Assert.assertEquals(developmentCardDeck.getSize(), 0);
        Assert.assertEquals(discardDeck.getSize(), 0);
        Assert.assertNotEquals(market, null);
        Assert.assertNotEquals(dvGrid, null);
    }

    @Test
    public void resetTest() {
        GameSettings gameSettings = buildGameSettings();
        GameBoard gameBoard = new GameBoard();
        gameBoard.reset(gameSettings);

        Deck leaderCardDeck = gameBoard.getLeaderDeck();
        Deck developmentCardDeck = gameBoard.getDevelopmentDeck();
        Deck discardDeck = gameBoard.getDiscardDeck();
        Market market = gameBoard.getMarket();
        DevelopmentCardGrid dvGrid = gameBoard.getDevelopmentCardGrid();

        Assert.assertEquals(leaderCardDeck.getSize(), gameSettings.getLeaderCardNum());
        Assert.assertEquals(developmentCardDeck.getSize(), 0);

        LeaderCard[] lc = new LeaderCard[leaderCardDeck.getSize()];

        IntStream.range(0, lc.length)
                .forEach(i -> lc[i] = (LeaderCard) leaderCardDeck.getCard());

        List<DevelopmentCard> dvCardsFromGrid = new ArrayList<>();

        IntStream.range(0, DEVELOPMENT_CARD_NUM)
                .forEach(i -> dvCardsFromGrid.add(dvGrid.buy(getRow(i), getColumn(i))));

        Assert.assertTrue(Arrays.asList(gameSettings.getLeaderCards()).containsAll(Arrays.asList(lc)));
        Assert.assertTrue(Arrays.asList(lc).containsAll(Arrays.asList(gameSettings.getLeaderCards())));

        Assert.assertTrue(Arrays.asList(gameSettings.getDevelopmentCards()).containsAll(dvCardsFromGrid));
        Assert.assertTrue(dvCardsFromGrid.containsAll(Arrays.asList(gameSettings.getDevelopmentCards())));

        Assert.assertEquals(leaderCardDeck.getSize(), 0);
        Assert.assertEquals(developmentCardDeck.getSize(), 0);
        Assert.assertNotEquals(market, null);
        Assert.assertNotEquals(dvGrid, null);
        Assert.assertNotEquals(discardDeck, null);
    }

    @Test
    public void discardLeaderCardTest() {
        GameSettings gameSettings = buildGameSettings();
        GameBoard gameBoard = new GameBoard();
        gameBoard.reset(gameSettings);

        Player player = new Player(gameSettings, "test");
        player.reset();

        Deck leaderCardDeck = gameBoard.getLeaderDeck();
        Deck discardDeck = gameBoard.getDiscardDeck();

        LeaderCard c = (LeaderCard) leaderCardDeck.getCard();

        Assert.assertEquals(discardDeck.getSize(), 0);
        Assert.assertEquals(player.getDashboard().getFaithMarkerPosition(), 0);

        gameBoard.discardCard(c);

        discardDeck = gameBoard.getDiscardDeck();

        Assert.assertEquals(discardDeck.getSize(), 1);
        Assert.assertEquals(discardDeck.getCard(), c);
    }

    @Test
    public void getLeaderCardTest() {
        GameSettings gameSettings = buildGameSettings();
        GameBoard gameBoard = new GameBoard();
        gameBoard.reset(gameSettings);

        Deck leaderCardDeck = gameBoard.getLeaderDeck();

        Assert.assertEquals(leaderCardDeck.getSize(), gameSettings.getLeaderCardNum());

        LeaderCard c = gameBoard.getLeaderCard();

        leaderCardDeck = gameBoard.getLeaderDeck();

        Assert.assertEquals(leaderCardDeck.getSize(), gameSettings.getLeaderCardNum() - 1);
    }

    @Test
    public void marketTest() {
        GameSettings gameSettings = buildGameSettings();
        GameBoard gameBoard = new GameBoard();
        gameBoard.reset(gameSettings);
        Player p = new Player(gameSettings, "test");
        Market market = gameBoard.getMarket();

        for (int possibleMove = 0; possibleMove < 7; possibleMove++) {
            ArrayList<Resource> testResList = new ArrayList<>();
            Map<Resource, Long> testRes;

            ArrayList<Resource> actualResList;
            Map<Resource, Long> actualRes;

            int pM = possibleMove;
            if (possibleMove < 3) {
                // row move
                IntStream.range(0, Market.gridColLength).forEach(i -> testResList.add(market.getMarble(pM, i).getResource(p)));
                actualResList = gameBoard.getResourcesFromMarket(pM, p);
            } else {
                // col move
                IntStream.range(0, Market.gridRowLength).forEach(i -> testResList.add(market.getMarble(i, pM - 3).getResource(p)));
                actualResList = gameBoard.getResourcesFromMarket(pM, p);
            }

            testRes = testResList.stream().filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            actualRes = actualResList.stream().filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            Assert.assertEquals(testRes.get(Resource.STONE), actualRes.get(Resource.STONE));
            Assert.assertEquals(testRes.get(Resource.GOLD), actualRes.get(Resource.GOLD));
            Assert.assertEquals(testRes.get(Resource.SERVANT), actualRes.get(Resource.SERVANT));
            Assert.assertEquals(testRes.get(Resource.SHIELD), actualRes.get(Resource.SHIELD));

        }
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