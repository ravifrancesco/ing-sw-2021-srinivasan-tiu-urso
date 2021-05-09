package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.ServerController;

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
