package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerGetsFromMarket extends ClientGameMessage implements Serializable {

    /**
     * Message to activate the get from market move from the controller.
     * See controller doc for more details about arguments.
     */


    private final int move;
    private final ArrayList<Resource> wmrs;

    public PlayerGetsFromMarket(int move, ArrayList<Resource> wmrs) {
        this.move = move;
        this.wmrs = wmrs;
    }

    @Override
    public void handle(ServerVirtualView c, Controller controller) {
        int output = controller.getFromMarket(c.getNickname(), move, wmrs);
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Market move completed, adding resources to your supply...");
            c.sendCLIupdateMessage("after_getfrommarket");
        }
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        int output = controller.getFromMarket(offlineClientVirtualView.getNickname(), move, wmrs);
        if (output == 0) {
            offlineClientVirtualView.printSuccessfulMove("Market move completed, adding resources to your supply...");
            offlineClientVirtualView.getUi().handleMenuCode("after_getfrommarket");
        }
    }
}