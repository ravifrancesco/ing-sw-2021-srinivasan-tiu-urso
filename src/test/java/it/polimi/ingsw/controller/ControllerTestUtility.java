package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.GameFullException;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.utils.GameSettings;
import it.polimi.ingsw.utils.GameSettingsBuilder;

import javax.naming.InvalidNameException;

public class ControllerTestUtility {

    public static Controller init() {
        Controller controller = new Controller("01", 3);
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
        return controller;
    }

    public static Controller initWithoutDiscarding() {
        Controller controller = new Controller("01", 3);
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
        return controller;
    }

}
