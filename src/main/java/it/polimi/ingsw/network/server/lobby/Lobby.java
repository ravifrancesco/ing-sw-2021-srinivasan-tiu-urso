package it.polimi.ingsw.network.server.lobby;

import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;

import javax.naming.InvalidNameException;

public interface Lobby {

    void handleMessage(ClientMessage clientMessage, ServerVirtualView c) throws InvalidNameException;

    void enterLobby(ServerVirtualView serverVirtualView) throws InvalidNameException, IllegalStateException;

    LobbyType getType();
}
