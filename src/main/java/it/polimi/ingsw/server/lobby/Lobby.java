package it.polimi.ingsw.server.lobby;

import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;

import javax.naming.InvalidNameException;

public interface Lobby {

    void handleMessage(ClientMessage clientMessage, Connection c);

    void enterLobby(Connection connection) throws InvalidNameException, IllegalStateException;

    LobbyType getType();
}
