package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;


public class PlayerJoinsGame extends ClientGameMessage implements Serializable {
    @Override
    public void handle(Connection c, ServerController serverController) {
        try {
            serverController.joinGame(c.getNickname());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
