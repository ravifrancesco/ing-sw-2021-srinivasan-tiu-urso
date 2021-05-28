package it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby;

import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.Lobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.ClientLobbyMessage;

import javax.naming.InvalidNameException;
import java.io.Serializable;

public class AskGameLobbies extends ClientLobbyMessage implements Serializable {

    @Override
    public void handle(Connection connection, Lobby lobby) throws InvalidNameException {
        connection.sendGameLobbiesDetails();
    }
}
