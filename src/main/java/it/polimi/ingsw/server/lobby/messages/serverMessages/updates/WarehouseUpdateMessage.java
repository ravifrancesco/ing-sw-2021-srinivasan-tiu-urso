package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.model.Warehouse;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

/**
 * Class used to send Warehouse updates to the clients.
 */
public class WarehouseUpdateMessage implements ServerMessage {

    private final Warehouse warehouse;

    /**
     * Constructor.
     *
     * @param warehouse warehouse for the update.
     */
    public WarehouseUpdateMessage(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        // TODO
    }
}
