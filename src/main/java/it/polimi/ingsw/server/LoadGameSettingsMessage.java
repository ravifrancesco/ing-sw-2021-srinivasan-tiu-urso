package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.GameSettings;

public class LoadGameSettingsMessage implements ClientMessage {

    private GameSettings gameSettings;

    public LoadGameSettingsMessage(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        try {
            serverController.loadGameSettings(gameSettings);
        } catch (Exception e) {
            // TODO
        }
    }
}
