package it.polimi.ingsw.server.lobby;

import it.polimi.ingsw.server.Connection;

import javax.naming.InvalidNameException;

public interface Lobby {

    void handleMessage(String msg, Connection c);

    void enterLobby(Connection c) throws InvalidNameException, IllegalStateException;

}
