package it.polimi.ingsw.server.lobby.clientMessages;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;


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
