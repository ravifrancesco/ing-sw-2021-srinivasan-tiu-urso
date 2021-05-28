package it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby;

import it.polimi.ingsw.model.GameSettings;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.Lobby;
import it.polimi.ingsw.server.lobby.MainLobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.ClientLobbyMessage;

import javax.naming.InvalidNameException;
import java.io.Serializable;

public class CreateGameLobby extends ClientLobbyMessage implements Serializable {

    int numberOfPlayers;

    public CreateGameLobby(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    @Override
    public void handle(Connection connection, Lobby lobby) {
        try {
            ((MainLobby) lobby).createGame(connection, numberOfPlayers);
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
    }
}
