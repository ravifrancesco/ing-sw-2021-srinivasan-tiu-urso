package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;

public class UnlimitedResourcesMessage extends ClientGameMessage implements Serializable {
    @Override
    public void handle(ServerVirtualView serverVirtualView, Controller controller) {
        controller.hack(serverVirtualView.getNickname());
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        controller.hack(offlineClientVirtualView.getNickname());
    }
}
