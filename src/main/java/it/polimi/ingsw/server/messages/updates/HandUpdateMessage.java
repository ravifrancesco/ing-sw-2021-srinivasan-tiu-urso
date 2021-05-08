package it.polimi.ingsw.server.messages.updates;

import it.polimi.ingsw.model.Hand;
import it.polimi.ingsw.server.messages.ServerMessage;

/**
 * Class used to send Hand updates to the clients.
 */
public class HandUpdateMessage implements ServerMessage {

    private final Hand hand;

    /**
     * Constructor.
     *
     * @param hand hand for the update.
     */
    public HandUpdateMessage(Hand hand) {
        this.hand = hand;
    }

    @Override
    public void updateClient(Object client, String nickname) {
        // TODO
    }
}
