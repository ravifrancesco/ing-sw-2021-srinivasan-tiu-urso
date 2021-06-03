package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;


public class PlayerDiscardsExcessLeaderCards extends ClientGameMessage implements Serializable {
    private int cardToDiscard;

    public PlayerDiscardsExcessLeaderCards(int cardToDiscard) {
        this.cardToDiscard = cardToDiscard;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        int output = serverController.discardExcessLeaderCards(c.getNickname(), cardToDiscard);
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Card with index " + cardToDiscard + " has been discarded successfully");
        }
    }
}
