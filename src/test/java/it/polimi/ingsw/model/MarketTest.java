package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

public class MarketTest {

    @Test
    public void initTest() {
        Market market = new Market();
        market.init();

        Assert.assertEquals(3, market.getGridRowLenght());
        Assert.assertEquals(4, market.getGridColLength());

        int greyCount = 0;
        int purpleCount = 0;
        int yellowCount = 0;
        int blueCount = 0;
        int redCount = 0;
        int whiteCount = 0;


        for(int i = 0; i < market.getGridRowLenght(); i++) {
            for(int j = 0; j < market.getGridColLength(); j++) {
                if ((market.getMarblesGrid())[i][j] instanceof WhiteMarble &&
                        (market.getMarblesGrid())[i][j] != null) {
                    System.out.println("white");
                    whiteCount++;
                }

                if ((market.getMarblesGrid())[i][j] instanceof GreyMarble &&
                        (market.getMarblesGrid())[i][j] != null) {
                    System.out.println("grey");
                    greyCount++;
                }

                if ((market.getMarblesGrid())[i][j] instanceof PurpleMarble &&
                        (market.getMarblesGrid())[i][j] != null) {
                    System.out.println("purple");
                    purpleCount++;
                }

                if ((market.getMarblesGrid())[i][j] instanceof YellowMarble &&
                        (market.getMarblesGrid())[i][j] != null) {
                    System.out.println("yellow");
                    yellowCount++;
                }

                if ((market.getMarblesGrid())[i][j] instanceof BlueMarble &&
                        (market.getMarblesGrid())[i][j] != null) {
                    System.out.println("blue");
                    blueCount++;
                }

                if ((market.getMarblesGrid())[i][j] instanceof RedMarble &&
                        (market.getMarblesGrid())[i][j] != null) {
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
        market.init();
        Player p = new Player();
        int row;

        Collection<Resource> returnedRes;

        int greyCount = 0;
        int purpleCount = 0;
        int yellowCount = 0;
        int blueCount = 0;
        int redCount = 0;
        int whiteCount = 0;

        for(int i = 1; i < 8; i++) { // tries every possible move
            if(isRowMove(i)) {
                row = i - 1;
                for(int j = 0; j < 4; j++) {
                    Marble curr = market.getMarblesGrid()[row][j];
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
                row = i - 4;
                for(int j = 0; j < 3; j++) {
                    Marble curr = market.getMarblesGrid()[j][row];
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
        return moveIndex < 4;
    }
}
