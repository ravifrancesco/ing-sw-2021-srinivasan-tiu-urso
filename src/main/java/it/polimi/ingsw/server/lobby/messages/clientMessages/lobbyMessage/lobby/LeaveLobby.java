package it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.GameLobby;
import it.polimi.ingsw.server.lobby.Lobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class LeaveLobby extends ClientGameMessage implements Serializable {
    @Override
    public void handle(Connection connection, ServerController serverController) {
        ((GameLobby) connection.getCurrentLobby()).leaveLobby(connection);
        connection.enterLobby(connection.getMainLobby());
    }
}
