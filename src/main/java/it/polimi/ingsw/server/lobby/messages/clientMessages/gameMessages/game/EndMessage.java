package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class EndMessage extends ClientGameMessage implements Serializable {

    @Override
    public void handle(Connection connection, ServerController serverController) {
        serverController.end(connection.getNickname(), 0);
    }
}
