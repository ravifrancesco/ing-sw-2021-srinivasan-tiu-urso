package it.polimi.ingsw.server.lobby.messages.serverMessages;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.IO.Constants;

import java.io.Serializable;

public class SuccessfulMoveMessage implements ServerMessage, Serializable {
    private String message;

    public SuccessfulMoveMessage(String message) {
        this.message = message;
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        clientConnection.ui.printColoredMessage(message, Constants.ANSI_GREEN);
    }
}
