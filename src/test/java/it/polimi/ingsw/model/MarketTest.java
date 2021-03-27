package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MarketTest {

    @Test
    public void resetTest() {
        Market market = new Market();
        market.reset();

        Assert.assertEquals(3, market.getGridRowLenght());
        Assert.assertEquals(4, market.getGridColLength());

        int greyCount = 0;
        int purpleCount = 0;
        int yellowCount = 0;
        int blueCount = 0;
        int redCount = 0;
        int whiteCount = 0;

        int i = 0;
        int j = 0;
        for(i = 0; i < market.getGridRowLenght(); i++) {
            for(j = 0; j < market.getGridColLength(); j++) {
                if ((market.getMarble(i,j) instanceof WhiteMarble)&&
                        (market.getMarble(i,j) != null)) {
                    System.out.println("white");
                    whiteCount++;
                }

                if ((market.getMarble(i,j) instanceof GreyMarble)&&
                        (market.getMarble(i,j) != null)) {
                    System.out.println("grey");
                    greyCount++;
                }


                if ((market.getMarble(i,j) instanceof PurpleMarble)&&
                        (market.getMarble(i,j) != null)) {
                    System.out.println("purple");
                    purpleCount++;
                }


                if ((market.getMarble(i,j) instanceof YellowMarble)&&
                        (market.getMarble(i,j) != null)) {
                    System.out.println("yellow");
                    yellowCount++;
                }

                if ((market.getMarble(i,j) instanceof BlueMarble)&&
                        (market.getMarble(i,j) != null)) {
                    System.out.println("blue");
                    blueCount++;
                }

                if ((market.getMarble(i,j) instanceof RedMarble)&&
                        (market.getMarble(i,j) != null)) {
                    System.out.println("red");
                    redCount++;
                }
            }
        }

        // forse dovevo usare un po' di functional
        if(market.getFreeMarble() instanceof WhiteMarble && market.getFreeMarble() != null) {
            whiteCount++;
        }
        if(market.getFreeMarble() instanceof GreyMarble && market.getFreeMarble() != null) {
            greyCount++;
        }
        if(market.getFreeMarble() instanceof PurpleMarble && market.getFreeMarble() != null) {
            purpleCount++;
        }
        if(market.getFreeMarble() instanceof YellowMarble && market.getFreeMarble() != null) {
            yellowCount++;
        }
        if(market.getFreeMarble() instanceof BlueMarble && market.getFreeMarble() != null) {
            blueCount++;
        }
        if(market.getFreeMarble() instanceof RedMarble && market.getFreeMarble() != null) {
            redCount++;
        }

        Assert.assertEquals(4, whiteCount);
        Assert.assertEquals(2, greyCount);
        Assert.assertEquals(2, purpleCount);
        Assert.assertEquals(2, yellowCount);
        Assert.assertEquals(2, blueCount);
        Assert.assertEquals(1, redCount);

    }


    @Test
    public void getResourcesTest() {
        Market market = new Market();
        market.reset();
        Player p = new Player();
        int row;

        Collection<Resource> returnedRes;

        int greyCount = 0;
        int purpleCount = 0;
        int yellowCount = 0;
        int blueCount = 0;
        int redCount = 0;
        int whiteCount = 0;
        int move;
        int t;

        for(int i = 0; i < 7; i++) { // tries every possible move
            // ArrayList<Marble> alGrid = new ArrayList<Marble>(Arrays.asList(market.getMarblesGrid()));

            //Map<Resource, Long> result = alGrid.stream().collect(Collectors.groupingBy(Marble::getResource));


            // Map<Marble, Integer> temp = Arrays.stream(market.getMarblesGrid()).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            System.out.println("Testing move " + i);
            if(isRowMove(i)) {
                for(int j = 0; j < 4; j++) {
                    System.out.println("-calling " + i + " " + j );
                    Marble curr = market.getMarble(i, j);;
                    if(curr instanceof BlueMarble) {
                        blueCount++;
                    }
                    if(curr instanceof PurpleMarble) {
                        purpleCount++;
                    }
                    if(curr instanceof GreyMarble) {
                        greyCount++;
                    }
                    if(curr instanceof YellowMarble) {
                        yellowCount++;
                    }
                }
            } else {
                t = i - 3;
                for(int j = 0; j < 3; j++) {
                    System.out.println("&calling " + t + " " + j );
                    Marble curr = market.getMarble(j, t);;
                    if(curr instanceof BlueMarble) {
                        blueCount++;
                    }
                    if(curr instanceof PurpleMarble) {
                        purpleCount++;
                    }
                    if(curr instanceof GreyMarble) {
                        greyCount++;
                    }
                    if(curr instanceof YellowMarble) {
                        yellowCount++;
                    }
                }
            }


            System.out.println("Test -- STONE: " + greyCount + " SERVANT: " + purpleCount + " SHIELD: " + blueCount + " GOLD: " + yellowCount);



            returnedRes = market.getResources(i, p);
            Assert.assertEquals(yellowCount, returnedRes.stream().filter(r -> r == Resource.GOLD).count());
            Assert.assertEquals(purpleCount, returnedRes.stream().filter(r -> r == Resource.SERVANT).count());
            Assert.assertEquals(blueCount, returnedRes.stream().filter(r -> r == Resource.SHIELD).count());
            Assert.assertEquals(greyCount, returnedRes.stream().filter(r -> r == Resource.STONE).count());

            blueCount = 0;
            purpleCount = 0;
            greyCount = 0;
            yellowCount = 0;
        }
    }

    public boolean isRowMove(int moveIndex) {
        return moveIndex < 3;
    }







}
