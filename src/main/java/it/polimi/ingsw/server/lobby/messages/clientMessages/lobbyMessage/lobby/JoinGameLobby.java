package it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby;

import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.Lobby;
import it.polimi.ingsw.server.lobby.MainLobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.ClientLobbyMessage;

import javax.naming.InvalidNameException;
import java.io.Serializable;

public class JoinGameLobby extends ClientLobbyMessage implements Serializable {

    String gameID;

    public JoinGameLobby(String gameID) {
        this.gameID = gameID;
    }

    @Override
    public void handle(Connection connection, Lobby lobby) throws InvalidNameException {
        ((MainLobby) lobby).joinGame(connection, gameID);
    }
}
