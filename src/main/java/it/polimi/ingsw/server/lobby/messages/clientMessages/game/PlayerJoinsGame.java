package it.polimi.ingsw.server.lobby.messages.clientMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;


public class PlayerJoinsGame implements ClientMessage {
    @Override
    public void handle(Connection c, ServerController serverController) {
        try {
            serverController.joinGame(c.getNickname());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
