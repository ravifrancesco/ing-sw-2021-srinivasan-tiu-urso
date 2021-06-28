package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;

public class PlayerStoresFromSupply extends ClientGameMessage implements Serializable {
    private int from;
    private int to;

    public PlayerStoresFromSupply(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void handle(ServerVirtualView c, Controller controller) {
        int output = controller.storeFromSupply(c.getNickname(), from, to);
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Store from supply successfull, added resource to deposit index " + to);
            c.sendCLIupdateMessage("after_store_supply");
        }
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        int output = controller.storeFromSupply(offlineClientVirtualView.getNickname(), from, to);
        if (output == 0) {
            offlineClientVirtualView.printSuccessfulMove("Store from supply successfull, added resource to deposit index " + to);
            offlineClientVirtualView.getUi().handleMenuCode("after_store_supply");
        }
    }
}
