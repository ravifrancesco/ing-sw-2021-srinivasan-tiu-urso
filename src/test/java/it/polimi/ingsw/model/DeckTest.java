package it.polimi.ingsw.model;

import it.polimi.ingsw.model.full.cards.Banner;
import it.polimi.ingsw.model.full.cards.BannerEnum;
import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.model.full.specialAbilities.*;
import it.polimi.ingsw.model.full.table.Deck;
import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.full.table.VaticanReport;
import it.polimi.ingsw.model.utils.GameSettings;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.IntStream;

public class DeckTest {

    @Test
    public void constructorTest() {
        Deck deck = new Deck();

        Assert.assertEquals(deck.getSize(), 0);
    }

    @Test
    public void developmentCardDeckTest() {
        DevelopmentCard[] developmentCards = developmentCardDeckBuilder();
        Deck developmentDeck = new Deck();

        developmentDeck.reset(Arrays.asList(developmentCards));

        Assert.assertEquals(developmentDeck.getSize(), developmentCards.length);

        developmentDeck.shuffle();

        DevelopmentCard[] dc = new DevelopmentCard[developmentDeck.getSize()];

        IntStream.range(0, dc.length)
                .forEach(i -> dc[i] = (DevelopmentCard) developmentDeck.getCard());

        Assert.assertTrue(Arrays.asList(developmentCards).containsAll(Arrays.asList(dc)));
        Assert.assertTrue(Arrays.asList(dc).containsAll(Arrays.asList(developmentCards)));
    }

    @Test
    public void leaderCardDeckTest() {
        int leaderCardNum = 16;
        LeaderCard[] leaderCards = leaderCardDeckBuilder(leaderCardNum);
        Deck leaderDeck = new Deck();

        leaderDeck.reset(Arrays.asList(leaderCards));

        Assert.assertEquals(leaderDeck.getSize(), leaderCards.length);

        leaderDeck.shuffle();

        LeaderCard[] lc = new LeaderCard[leaderDeck.getSize()];

        IntStream.range(0, lc.length)
                .forEach(i -> lc[i] = (LeaderCard) leaderDeck.getCard());

        Assert.assertTrue(Arrays.asList(leaderCards).containsAll(Arrays.asList(lc)));
        Assert.assertTrue(Arrays.asList(lc).containsAll(Arrays.asList(leaderCards)));
    }

    @Test
    public void discardTest() {
        int leaderCardNum = 16;
        LeaderCard[] leaderCards = leaderCardDeckBuilder(leaderCardNum);
        Deck discardDeck = new Deck();
        LeaderCard lc = leaderCards[0];

        Assert.assertEquals(discardDeck.getSize(), 0);

        discardDeck.add(lc);

        Assert.assertEquals(discardDeck.getSize(), 1);

        LeaderCard lc2 = (LeaderCard) discardDeck.getCard();

        Assert.assertEquals(discardDeck.getSize(), 0);
        Assert.assertEquals(lc, lc2);

    }

    @Test
    public void copyTest() {
        DevelopmentCard[] developmentCards = developmentCardDeckBuilder();
        Deck developmentDeck = new Deck();

        developmentDeck.reset(Arrays.asList(developmentCards));

        Assert.assertEquals(developmentDeck.getSize(), developmentCards.length);

        developmentDeck.shuffle();

        Deck developmentDeckCopy = developmentDeck.copy();
        developmentDeckCopy.shuffle();

        DevelopmentCard[] dc = new DevelopmentCard[developmentDeck.getSize()];

        IntStream.range(0, dc.length)
                .forEach(i -> dc[i] = (DevelopmentCard) developmentDeck.getCard());

        DevelopmentCard[] dcCopy = new DevelopmentCard[developmentDeckCopy.getSize()];

        IntStream.range(0, dcCopy.length)
                .forEach(i -> dcCopy[i] = (DevelopmentCard) developmentDeckCopy.getCard());

        Assert.assertTrue(Arrays.asList(dc).containsAll(Arrays.asList(dcCopy)));
        Assert.assertTrue(Arrays.asList(dcCopy).containsAll(Arrays.asList(dc)));
    }

