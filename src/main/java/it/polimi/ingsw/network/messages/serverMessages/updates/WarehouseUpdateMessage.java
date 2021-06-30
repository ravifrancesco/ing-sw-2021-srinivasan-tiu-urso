package it.polimi.ingsw.network.messages.serverMessages.updates;

import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.full.table.Warehouse;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;

import java.io.Serializable;
import java.util.Map;

/**
 * Class used to send Warehouse updates to the clients.
 */
public class WarehouseUpdateMessage implements ServerMessage, Serializable {

    private final String playerNickname;
    private final Resource[] deposit;
    private final Resource[][] extraDeposits;
    private final Map<Resource, Integer> locker;

    /**
     * Constructor.
     *
     * @param warehouse warehouse for the update.
     */
    public WarehouseUpdateMessage(Warehouse warehouse) {
        this.playerNickname = warehouse.getDashboard().getPlayer().getNickname();
        this.deposit = warehouse.getDeposit();
        this.extraDeposits = warehouse.getExtraDeposits();
        this.locker = warehouse.getLocker();
    }

    @Override
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        clientVirtualView.updateReducedWarehouse(playerNickname, deposit, extraDeposits, locker);
    }
}
