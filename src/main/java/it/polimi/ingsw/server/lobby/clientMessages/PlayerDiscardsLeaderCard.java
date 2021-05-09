package it.polimi.ingsw.server.lobby.clientMessages;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;

public class PlayerDiscardsLeaderCard implements ClientMessage {
    private int cardToDiscard;

    public PlayerDiscardsLeaderCard(int cardToDiscard) {
        this.cardToDiscard = cardToDiscard;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        try {
            serverController.discardLeaderCard(c.getNickname(), cardToDiscard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