    @Test
    public void resetProductionPowerDevelopmentCardsTest() {
        DevelopmentCard[] developmentCards = developmentCardDeckBuilder();
        Deck developmentDeck = new Deck();
        Player player = new Player(buildGameSettings(), "test");
        player.reset();

        developmentDeck.reset(Arrays.asList(developmentCards));

        Assert.assertEquals(developmentDeck.getSize(), developmentCards.length);

        developmentDeck.shuffle();

        DevelopmentCard[] dc = new DevelopmentCard[developmentDeck.getSize()];

        IntStream.range(0, dc.length)
                .forEach(i -> dc[i] = (DevelopmentCard) developmentDeck.getCard());

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 1);
        playerResources.put(Resource.STONE, 1);

        playerResources.forEach((key, value) -> player.getDashboard().storeResourceInLocker(key, value));

        IntStream.range(0, dc.length)
                .forEach(i -> dc[i].getProductionPower().setSelectableResource(new HashMap<>(), new HashMap<>()));

        IntStream.range(0, dc.length)
                .filter(i -> i%2 == 0)
                .forEach(i -> dc[i].activate(player));

        IntStream.range(0, dc.length)
                .forEach(i -> developmentDeck.add(dc[i]));

        developmentDeck.resetProductionPowerDevelopmentCards();

        Assert.assertTrue(IntStream.range(0, developmentDeck.getSize()).allMatch(i -> ((DevelopmentCard) developmentDeck.getCard()).getProductionPower().isActivatable()));
    }

    @Test
    public void resetProductionPowerLeaderCards() {
        int leaderCardNum = 16;
        LeaderCard[] leaderCards = leaderCardDeckBuilder(leaderCardNum);
        Deck leaderDeck = new Deck();
        Player player = new Player(buildGameSettings(), "test");
        player.reset();

        leaderDeck.reset(Arrays.asList(leaderCards));

        Assert.assertEquals(leaderDeck.getSize(), leaderCards.length);

        leaderDeck.shuffle();

        LeaderCard[] lc = new LeaderCard[leaderDeck.getSize()];

        IntStream.range(0, lc.length)
                .forEach(i -> lc[i] = (LeaderCard) leaderDeck.getCard());

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 1);

        playerResources.forEach((key, value) -> player.getDashboard().storeResourceInLocker(key, value));

        IntStream.range(0, lc.length)
                .filter(i -> lc[i].getSpecialAbility().getType() == SpecialAbilityType.PRODUCTION_POWER)
                .forEach(i -> ((ProductionPower) lc[i].getSpecialAbility()).setSelectableResource(new HashMap<>(), resourceProduced));

        IntStream.range(0, lc.length)
                .filter(i -> i%2 == 0)
                .forEach(i -> lc[i].activate(player));

        IntStream.range(0, lc.length)
                .forEach(i -> leaderDeck.add(lc[i]));

        leaderDeck.resetProductionPowerLeaderCards();

        Assert.assertTrue(IntStream.range(0, leaderDeck.getSize())
                .mapToObj(i -> ((LeaderCard) leaderDeck.getCard()).getSpecialAbility())
                .filter(specialAbility -> specialAbility.getType()==SpecialAbilityType.PRODUCTION_POWER)
                .map(specialAbility -> (ProductionPower) specialAbility)
                .allMatch(ProductionPower::isActivatable));

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

        Banner banner = new Banner(BannerEnum.BLUE, 2);

        Map<Resource, Integer> resourceCost = new HashMap<>();
        resourceCost.put(Resource.SERVANT, 3);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        return IntStream.range(0, GameSettings.DEVELOPMENT_CARD_NUM)
                .boxed()
                .map(i -> new DevelopmentCard(i, 4, resourceCost, new ProductionPower(resourceRequired, resourceProduced,0), banner))
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

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        return  IntStream.range(0, leaderCardNum)
                .boxed()
                .map(i -> new LeaderCard(i, 2, bannerCost, resourceCost, new ProductionPower(resourceRequired, resourceProduced, 1)))
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

        return new ProductionPower(resourceRequired, resourceProduced, 1);
    }
}