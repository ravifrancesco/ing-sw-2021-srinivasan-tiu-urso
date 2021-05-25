package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import java.io.Serializable;

/**
 * Class used to send GameBoard updates to the clients.
 */
public class GameBoardUpdateMessage implements ServerMessage, Serializable {

    private final GameBoard gameBoard;

    /**
     * Constructor.
     *
     * @param gameBoard faithTrack for the update.
     */
    public GameBoardUpdateMessage(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        // TODO
    }
}
