package it.polimi.ingsw.network.messages.clientMessages.lobbyMessage;

import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;
import it.polimi.ingsw.network.server.lobby.Lobby;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;

import javax.naming.InvalidNameException;

public abstract class ClientLobbyMessage extends ClientMessage {

    public abstract void handle(ServerVirtualView serverVirtualView, Lobby lobby) throws InvalidNameException;
}
