package it.polimi.ingsw.server.lobby.messages.serverMessages.commons;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import java.io.Serializable;

public class SuccessfulConnectionToGameLobbyMessage implements ServerMessage, Serializable {

    String id;
    Boolean isHost;

    public SuccessfulConnectionToGameLobbyMessage(String id, boolean isHost) {
        this.id = id;
        this.isHost = isHost;
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        clientConnection.ui.enterGamePhase(isHost);
    }
}