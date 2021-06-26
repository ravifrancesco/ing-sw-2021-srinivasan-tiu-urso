package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.ResourceContainer;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class PlayLeaderCardGameMessage extends ClientGameMessage implements Serializable {

    int cardToPlay;
    ResourceContainer resourceContainer;

    public PlayLeaderCardGameMessage(int cardToPlay, ResourceContainer resourceContainer) {
        this.cardToPlay = cardToPlay;
        this.resourceContainer = resourceContainer;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        int output;
        output = serverController.playLeaderCard(c.getNickname(), cardToPlay, resourceContainer);
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Card with index " + cardToPlay + " has been placed correctly!");
        }
    }
}
