package it.polimi.ingsw.server.lobby.messages.clientMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;


public class PlayerDiscardsExcessLeaderCards implements ClientMessage {
    private int cardToDiscard;

    public PlayerDiscardsExcessLeaderCards(int cardToDiscard) {
        this.cardToDiscard = cardToDiscard;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        try {
            serverController.discardExcessLeaderCards(c.getNickname(), cardToDiscard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
