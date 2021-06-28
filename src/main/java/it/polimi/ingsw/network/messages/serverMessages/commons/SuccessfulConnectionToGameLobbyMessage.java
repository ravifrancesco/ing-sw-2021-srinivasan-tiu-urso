package it.polimi.ingsw.network.messages.serverMessages.commons;

import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;

import java.io.Serializable;

public class SuccessfulConnectionToGameLobbyMessage implements ServerMessage, Serializable {

    String id;
    Boolean isHost;

    public SuccessfulConnectionToGameLobbyMessage(String id, boolean isHost) {
        this.id = id;
        this.isHost = isHost;
    }

    @Override
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        clientVirtualView.ui.enterGamePhase(isHost, false);
    }
}