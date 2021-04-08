package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assumptions;

import java.util.*;
import java.util.stream.IntStream;

import static it.polimi.ingsw.model.DevelopmentCardGrid.DEVELOPMENT_CARD_NUM;

public class DevelopmentCardGridTest {

    @Test
    public void isBuyableTrueTest() {
        Deck developmentCardDeck = new Deck();
        developmentCardDeck.init(Arrays.asList(developmentCardDeckBuilder()));

        DevelopmentCardGrid dvGrid = new DevelopmentCardGrid();
        dvGrid.init(developmentCardDeck);

        Map<Resource, Integer> playerResources = new HashMap<>();
        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.SERVANT, 3);
        playerResources.put(Resource.SHIELD, 1);
        playerResources.put(Resource.STONE, 4);

        Assert.assertTrue(dvGrid.isBuyable(1, 1, playerResources));

    }

    @Test
    public void isBuyableFalseTest() {
        Deck developmentCardDeck = new Deck();
        developmentCardDeck.init(Arrays.asList(developmentCardDeckBuilder()));

        DevelopmentCardGrid dvGrid = new DevelopmentCardGrid();
        dvGrid.init(developmentCardDeck);

        Map<Resource, Integer> playerResources = new HashMap<>();
        playerResources.put(Resource.GOLD, 1);
        playerResources.put(Resource.SERVANT, 1);
        playerResources.put(Resource.SHIELD, 1);
        playerResources.put(Resource.STONE, 4);

        Assert.assertFalse(dvGrid.isBuyable(1, 1, playerResources));
    }

    @Test
    public void checkValidPositions() {
        Deck developmentCardDeck = new Deck();
        developmentCardDeck.init(Arrays.asList(developmentCardDeckBuilder()));

        DevelopmentCardGrid dvGrid = new DevelopmentCardGrid();
        dvGrid.init(developmentCardDeck);

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
        developmentCardDeck.init(Arrays.asList(developmentCardDeckBuilder()));

        List<DevelopmentCard> dvStartingDeck = Arrays.asList(developmentCardDeckBuilder());

        DevelopmentCardGrid dvGrid = new DevelopmentCardGrid();
        dvGrid.init(developmentCardDeck);

        List<DevelopmentCard> dvCardsFromGrid = new ArrayList<>();

        IntStream.range(0, DEVELOPMENT_CARD_NUM)
                .forEach(i -> dvCardsFromGrid.add(dvGrid.buy(getRow(i), getColumn(i))));

        Assert.assertTrue(dvStartingDeck.containsAll(dvCardsFromGrid));
        Assert.assertTrue(dvCardsFromGrid.containsAll(dvStartingDeck));
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

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2, false);

        return IntStream.range(0, GameSettings.DEVELOPMENT_CARD_NUM)
                .boxed()
                .map(i -> new DevelopmentCard(i, 4, resourceCost, p, chooseBanner(i)))
                .toArray(DevelopmentCard[]::new);

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
