package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.GameFullException;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.utils.GameSettings;
import it.polimi.ingsw.utils.GameSettingsBuilder;
import org.junit.Assert;
import org.junit.Test;

import javax.naming.InvalidNameException;
import java.util.stream.IntStream;

public class WarehouseControllerTest {
    private Controller controller;


    public void init() {
        this.controller = new Controller("01", 3);
        GameSettingsBuilder gameSettingsBuilder = new GameSettingsBuilder();
        GameSettings gameSettings = gameSettingsBuilder.build();
        controller.loadGameSettings(gameSettings);
        try {
            controller.joinGame("rbta-svg");
            controller.joinGame("ravifrancesco");
            controller.joinGame("giuseppeurso");
        } catch (InvalidNameException | GameFullException e) {
            e.printStackTrace();
        }
        controller.startGame("randomname");
        controller.discardExcessLeaderCards("rbta-svg", 0);
        controller.discardExcessLeaderCards("rbta-svg", 0);
        controller.endTurn("rbta-svg");
        controller.discardExcessLeaderCards("ravifrancesco", 0);
        controller.discardExcessLeaderCards("ravifrancesco", 0);
        controller.getInitialResources("ravifrancesco", Resource.GOLD, 0);
        controller.endTurn("ravifrancesco");
        controller.discardExcessLeaderCards("giuseppeurso", 0);
        controller.discardExcessLeaderCards("giuseppeurso", 0);
        controller.getInitialResources("giuseppeurso", Resource.STONE, 0);
        controller.endTurn("giuseppeurso");



    }



    @Test
    public void changeResourcesDepositTest() {
        init();
        int thrownException = 0;
        Resource[] resourcesDeposit = new Resource[6];
        Assert.assertEquals(controller.changeDeposit("ravifrancesco", resourcesDeposit), -1);
        controller.endTurn("rbta-svg");
        // ravifrancesco has 1 gold from the initial phase, placed in deposit 0
        // trying to change position
        resourcesDeposit[1] = Resource.GOLD;
        Assert.assertEquals(controller.getGameStatus().getPlayers().get(controller.getCurrentPlayer()).getDashboard().getWarehouse().getDeposit()[0], Resource.GOLD);
        IntStream.range(1, 6).forEach(i -> Assert.assertNull(controller.getGameStatus().getPlayers().get(controller.getCurrentPlayer()).getDashboard().getWarehouse().getDeposit()[i]));
        controller.changeDeposit("ravifrancesco", resourcesDeposit);
        Assert.assertEquals(controller.getGameStatus().getPlayers().get(controller.getCurrentPlayer()).getDashboard().getWarehouse().getDeposit()[1], Resource.GOLD);
        IntStream.range(1, 6).filter(i -> i != 1).forEach(i -> Assert.assertNull(controller.getGameStatus().getPlayers().get(controller.getCurrentPlayer()).getDashboard().getWarehouse().getDeposit()[i]));
    }

    public void changeResourcesDepositExtraDepositTest() {
        init();
    }



    }


