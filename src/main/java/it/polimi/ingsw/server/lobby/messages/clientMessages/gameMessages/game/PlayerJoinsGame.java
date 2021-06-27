package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.client.SinglePlayerView;
import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;


public class PlayerJoinsGame extends ClientGameMessage implements Serializable {
    @Override
    public void handle(Connection c, ServerController serverController) {
        try {
            serverController.joinGame(c.getNickname());
        } catch (Exception e) {
            c.sendFailedMoveMessage("Failed connectiong to the lobby: " + e.getMessage());
        }
    }

    @Override
    public void handleLocally(SinglePlayerView singlePlayerView, ServerController serverController) {
        try {
            serverController.joinGame(singlePlayerView.getNickname());
        } catch (Exception e) {
            singlePlayerView.printSuccessfulMove("Failed connectiong to the lobby: " + e.getMessage());
        }
    }
}
