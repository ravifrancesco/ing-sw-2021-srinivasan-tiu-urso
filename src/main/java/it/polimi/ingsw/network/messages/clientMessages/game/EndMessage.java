package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;

public class EndMessage extends ClientGameMessage implements Serializable {

    @Override
    public void handle(ServerVirtualView c, Controller controller) {
        controller.end(c.getNickname());
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        controller.end(offlineClientVirtualView.getNickname());
    }
}
