package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.marbles.Marble;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import java.io.Serializable;

/**
 * Class used to send Market updates to the clients.
 */
public class MarketUpdateMessage implements ServerMessage, Serializable {

    private Marble[] marblesGrid;

    /**
     * Constructor.
     *
     * @param market for the update.
     */
    public MarketUpdateMessage(Market market) {
        this.marblesGrid = market.getMarblesGrid();
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        // TODO clientConnection.updateReducedMarket(marblesGrid);
    }
}
