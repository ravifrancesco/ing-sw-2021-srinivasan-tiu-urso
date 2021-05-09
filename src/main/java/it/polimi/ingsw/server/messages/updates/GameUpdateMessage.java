package it.polimi.ingsw.server.messages.updates;

import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.messages.ServerMessage;

/**
 * Class used to send Game updates to the clients.
 */
public class GameUpdateMessage implements ServerMessage {

    private final Game game;

    /**
     * Constructor.
     *
     * @param game dashboard for the update.
     */
    public GameUpdateMessage(Game game) {
        this.game = game;
    }

    @Override
    public void updateClient(Object client, String nickname) {
        // TODO
    }
}
