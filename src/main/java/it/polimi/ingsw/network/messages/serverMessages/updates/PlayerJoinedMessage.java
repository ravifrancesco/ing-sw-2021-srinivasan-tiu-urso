package it.polimi.ingsw.network.messages.serverMessages.updates;

import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;

import java.io.Serializable;

public class PlayerJoinedMessage implements ServerMessage, Serializable {

    private final String player;

    public PlayerJoinedMessage(String player) {
        this.player = player;
    }

    @Override
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        clientVirtualView.ui.printMessage("Player " + player + " joined the lobby");
    }
}
