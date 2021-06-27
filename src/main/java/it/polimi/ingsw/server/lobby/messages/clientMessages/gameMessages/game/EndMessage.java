package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.client.SinglePlayerView;
import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class EndMessage extends ClientGameMessage implements Serializable {

    @Override
    public void handle(Connection c, ServerController serverController) {
        serverController.end(c.getNickname(), 0);
    }

    @Override
    public void handleLocally(SinglePlayerView singlePlayerView, ServerController serverController) {
        serverController.end(singlePlayerView.getNickname(), 0);
    }
}
