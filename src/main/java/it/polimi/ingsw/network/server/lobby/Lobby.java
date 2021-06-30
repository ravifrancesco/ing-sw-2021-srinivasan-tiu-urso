package it.polimi.ingsw.network.server.lobby;

import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import javax.naming.InvalidNameException;

public interface Lobby {

    /**
     * Handles client messages
     *
     * @param clientMessage         message to handle
     * @param c                     ServerVirtual view on which the message is handled
     * @throws InvalidNameException if a InvalidNameException is thrown
     */
    void handleMessage(ClientMessage clientMessage, ServerVirtualView c) throws InvalidNameException;

    /**
     * Handles entering in a lobby
     *
     * @param serverVirtualView         ServerVirtualView which is entering a lobby
     * @throws InvalidNameException     name of the ServerVirtualView is not valid
     * @throws IllegalStateException    the lobby cannot be entered
     */
    void enterLobby(ServerVirtualView serverVirtualView) throws InvalidNameException, IllegalStateException;

    /**
     * Returns the type of the lobby
     *
     * @return  type of the lobby
     */
    LobbyType getType();
}
