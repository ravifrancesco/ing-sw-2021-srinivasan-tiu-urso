package it.polimi.ingsw.network.messages.serverMessages.updates;

import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;

import java.io.Serializable;

/**
 * Class used to send Dashboard updates to the clients.
 */
public class UIMenuUpdate implements ServerMessage, Serializable {

    private final String menuCode;

    public UIMenuUpdate(String menuCode) {
        this.menuCode = menuCode;
    }

    @Override
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        clientVirtualView.ui.handleMenuCode(menuCode);
    }
}
