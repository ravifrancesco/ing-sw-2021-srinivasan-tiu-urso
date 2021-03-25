package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assumptions;

import java.util.HashMap;
import java.util.Map;

public class LeaderCardTest{
    @Test
    public void constructorTest(){
        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.SHIELD, 1);
        resourceCost.put(Resource.SERVANT, 2);

        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner("GREEN", 1), 2);
        bannerCost.put(new Banner("BLUE", 2), 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, resourceCost, bannerCost, null);

        Assert.assertFalse(leaderCard.isDiscarded());
        Assert.assertFalse(leaderCard.isPlayed());
    }

    @Test
    public void discardTest(){
        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.SHIELD, 1);
        resourceCost.put(Resource.SERVANT, 2);

        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner("GREEN", 1), 2);
        bannerCost.put(new Banner("BLUE", 2), 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, resourceCost, bannerCost, null);

        leaderCard.discard();

        Assert.assertTrue(leaderCard.isDiscarded());
        Assert.assertFalse(leaderCard.isPlayed());
    }

    @Test
    public void playTest(){
        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.SHIELD, 1);
        resourceCost.put(Resource.SERVANT, 2);

        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner("GREEN", 1), 2);
        bannerCost.put(new Banner("BLUE", 2), 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, resourceCost, bannerCost, null);

        leaderCard.play(null);

        Assert.assertFalse(leaderCard.isDiscarded());
        Assert.assertTrue(leaderCard.isPlayed());
    }

    @Test
    public void resetTest(){
        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.SHIELD, 1);
        resourceCost.put(Resource.SERVANT, 2);

        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner("GREEN", 1), 2);
        bannerCost.put(new Banner("BLUE", 2), 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, resourceCost, bannerCost, null);

        leaderCard.discard();
        leaderCard.play(null);

        leaderCard.reset();

        Assert.assertFalse(leaderCard.isDiscarded());
        Assert.assertFalse(leaderCard.isPlayed());
    }

    @Test
    public void nullTest(){
        LeaderCard leaderCard = new LeaderCard(1, 5, null, null, null);

        Assert.assertEquals("ID=1;VP=5;RC=;BC=;SA=;", leaderCard.toString());
    }

    /*@Test
    public void playableTest1(){
        Map<Resource, Integer> resourceCost = new HashMap<Resource, Integer>();

        resourceCost.put(Resource.SHIELD, 1);
        resourceCost.put(Resource.SERVANT, 2);

        Map<Banner, Integer> bannerCost = new HashMap<Banner, Integer>();

        bannerCost.put(new Banner("GREEN", 1), 2);
        bannerCost.put(new Banner("BLUE", 2), 1);

        Map<Resource, Integer> userResources = new HashMap<Resource, Integer>();

        userResources.put(Resource.SHIELD, 2);
        userResources.put(Resource.SERVANT, 3);

        Map<Banner, Integer> userBanners = new HashMap<Banner, Integer>();

        userBanners.put(new Banner("GREEN", 1), 2);
        userBanners.put(new Banner("BLUE", 2), 1);

        LeaderCard leaderCard = new LeaderCard(1, 5, resourceCost, bannerCost, null);

        Assert.assertEquals(true, leaderCard.isPlayable(userResources, userBanners));
    }*/
}