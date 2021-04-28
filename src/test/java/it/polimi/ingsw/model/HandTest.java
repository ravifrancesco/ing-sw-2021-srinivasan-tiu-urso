package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.ProductionPower;
import it.polimi.ingsw.model.specialAbilities.SpecialAbility;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HandTest {

    @Test
    public void ContstructorTest() {

        Hand hand = new Hand();

        Assert.assertNotNull(hand);
    }

    @Test
    public void addCardTest() {

        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.SHIELD, 1);

        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, null, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, resourceCost, bannerCost, sa);

        Hand hand = new Hand();

        hand.addCard(leaderCard);

        Assert.assertEquals(hand.getHandSize(), 1);
        Assert.assertEquals(leaderCard, hand.getCard(0));

    }

    @Test
    public void removeCardTest() {

        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.SHIELD, 1);

        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, null, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, resourceCost, bannerCost, sa);

        Hand hand = new Hand();

        hand.addCard(leaderCard);

        Assert.assertEquals(leaderCard, hand.removeCard(0));
        Assert.assertEquals(hand.getHandSize(), 0);

    }

    @Test
    public void resetTest() {

        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.SHIELD, 1);

        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, null, 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, resourceCost, bannerCost, sa);

        Hand hand = new Hand();

        hand.addCard(leaderCard);
        hand.reset();

        Assert.assertEquals(hand.getHandSize(), 0);

    }

}
