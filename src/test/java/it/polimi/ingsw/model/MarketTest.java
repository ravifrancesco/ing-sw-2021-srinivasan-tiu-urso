package it.polimi.ingsw.model;


import it.polimi.ingsw.model.marbles.Marble;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MarketTest {
    /*

    @Test
    public void resetTest() {
        Market market = new Market();
        Player p = new Player("test", "0");

        market.reset();



        Assert.assertEquals(3, market.getGridRowLength());
        Assert.assertEquals(4, market.getGridColLength());

        ArrayList<Marble> alMarble = new ArrayList<>(Arrays.asList(market.getMarblesGrid()));

        Map<String, Long> dest = alMarble.stream().collect(Collectors.groupingBy(m -> m.getClass().toString(), Collectors.counting()));

        System.out.println("Test produced:");
        dest.forEach((key, val) -> System.out.println("Marble: " + key + " Qty: " + val));

        Assert.assertEquals(Long.valueOf(2), dest.get("class it.polimi.ingsw.model.marbles.YellowMarble"));
        Assert.assertEquals(Long.valueOf(2), dest.get("class it.polimi.ingsw.model.marbles.GreyMarble"));
        Assert.assertEquals(Long.valueOf(1), dest.get("class it.polimi.ingsw.model.marbles.RedMarble"));
        Assert.assertEquals(Long.valueOf(2), dest.get("class it.polimi.ingsw.model.marbles.PurpleMarble"));
        Assert.assertEquals(Long.valueOf(2), dest.get("class it.polimi.ingsw.model.marbles.BlueMarble"));
        Assert.assertEquals(Long.valueOf(4), dest.get("class it.polimi.ingsw.model.marbles.WhiteMarble"));


    }

    @Test
    public void getResourcesTest() {
        Market market = new Market();
        Player p = new Player("test", "0");

        market.reset();

        ArrayList<Marble> alMarble = new ArrayList<>(Arrays.asList(market.getMarblesGrid()));

        for(int possibleMove = 0; possibleMove < 7; possibleMove++) {
            ArrayList<Resource> testResList = new ArrayList<>();
            Map<Resource, Long> testRes = new HashMap<>();

            ArrayList<Resource> actualResList = new ArrayList<>();
            Map<Resource, Long> actualRes = new HashMap<>();

            int pM = possibleMove;
            if(possibleMove < 3) {
                // row move
                IntStream.range(0, market.getGridColLength()).forEach(i -> testResList.add(market.getMarble(pM, i).getResource(p)));
                actualResList = (ArrayList<Resource>) market.getResources(pM, p);
            } else {
                // col move
                IntStream.range(0, market.getGridRowLength()).forEach(i -> testResList.add(market.getMarble(i, pM-3).getResource(p)));
                actualResList = (ArrayList<Resource>) market.getResources(pM, p);
            }

            testRes = testResList.stream().filter(r -> r != null)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            actualRes = actualResList.stream().filter(r -> r != null)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            System.out.println("Test produced:");
            testRes.forEach((key, val) -> System.out.println("Resource: " + key + " Qty: " + val));

            System.out.println("Game produced:");
            actualRes.forEach((key, val) -> System.out.println("Resource: " + key + " Qty: " + val));

            Assert.assertEquals(testRes.get(Resource.STONE), actualRes.get(Resource.STONE));
            Assert.assertEquals(testRes.get(Resource.GOLD), actualRes.get(Resource.GOLD));
            Assert.assertEquals(testRes.get(Resource.SERVANT), actualRes.get(Resource.SERVANT));
            Assert.assertEquals(testRes.get(Resource.SHIELD), actualRes.get(Resource.SHIELD));

        }
    }

     */

}
