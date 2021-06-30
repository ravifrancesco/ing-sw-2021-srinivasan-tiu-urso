package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

public abstract class ClientGameMessage extends ClientMessage {

    public abstract void handle(ServerVirtualView serverVirtualView, Controller controller);

    public abstract void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller);

}
