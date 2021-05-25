package it.polimi.ingsw.server.lobby.messages.serverMessages;

import it.polimi.ingsw.client.ClientConnection;

/**
 * Messages for the clients updates.
 */
public interface ServerMessage {

    // TODO change to client and doc
    void updateClient(ClientConnection clientConnection, String nickname);

}

