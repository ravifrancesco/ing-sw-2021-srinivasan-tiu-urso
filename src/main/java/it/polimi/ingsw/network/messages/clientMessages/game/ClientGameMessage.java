package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

public abstract class ClientGameMessage extends ClientMessage {

    /**
     * Client message that gets send to server
     * @param serverVirtualView the server virtual view
     * @param controller the game controller itself
     */

    /**
     * Calls the correct method on the controller and notifies the UI based on the output
     * @param serverVirtualView the server virtual view
     * @param controller the game controller itself
     */
    public abstract void handle(ServerVirtualView serverVirtualView, Controller controller);

    public abstract void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller);

}
