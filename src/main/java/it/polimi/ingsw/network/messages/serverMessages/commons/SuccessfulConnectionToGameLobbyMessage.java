package it.polimi.ingsw.network.messages.serverMessages.commons;

import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;

import java.io.Serializable;

public class SuccessfulConnectionToGameLobbyMessage implements ServerMessage, Serializable {

    Boolean isHost;

    public SuccessfulConnectionToGameLobbyMessage(boolean isHost) {
        this.isHost = isHost;
    }

    @Override
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        clientVirtualView.ui.enterGamePhase(isHost, false);
    }
}