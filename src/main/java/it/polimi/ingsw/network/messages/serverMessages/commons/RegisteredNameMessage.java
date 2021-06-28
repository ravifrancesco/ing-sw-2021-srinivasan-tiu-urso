package it.polimi.ingsw.network.messages.serverMessages.commons;

import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;
import it.polimi.ingsw.view.UI.CLI.CLI;
import it.polimi.ingsw.view.UI.CLI.IO.Constants;
import it.polimi.ingsw.view.UI.UIType;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;

import java.io.Serializable;

public class RegisteredNameMessage implements ServerMessage, Serializable {



    @Override
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        clientVirtualView.setPlayerNickname(nickname);
        clientVirtualView.nameRegistered();
        if(clientVirtualView.ui.getType() == UIType.CLI) {
            ((CLI) clientVirtualView.ui).printMessageNoNL("Successfully registered with nickname ");
            ((CLI) clientVirtualView.ui).printColoredMessage("" + nickname, Constants.ANSI_CYAN);
        }
    }
}
