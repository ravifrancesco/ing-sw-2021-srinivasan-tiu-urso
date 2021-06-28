package it.polimi.ingsw.network.messages.serverMessages.commons;

import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;

import java.io.Serializable;

public class ErrorMessage implements ServerMessage, Serializable {

    @Override
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        clientVirtualView.ui.printErrorMessage("Server side error: ");
    }
}
