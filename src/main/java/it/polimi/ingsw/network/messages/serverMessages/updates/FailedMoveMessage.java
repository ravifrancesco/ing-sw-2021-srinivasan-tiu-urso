package it.polimi.ingsw.network.messages.serverMessages.updates;

import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;

import java.io.Serializable;

public class FailedMoveMessage implements ServerMessage, Serializable {
    private final String message;

    public FailedMoveMessage(String message) {
        this.message = message;
    }

    @Override
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        clientVirtualView.ui.printErrorMessage(message);
    }
}
