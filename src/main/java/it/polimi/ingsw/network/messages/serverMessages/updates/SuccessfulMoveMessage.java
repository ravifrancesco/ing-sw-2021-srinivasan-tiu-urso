package it.polimi.ingsw.network.messages.serverMessages.updates;

import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.view.UI.CLI.IO.Constants;
import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;

import java.io.Serializable;

public class SuccessfulMoveMessage implements ServerMessage, Serializable {
    private final String message;

    public SuccessfulMoveMessage(String message) {
        this.message = message;
    }

    @Override
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        clientVirtualView.ui.printColoredMessage(message, Constants.ANSI_GREEN);
    }
}
