package it.polimi.ingsw.server.lobby.messages.serverMessages;

/**
 * Messages for the clients updates.
 */
public interface ServerMessage {

    // TODO change to client and doc
    void updateClient(Object client, String nickname);

}

