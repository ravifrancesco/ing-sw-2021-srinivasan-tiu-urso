package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.client.SinglePlayerView;
import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class PlayMessage extends ClientGameMessage implements Serializable {
    private int index;

    public PlayMessage(int index) {
        this.index = index;
    }

    @Override
    public void handle(Connection connection, ServerController serverController) {
        serverController.play(connection.getNickname(), index);
    }

    @Override
    public void handleLocally(SinglePlayerView singlePlayerView, ServerController serverController) {
        serverController.play(singlePlayerView.getNickname(), index);
    }
}
