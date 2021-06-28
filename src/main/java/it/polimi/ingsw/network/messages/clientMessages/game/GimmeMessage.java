package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;

public class GimmeMessage extends ClientGameMessage implements Serializable {
    private int row;
    private int column;
    private int index;

    public GimmeMessage(int row, int column, int index) {
        this.row = row;
        this.column = column;
        this.index = index;
    }

    @Override
    public void handle(ServerVirtualView serverVirtualView, Controller controller) {
        controller.gimme(serverVirtualView.getNickname(), row, column, index);
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        controller.gimme(offlineClientVirtualView.getNickname(), row, column, index);
    }
}
