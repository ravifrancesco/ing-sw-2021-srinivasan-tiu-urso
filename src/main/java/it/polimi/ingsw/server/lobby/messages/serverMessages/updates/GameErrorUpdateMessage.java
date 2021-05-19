package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.model.GameError;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

public class GameErrorUpdateMessage implements ServerMessage {
    private final GameError gameError;

    /**
     * Constructor for the GameErrorUpdate message.
     * @param gameError the GameError class containing the latest error.
     */
    public GameErrorUpdateMessage(GameError gameError) {
        this.gameError = gameError;
    }

    @Override
    public void updateClient(Object client, String nickname) {
        // TODO
    }
}
