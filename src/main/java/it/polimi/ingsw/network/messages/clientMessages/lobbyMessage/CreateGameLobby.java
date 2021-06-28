package it.polimi.ingsw.network.messages.clientMessages.lobbyMessage;

import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;
import it.polimi.ingsw.network.server.lobby.Lobby;
import it.polimi.ingsw.network.server.lobby.MainLobby;

import javax.naming.InvalidNameException;
import java.io.Serializable;

public class CreateGameLobby extends ClientLobbyMessage implements Serializable {

    int numberOfPlayers;

    public CreateGameLobby(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    @Override
    public void handle(ServerVirtualView serverVirtualView, Lobby lobby) {
        try {
            ((MainLobby) lobby).createGame(serverVirtualView, numberOfPlayers);
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
    }
}
