package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.GameFullException;
import it.polimi.ingsw.controller.exceptions.WrongTurnException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.GameSettingsBuilder;
import org.junit.Assert;
import org.junit.Test;

import javax.naming.InvalidNameException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class WarehouseControllerTest {
    private ServerController serverController;


    public void init() {
        this.serverController = new ServerController("01", 3);
        GameSettingsBuilder gameSettingsBuilder = new GameSettingsBuilder();
        GameSettings gameSettings = gameSettingsBuilder.build();
        serverController.loadGameSettings(gameSettings);
        try {
            serverController.joinGame("rbta-svg");
            serverController.joinGame("ravifrancesco");
            serverController.joinGame("giuseppeurso");
        } catch (InvalidNameException | GameFullException e) {
            e.printStackTrace();
        }
        serverController.startGame("randomname");
        serverController.discardExcessLeaderCards("rbta-svg", 0);
        serverController.discardExcessLeaderCards("rbta-svg", 0);
        serverController.endTurn("rbta-svg");
        serverController.discardExcessLeaderCards("ravifrancesco", 0);
        serverController.discardExcessLeaderCards("ravifrancesco", 0);
        serverController.getInitialResources("ravifrancesco", Resource.GOLD, 0);
        serverController.endTurn("ravifrancesco");
        serverController.discardExcessLeaderCards("giuseppeurso", 0);
        serverController.discardExcessLeaderCards("giuseppeurso", 0);
        serverController.getInitialResources("giuseppeurso", Resource.STONE, 0);
        serverController.endTurn("giuseppeurso");



    }



    @Test
    public void changeResourcesDepositTest() {
        init();
        int thrownException = 0;
        Resource[] resourcesDeposit = new Resource[6];
        Assert.assertEquals(serverController.changeDeposit("ravifrancesco", resourcesDeposit), -1);
        serverController.endTurn("rbta-svg");
        // ravifrancesco has 1 gold from the initial phase, placed in deposit 0
        // trying to change position
        resourcesDeposit[1] = Resource.GOLD;
        Assert.assertEquals(serverController.getGameStatus().getPlayers().get(serverController.getCurrentPlayer()).getDashboard().getWarehouse().getDeposit()[0], Resource.GOLD);
        IntStream.range(1, 6).forEach(i -> Assert.assertNull(serverController.getGameStatus().getPlayers().get(serverController.getCurrentPlayer()).getDashboard().getWarehouse().getDeposit()[i]));
        serverController.changeDeposit("ravifrancesco", resourcesDeposit);
        Assert.assertEquals(serverController.getGameStatus().getPlayers().get(serverController.getCurrentPlayer()).getDashboard().getWarehouse().getDeposit()[1], Resource.GOLD);
        IntStream.range(1, 6).filter(i -> i != 1).forEach(i -> Assert.assertNull(serverController.getGameStatus().getPlayers().get(serverController.getCurrentPlayer()).getDashboard().getWarehouse().getDeposit()[i]));
    }

    public void changeResourcesDepositExtraDepositTest() {
        init();
    }



    }


