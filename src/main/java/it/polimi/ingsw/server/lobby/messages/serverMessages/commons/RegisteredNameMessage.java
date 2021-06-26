package it.polimi.ingsw.server.lobby.messages.serverMessages.commons;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.client.IO.Constants;
import it.polimi.ingsw.client.UIType;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import java.io.Serializable;

public class RegisteredNameMessage implements ServerMessage, Serializable {



    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        clientConnection.setPlayerNickname(nickname);
        clientConnection.nameRegistered();
        if(clientConnection.ui.getType() == UIType.CLI) {
            ((CLI) clientConnection.ui).printMessageNoNL("Successfully registered with nickname ");
            ((CLI) clientConnection.ui).printColoredMessage("" + nickname, Constants.ANSI_CYAN);
        }
    }
}
