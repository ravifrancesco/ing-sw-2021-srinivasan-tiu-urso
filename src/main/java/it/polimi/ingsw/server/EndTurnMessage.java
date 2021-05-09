package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.ServerController;

public class EndTurnMessage implements ClientMessage {

    @Override
    public void handle(Connection c, ServerController serverController) {
        try {
            serverController.endTurn(c.getNickname());
        } catch (Exception e) {
            // TODO
        }
    }
}
