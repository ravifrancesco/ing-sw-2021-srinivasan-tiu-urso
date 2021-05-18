package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.GameFullException;
import it.polimi.ingsw.controller.exceptions.IllegalDepositStateException;
import it.polimi.ingsw.controller.exceptions.WrongMoveException;
import it.polimi.ingsw.controller.exceptions.WrongTurnException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameSettings;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utils.GameSettingsBuilder;
import org.junit.Assert;
import org.junit.Test;

import javax.naming.InvalidNameException;
import java.util.Arrays;

public class WarehouseControllerTest {
    private Game game;
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
    }
    @Test
    public void setCurrentPlayerTest() {
        WarehouseController warehouseController = new WarehouseController(game);
        Assert.assertNull(warehouseController.getCurrentPlayer());
        warehouseController.setCurrentPlayer("rbta-svg");
        Assert.assertNotNull(warehouseController.getCurrentPlayer());
    }

    @Test
    public void changeResourcesDepositTest() {
        init();
        Resource[] testDeposit = new Resource[6];
        int thrownExceptions = 0;

        // wrong turn
        try {
            serverController.changeDeposit("ravifrancesco", testDeposit);
        } catch (WrongMoveException e) {
            e.printStackTrace();
        } catch (WrongTurnException e) {
            e.printStackTrace();
            thrownExceptions += 1;
        } catch (IllegalDepositStateException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(thrownExceptions, 1);

        // wrong deposit state
        testDeposit[0] = Resource.STONE;
        testDeposit[1] = Resource.STONE;
        try {
            serverController.changeDeposit("rbta-svg", testDeposit);
        } catch (WrongMoveException e) {
            e.printStackTrace();
        } catch (WrongTurnException e) {
            e.printStackTrace();
        } catch (IllegalDepositStateException e) {
            thrownExceptions += 1;
            e.printStackTrace();
        }
        Assert.assertEquals(thrownExceptions, 2);

        // wrong deposit state
        testDeposit[1] = Resource.SHIELD;
        Resource[] wrongDeposit = new Resource[7];
        try {
            serverController.changeDeposit("rbta-svg", wrongDeposit);
        } catch (WrongMoveException e) {
            thrownExceptions += 1;
            e.printStackTrace();
        } catch (WrongTurnException e) {
            e.printStackTrace();
        } catch (IllegalDepositStateException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(thrownExceptions, 3);

        // still an illegal move because you have to insert a deposit with the same resources from before
        try {
            serverController.changeDeposit("rbta-svg", testDeposit);
        } catch (WrongMoveException e) {
            e.printStackTrace();
        } catch (WrongTurnException e) {
            e.printStackTrace();
        } catch (IllegalDepositStateException e) {
            thrownExceptions += 1;
            e.printStackTrace();
        }
        Assert.assertEquals(thrownExceptions, 4);
        // TODO test with an actual filled deposit?
        System.out.println(Arrays.toString(game.getGameBoard().getMarket().getMarblesGrid()));



    }
}

