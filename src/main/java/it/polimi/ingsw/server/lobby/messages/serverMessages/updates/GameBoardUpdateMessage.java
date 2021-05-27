package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to send GameBoard updates to the clients.
 */
public class GameBoardUpdateMessage implements ServerMessage, Serializable {

    private final List<Card> leaderCardDeck;
    private final List<Card> developmentCardDeck;
    private final List<Card> discardDeck;

    /**
     * Constructor.
     *
     * @param gameBoard faithTrack for the update.
     */
    public GameBoardUpdateMessage(GameBoard gameBoard) {
        this.leaderCardDeck = gameBoard.getLeaderDeck().toList();
        this.developmentCardDeck = gameBoard.getDevelopmentDeck().toList();
        this.discardDeck = gameBoard.getDiscardDeck().toList();
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        // TODO clientConnection.updateGameBoard(leaderCardDeck, developmentCardDeck, discardDeck);
    }
}
