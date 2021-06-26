package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.UIType;
import it.polimi.ingsw.model.GameError;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.utils.Pair;

import java.io.Serializable;

public class GameErrorUpdateMessage implements ServerMessage, Serializable {

    private final Pair<String, Exception> gameError;

    /**
     * Constructor for the GameErrorUpdate message.
     *
     * @param gameError the GameError class containing the latest error.
     */
    public GameErrorUpdateMessage(GameError gameError) {
        this.gameError = gameError.getError();
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        UI ui = clientConnection.ui;
        if (gameError.first.equals(nickname)) {
            ui.printErrorMessage("Move failed: " + gameError.second.getMessage());
        }
    }
}
