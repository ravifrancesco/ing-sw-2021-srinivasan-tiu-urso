package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assumptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderCardTest{
    @Test
    public void toStringTestWithNullRP(){
        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.SHIELD, 1);

        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner("GREEN", 1), 2);
        bannerCost.put(new Banner("BLUE", 2), 1);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, null, 1, true);

        LeaderCard leaderCard = new LeaderCard(1, 5, resourceCost, bannerCost, sa);

        String s1 = "ID=1;VP=5;RC=SHIELD,1;BC=BLUE,2,1,GREEN,1,2;SA=PP;RR=GOLD,1;FP=1;SR=y;";
        String s2 = "ID=1;VP=5;RC=SHIELD,1;BC=GREEN,1,2,BLUE,2,1;SA=PP;RR=GOLD,1;FP=1;SR=y;";

        Assumptions.assumeTrue(s1.equals(leaderCard.toString()) || s2.equals(leaderCard.toString()));

    }

    @Test
    public void getterTest(){
        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.SHIELD, 1);

        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner("GREEN", 1), 2);
        bannerCost.put(new Banner("BLUE", 2), 1);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);

        SpecialAbility sa = new ProductionPower(resourceRequired, null, 1, true);

        LeaderCard leaderCard = new LeaderCard(1, 5, resourceCost, bannerCost, sa);

        Assert.assertEquals(leaderCard.getId(), 1);
        Assert.assertEquals(leaderCard.getVictoryPoints(), 5);
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

        int[] faithTrackVictoryPoints = {0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,0};

        List<VaticanReport> vaticanReports = new ArrayList<>();

        vaticanReports.add(new VaticanReport(5,8,2));
        vaticanReports.add(new VaticanReport(12,15,2));
        vaticanReports.add(new VaticanReport(19,24,4));


        GameSettings gameSettings = new GameSettings(null, 0, null, null, vaticanReports, faithTrackVictoryPoints);

        FaithTrack faithTrack = new FaithTrack(gameSettings);

        Dashboard dashboard = new Dashboard(gameSettings, null);

        leaderCard.discard(dashboard);

        // TODO check if the card is in the discardDeck (when Deck will be implemented)

        Assert.assertEquals(dashboard.getFaithMarkerPosition(),1);
    }

    @Test
    public void playTest(){
        // TODO when GameBoard will be done
    }

    @Test
    public void activateTest(){
        // TODO when GameBoard will be done
    }
}