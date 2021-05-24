package it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby;

import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.Lobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.ClientLobbyMessage;

import javax.naming.InvalidNameException;
import java.io.IOException;
import java.io.Serializable;

public class QuitGame extends ClientLobbyMessage implements Serializable {
    @Override
    public void handle(Connection connection, Lobby lobby) throws InvalidNameException {
        // TODO works if uncommented but calls quit() 3 times and doesn't kill reading thread
        // 1 time from call here
        // 1 time because catches IOException when sending CorrectHandlingMessage
        // 1 time in the "finally" whilst running the connection
        // connection.close();
    }
}
