package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.GameSettings;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

public class LoadGameSettingsGameMessage extends ClientGameMessage {

    private GameSettings gameSettings;

    public LoadGameSettingsGameMessage(GameSettings gameSettings) {
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
