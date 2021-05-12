package it.polimi.ingsw.server.messages.updates;

import it.polimi.ingsw.model.FaithTrack;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.server.messages.ServerMessage;

/**
 * Class used to send GameBoard updates to the clients.
 */
public class GameBoardUpdateMessage implements ServerMessage {

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
    public void updateClient(Object client, String nickname) {
        // TODO
    }
}
