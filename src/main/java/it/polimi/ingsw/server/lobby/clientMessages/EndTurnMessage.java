package it.polimi.ingsw.server.lobby.clientMessages;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.clientMessages.ClientMessage;

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
