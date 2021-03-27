package it.polimi.ingsw.model;


import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MarketTest {

    @Test
    public void resetTest() {
        Market market = new Market();
        Player p = new Player("test", "0");

        market.reset();

        Assert.assertEquals(3, market.getGridRowLenght());
        Assert.assertEquals(4, market.getGridColLength());

        ArrayList<Marble> alMarble = new ArrayList<>(Arrays.asList(market.getMarblesGrid()));

        Map<Integer, Long> dest = alMarble.stream().collect(Collectors.groupingBy(Marble::getId, Collectors.counting()));

        System.out.println("Test produced:");
        dest.forEach((key, val) -> System.out.println("Marble: " + key + " Qty: " + val));

        Assert.assertEquals(2, Math.toIntExact(dest.get(1))); // grey
        Assert.assertEquals(2, Math.toIntExact(dest.get(2))); // purple
        Assert.assertEquals(2, Math.toIntExact(dest.get(3))); // yellow
        Assert.assertEquals(2, Math.toIntExact(dest.get(4))); // blue
        Assert.assertEquals(1, Math.toIntExact(dest.get(5))); // red
        Assert.assertEquals(4, Math.toIntExact(dest.get(6))); // white

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
                IntStream.range(0, market.getGridRowLenght()).forEach(i -> testResList.add(market.getMarble(i, pM-3).getResource(p)));
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

}
