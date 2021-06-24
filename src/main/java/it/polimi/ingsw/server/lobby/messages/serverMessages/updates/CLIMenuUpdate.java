package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.client.UIType;
import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.ProductionPower;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Class used to send Dashboard updates to the clients.
 */
public class CLIMenuUpdate implements ServerMessage, Serializable {

    private String menuCode;

    public CLIMenuUpdate(String menuCode) {
        this.menuCode = menuCode;
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        if(clientConnection.ui.getType() == UIType.CLI) {
            CLI cli = (CLI) clientConnection.ui;
            cli.handleMenuCode(menuCode);
        }

    }
}
