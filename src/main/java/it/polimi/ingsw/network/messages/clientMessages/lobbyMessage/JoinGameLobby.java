package it.polimi.ingsw.network.messages.clientMessages.lobbyMessage;

import it.polimi.ingsw.network.server.lobby.Lobby;
import it.polimi.ingsw.network.server.lobby.MainLobby;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import javax.naming.InvalidNameException;
import java.io.Serializable;

public class JoinGameLobby extends ClientLobbyMessage implements Serializable {

    String gameID;

    public JoinGameLobby(String gameID) {
        this.gameID = gameID;
    }

    @Override
    public void handle(ServerVirtualView serverVirtualView, Lobby lobby) throws InvalidNameException {
        ((MainLobby) lobby).joinGame(serverVirtualView, gameID);
    }
}
