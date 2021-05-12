package it.polimi.ingsw.server.messages.updates;

import it.polimi.ingsw.model.Hand;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.observerPattern.observables.PlayerObservable;
import it.polimi.ingsw.server.messages.ServerMessage;

/**
 * Class used to send Player updates to the clients.
 */
public class PlayerUpdateMessage implements ServerMessage {

    private final Player player;

    /**
     * Constructor.
     *
     * @param player player for the update.
     */
    public PlayerUpdateMessage(Player player) {
        this.player = player;
    }

    @Override
    public void updateClient(Object client, String nickname) {
        // TODO
    }
}