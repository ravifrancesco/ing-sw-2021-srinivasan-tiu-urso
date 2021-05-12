package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

/**
 * Class used to send Market updates to the clients.
 */
public class MarketUpdateMessage implements ServerMessage {

    private final Market market;

    /**
     * Constructor.
     *
     * @param market faithTrack for the update.
     */
    public MarketUpdateMessage(Market market) {
        this.market = market;
    }

    @Override
    public void updateClient(Object client, String nickname) {
        // TODO
    }
}
