package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.model.GameError;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.utils.Pair;

public class GameErrorUpdateMessage implements ServerMessage {

    private final Pair<String,Exception> gameError;

    /**
     * Constructor for the GameErrorUpdate message.
     * @param gameError the GameError class containing the latest error.
     */
    public GameErrorUpdateMessage(GameError gameError) {
        this.gameError = gameError.getError();
    }

    @Override
    public void updateClient(Object client, String nickname) {
        // TODO
    }
}
