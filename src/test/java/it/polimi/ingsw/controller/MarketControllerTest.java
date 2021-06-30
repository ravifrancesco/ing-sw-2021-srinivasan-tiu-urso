package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.full.marbles.Marble;
import it.polimi.ingsw.model.full.table.Resource;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class MarketControllerTest {

    static Controller controller;

    @BeforeClass
    public static void init() {
        controller = ControllerTestUtility.init();
    }

    @Test
    public void testGetFromMarket() {

        Marble[] marblesGrid = controller.getGameStatus().getGameBoard().getMarket().getMarblesGrid();
        controller.getFromMarket("giuseppeurso", 0, new ArrayList<>());
        Assert.assertEquals(marblesGrid, controller.getGameStatus().getGameBoard().getMarket().getMarblesGrid());
        ArrayList<Resource> fakeWMR = new ArrayList<>();
        fakeWMR.add(Resource.SHIELD);
        controller.getFromMarket("rbta-svg", 0, fakeWMR);
        Assert.assertEquals(marblesGrid, controller.getGameStatus().getGameBoard().getMarket().getMarblesGrid());
        controller.getFromMarket("rbta-svg", 12, new ArrayList<>());
        Assert.assertEquals(marblesGrid, controller.getGameStatus().getGameBoard().getMarket().getMarblesGrid());

    }

}
