package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;

public class PlayerChangesDeposit extends ClientGameMessage implements Serializable {

    /**
     * Message to activate the change deposit move from the controller.
     * See controller doc for more details about arguments.
     */

    Resource[] deposit;

    public PlayerChangesDeposit(Resource[] deposit) {
        this.deposit = deposit;
    }

    @Override
    public void handle(ServerVirtualView c, Controller controller) {
        int output = controller.changeDeposit(c.getNickname(), deposit);
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Deposit swaps successful");
        }
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        int output = controller.changeDeposit(offlineClientVirtualView.getNickname(), deposit);
        if (output == 0) {
            offlineClientVirtualView.printSuccessfulMove("Deposit swaps successful");
        }
    }
}
