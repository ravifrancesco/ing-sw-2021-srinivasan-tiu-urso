package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.client.SinglePlayerView;
import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class UnlimitedResourcesMessage extends ClientGameMessage implements Serializable {
    @Override
    public void handle(Connection connection, ServerController serverController) {
        serverController.hack(connection.getNickname());
    }

    @Override
    public void handleLocally(SinglePlayerView singlePlayerView, ServerController serverController) {
        serverController.hack(singlePlayerView.getNickname());
    }
}
