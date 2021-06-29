package it.polimi.ingsw.network.messages.clientMessages.lobbyMessage;

import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;
import it.polimi.ingsw.network.server.lobby.Lobby;

import javax.naming.InvalidNameException;
import java.io.Serializable;

public class QuitGame extends ClientLobbyMessage implements Serializable {
    @Override
    public void handle(ServerVirtualView serverVirtualView, Lobby lobby) throws InvalidNameException {

    }
}
