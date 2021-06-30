package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;

public class PlayMessage extends ClientGameMessage implements Serializable {
    private final int index;

    public PlayMessage(int index) {
        this.index = index;
    }

    @Override
    public void handle(ServerVirtualView serverVirtualView, Controller controller) {
        controller.play(serverVirtualView.getNickname(), index);
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        controller.play(offlineClientVirtualView.getNickname(), index);
    }
}
