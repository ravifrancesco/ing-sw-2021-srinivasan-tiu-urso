package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;

public class PlayerChangesExtraDeposit extends ClientGameMessage implements Serializable {

    /**
     * Message to activate the change extra deposit move from the controller.
     * See controller doc for more details about arguments.
     */


    private final Resource[] deposit;
    private final Resource[] extraDeposit;
    private final int lcIndex;

    public PlayerChangesExtraDeposit(Resource[] deposit, Resource[] extraDeposit, int lcIndex) {
        this.deposit = deposit;
        this.extraDeposit = extraDeposit;
        this.lcIndex = lcIndex;
    }

    @Override
    public void handle(ServerVirtualView c, Controller controller) {
        try {
            controller.changeDepositExtraDeposit(c.getNickname(), deposit, extraDeposit, lcIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        try {
            controller.changeDepositExtraDeposit(offlineClientVirtualView.getNickname(), deposit, extraDeposit, lcIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
