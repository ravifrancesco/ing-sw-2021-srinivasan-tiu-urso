package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.Warehouse;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import java.io.Serializable;
import java.util.Map;

/**
 * Class used to send Warehouse updates to the clients.
 */
public class WarehouseUpdateMessage implements ServerMessage, Serializable {

    private final Resource[] deposit;
    private final Resource[][] extraDeposits;
    private final Map<Resource, Integer> locker;
    /**
     * Constructor.
     *
     * @param warehouse warehouse for the update.
     */
    public WarehouseUpdateMessage(Warehouse warehouse) {
        this.deposit = warehouse.getDeposit();
        this.extraDeposits = warehouse.getExtraDeposits();
        this.locker = warehouse.getLocker();
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        clientConnection.updateReducedWarehouse(nickname, deposit, extraDeposits, locker);
    }
}
