package it.polimi.ingsw.server.lobby.messages.clientMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;

public class PlayLeaderCardMessage implements ClientMessage {

    int cardToPlay;

    public PlayLeaderCardMessage(int cardToPlay) {
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
