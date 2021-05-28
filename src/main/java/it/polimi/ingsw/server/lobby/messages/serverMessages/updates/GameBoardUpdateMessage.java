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
import java.util.stream.Collectors;

/**
 * Class used to send GameBoard updates to the clients.
 */
public class GameBoardUpdateMessage implements ServerMessage, Serializable {

    private final List<LeaderCard> leaderCardDeck;
    private final List<DevelopmentCard> developmentCardDeck;
    private final List<Card> discardDeck;

    /**
     * Constructor.
     *
     * @param gameBoard faithTrack for the update.
     */
    public GameBoardUpdateMessage(GameBoard gameBoard) {
        this.leaderCardDeck = gameBoard.getLeaderDeck().toList().stream().map(card -> (LeaderCard) card).collect(Collectors.toList());
        this.developmentCardDeck = gameBoard.getDevelopmentDeck().toList().stream().map(card -> (DevelopmentCard) card).collect(Collectors.toList());
        this.discardDeck = gameBoard.getDiscardDeck().toList();
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        clientConnection.updateReducedGameBoard(leaderCardDeck, developmentCardDeck, discardDeck);
    }
}
