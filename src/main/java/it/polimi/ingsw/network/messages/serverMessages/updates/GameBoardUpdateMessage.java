package it.polimi.ingsw.network.messages.serverMessages.updates;

import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;
import it.polimi.ingsw.model.full.table.GameBoard;
import it.polimi.ingsw.model.full.cards.Card;
import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;

import java.io.Serializable;
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
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        clientVirtualView.updateReducedGameBoard(leaderCardDeck, developmentCardDeck, discardDeck);
    }
}
