package it.polimi.ingsw.server.lobby.messages.serverMessages.commons;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import java.io.Serializable;

public class CorrectHandlingMessage implements Serializable, ServerMessage {

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        clientConnection.cli.printMessage("Message successfully handled by the server!");
    }
}
