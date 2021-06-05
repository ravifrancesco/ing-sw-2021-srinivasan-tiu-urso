package it.polimi.ingsw.server.lobby.messages.serverMessages;

import it.polimi.ingsw.client.ClientConnection;

import java.io.Serializable;

public class FailedMoveMessage implements ServerMessage, Serializable {
    private String message;

    public FailedMoveMessage(String message) {
        this.message = message;
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        clientConnection.ui.printErrorMessage(message);
    }
}
