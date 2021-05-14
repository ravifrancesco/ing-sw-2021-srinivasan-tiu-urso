package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

public class PlayLeaderCardGameMessage extends ClientGameMessage {

    int cardToPlay;

    public PlayLeaderCardGameMessage(int cardToPlay) {
        this.cardToPlay = cardToPlay;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        try {
            serverController.playLeaderCard(c.getNickname(), cardToPlay);
        } catch (Exception e) {
            // TODO
        }
    }
}
