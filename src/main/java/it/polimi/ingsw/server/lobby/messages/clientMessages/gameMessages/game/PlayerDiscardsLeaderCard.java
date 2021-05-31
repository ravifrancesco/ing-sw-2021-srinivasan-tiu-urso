package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class PlayerDiscardsLeaderCard extends ClientGameMessage implements Serializable {
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
