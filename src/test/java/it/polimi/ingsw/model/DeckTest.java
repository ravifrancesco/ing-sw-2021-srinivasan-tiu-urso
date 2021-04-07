package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assumptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class DeckTest {

    @Test
    public void developmentCardDeckTest() {
        DevelopmentCard[] developmentCards = developmentCardDeckBuilder();
        Deck developmentDeck = new Deck();

        developmentDeck.init(Arrays.asList(developmentCards));

        Assert.assertEquals(developmentDeck.getSize(), developmentCards.length);

        developmentDeck.shuffle();

        DevelopmentCard[] dc = new DevelopmentCard[developmentDeck.getSize()];

        IntStream.range(0, dc.length)
                .forEach(i -> dc[i] = (DevelopmentCard) developmentDeck.getCard());

        Assumptions.assumeTrue(Arrays.asList(developmentCards).containsAll(Arrays.asList(dc)));
        Assumptions.assumeTrue(Arrays.asList(dc).containsAll(Arrays.asList(developmentCards)));
    }

    @Test
    public void leaderCardDeckTest() {
        int leaderCardNum = 16;
        LeaderCard[] leaderCards = leaderCardDeckBuilder(leaderCardNum);
        Deck leaderDeck = new Deck();

        leaderDeck.init(Arrays.asList(leaderCards));

        Assert.assertEquals(leaderDeck.getSize(), leaderCards.length);

        leaderDeck.shuffle();

        LeaderCard[] lc = new LeaderCard[leaderDeck.getSize()];

        IntStream.range(0, lc.length)
                .forEach(i -> lc[i] = (LeaderCard) leaderDeck.getCard());

        Assumptions.assumeTrue(Arrays.asList(leaderCards).containsAll(Arrays.asList(lc)));
        Assumptions.assumeTrue(Arrays.asList(lc).containsAll(Arrays.asList(leaderCards)));
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

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2, false);

        Map<Resource, Integer> resourceCostDC = new HashMap<>();
        resourceCostDC.put(Resource.SERVANT, 3);

        return IntStream.range(0, GameSettings.DEVELOPMENT_CARD_NUM)
                .boxed()
                .map(i -> new DevelopmentCard(i, 4, resourceCost, p, banner))
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
        resourceProduced.put(Resource.SHIELD, 1);
        SpecialAbility sa = new ProductionPower(resourceRequired, resourceProduced, 1, true);
        // TODO change special abilities

        return  IntStream.range(0, leaderCardNum)
                .boxed()
                .map(i -> new LeaderCard(i, 2, resourceCost, bannerCost, sa))
                .toArray(LeaderCard[]::new);

    }
}
