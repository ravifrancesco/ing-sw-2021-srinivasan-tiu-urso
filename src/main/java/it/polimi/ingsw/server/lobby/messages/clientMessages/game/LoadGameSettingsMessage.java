package it.polimi.ingsw.server.lobby.messages.clientMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.GameSettings;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;

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
