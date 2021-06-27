package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.client.SinglePlayerView;
import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.ResourceContainer;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class PlayLeaderCardGameMessage extends ClientGameMessage implements Serializable {

    int cardToPlay;

    public PlayLeaderCardGameMessage(int cardToPlay) {
        this.cardToPlay = cardToPlay;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        int output;
        output = serverController.playLeaderCard(c.getNickname(), cardToPlay);
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Card with index " + cardToPlay + " has been placed correctly!");
        }
    }

    @Override
    public void handleLocally(SinglePlayerView singlePlayerView, ServerController serverController) {
        int output;
        output = serverController.playLeaderCard(singlePlayerView.getNickname(), cardToPlay);
        if (output == 0) {
            singlePlayerView.printSuccessfulMove("Card with index " + cardToPlay + " has been placed correctly!");
        }
    }
}
