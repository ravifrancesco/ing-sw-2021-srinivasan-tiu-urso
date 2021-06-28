package it.polimi.ingsw.model;


import it.polimi.ingsw.model.full.marbles.*;
import it.polimi.ingsw.model.full.table.Market;
import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.full.table.VaticanReport;
import it.polimi.ingsw.model.utils.GameSettings;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MarketTest {
    GameSettings gs;
    public void setGs () {
        int[] faithTrackVictoryPoints = {0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,0};

        List<VaticanReport> vaticanReports = new ArrayList<>();

        vaticanReports.add(new VaticanReport(5,8,2));
        vaticanReports.add(new VaticanReport(12,15,2));
        vaticanReports.add(new VaticanReport(19,24,4));


        gs = new GameSettings(null, 0, null, null, vaticanReports, faithTrackVictoryPoints);

    }


    @Test
    public void resetTest() {
        setGs();
        Market market = new Market();
        Player p = new Player(gs, "test");

        Assert.assertEquals(3, Market.gridRowLength);
        Assert.assertEquals(4, Market.gridColLength);

        ArrayList<Marble> alMarble = new ArrayList<>(Arrays.asList(market.getMarblesGrid()));

        Map<String, Long> dest = alMarble.stream().collect(Collectors.groupingBy(m -> m.getClass().toString(), Collectors.counting()));


        Assert.assertEquals(Long.valueOf(2), dest.get("class it.polimi.ingsw.model.full.marbles.YellowMarble"));
        Assert.assertEquals(Long.valueOf(2), dest.get("class it.polimi.ingsw.model.full.marbles.GreyMarble"));
        Assert.assertEquals(Long.valueOf(1), dest.get("class it.polimi.ingsw.model.full.marbles.RedMarble"));
        Assert.assertEquals(Long.valueOf(2), dest.get("class it.polimi.ingsw.model.full.marbles.PurpleMarble"));
        Assert.assertEquals(Long.valueOf(2), dest.get("class it.polimi.ingsw.model.full.marbles.BlueMarble"));
        Assert.assertEquals(Long.valueOf(4), dest.get("class it.polimi.ingsw.model.full.marbles.WhiteMarble"));


    }

    @Test
    public void getResourcesTest() {
        setGs();
        Market market = new Market();
        Player p = new Player(gs, "test");

        market.reset();


        for(int possibleMove = 0; possibleMove < 7; possibleMove++) {
            ArrayList<Resource> testResList = new ArrayList<>();
            Map<Resource, Long> testRes;

            ArrayList<Resource> actualResList;
            Map<Resource, Long> actualRes;

            int pM = possibleMove;
            if(possibleMove < 3) {
                // row move
                IntStream.range(0, Market.gridColLength).forEach(i -> testResList.add(market.getMarble(pM, i).getResource(p)));
                actualResList = market.getResources(pM, p);
            } else {
                // col move
                IntStream.range(0, Market.gridRowLength).forEach(i -> testResList.add(market.getMarble(i, pM-3).getResource(p)));
                actualResList = market.getResources(pM, p);
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

    @Test
    public void getFreeMarble() {
        Market m = new Market();
        Marble mar = m.getFreeMarble();
        int isCorrectType = 0;

        if(mar instanceof WhiteMarble) {
            isCorrectType = 1;
        } else if (mar instanceof RedMarble) {
            isCorrectType = 1;
        } else if (mar instanceof BlueMarble) {
            isCorrectType = 1;
        } else if (mar instanceof PurpleMarble) {
            isCorrectType = 1;
        } else if (mar instanceof  GreyMarble) {
            isCorrectType = 1;
        } else if (mar instanceof  YellowMarble) {
            isCorrectType = 1;
        }

        Assert.assertEquals(isCorrectType, 1);
    }




}
