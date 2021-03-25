package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

import java.awt.image.RescaleOp;

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

}
