package it.polimi.ingsw.network.messages.serverMessages.updates;

import it.polimi.ingsw.model.utils.GameError;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.UI.UI;
import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;

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
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        UI ui = clientVirtualView.ui;
        if (gameError.first.equals(nickname)) {
            ui.printErrorMessage("Move failed: " + gameError.second.getMessage());
        }
    }
}
