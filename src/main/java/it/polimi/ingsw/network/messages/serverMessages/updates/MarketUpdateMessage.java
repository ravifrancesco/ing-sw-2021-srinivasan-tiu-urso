package it.polimi.ingsw.network.messages.serverMessages.updates;

import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;
import it.polimi.ingsw.model.full.table.Market;
import it.polimi.ingsw.model.full.marbles.Marble;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;

import java.io.Serializable;

/**
 * Class used to send Market updates to the clients.
 */
public class MarketUpdateMessage implements ServerMessage, Serializable {

    private final Marble[] marblesGrid;

    /**
     * Constructor.
     *
     * @param market for the update.
     */
    public MarketUpdateMessage(Market market) {
        this.marblesGrid = market.getMarblesGrid();
    }

    @Override
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        clientVirtualView.updateReducedMarket(marblesGrid);
    }
}
