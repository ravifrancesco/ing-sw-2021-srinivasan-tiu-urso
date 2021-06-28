package it.polimi.ingsw.network.messages.serverMessages.updates;

import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;

import java.io.Serializable;

/**
 * Class used to send Dashboard updates to the clients.
 * TODO change names with UI
 */
public class CLIMenuUpdate implements ServerMessage, Serializable {

    private String menuCode;

    public CLIMenuUpdate(String menuCode) {
        this.menuCode = menuCode;
    }

    @Override
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        clientVirtualView.ui.handleMenuCode(menuCode);
    }
}
