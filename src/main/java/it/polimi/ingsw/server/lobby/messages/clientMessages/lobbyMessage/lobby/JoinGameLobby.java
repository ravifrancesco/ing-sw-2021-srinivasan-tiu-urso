package it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby;

import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.Lobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.ClientLobbyMessage;

import java.io.Serializable;

public class JoinGameLobby extends ClientLobbyMessage implements Serializable {
    @Override
    public void handle(Connection connection, Lobby lobby) {

    }
}
