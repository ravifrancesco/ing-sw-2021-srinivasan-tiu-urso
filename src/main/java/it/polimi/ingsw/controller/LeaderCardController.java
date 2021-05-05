package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.CardNotPlayableException;
import it.polimi.ingsw.controller.exceptions.WrongMoveException;
import it.polimi.ingsw.controller.exceptions.WrongTurnException;
import it.polimi.ingsw.controller.exceptions.WrongTurnPhaseException;
import it.polimi.ingsw.model.*;

import java.util.Map;

public class LeaderCardController {
    private final Game game;

    private String currentPlayer;

    /**
     * Constructor for a LeaderCard Controller object.
     * @param game represents the game which the controller belongs to.
     */

    public LeaderCardController(Game game) {
        this.game = game;
    }

    /**
     * Setter for the current player.
     * @param currentPlayer the current player of the game.
     */

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * @see ServerController#discardExcessLeaderCards(String, int)
     */

    public void discardExcessLeaderCards(String nickname, int cardToDiscard) throws WrongTurnException, WrongMoveException, CardNotPlayableException {
        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (!game.getTurnPhase().equals(TurnPhase.FIRST_TURN)) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(currentPlayer);
        int handSize = player.getHand().getHandSize();

        if (handSize <= 2) {
            throw new WrongMoveException("Already discarded initial leader cards.");
        } else if (cardToDiscard >= handSize || cardToDiscard < 0) {
            throw new CardNotPlayableException("Invalid Index");
        }

        GameBoard gameboard = game.getGameBoard();
        player.discardLeaderCard(cardToDiscard, gameboard);
    }

    /**
     * @see ServerController#playLeaderCard(String, int)
     */

    public void playLeaderCard(String nickname, int cardToPlay) throws WrongTurnException, CardNotPlayableException {

        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (game.getTurnPhase().equals(TurnPhase.FIRST_TURN)) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();
        Map<Banner, Integer> playerBanners = dashboard.getBanners();
        Map<Resource, Integer> playerResources = dashboard.getAllPlayerResources();

        if (cardToPlay >= player.getHand().getHandSize() || cardToPlay < 0) {
            throw new CardNotPlayableException("Invalid index");
        } else if (!player.getHand().getCard(cardToPlay).isPlayable(playerBanners, playerResources)) {
            throw new CardNotPlayableException("Not enough resources or banners");
        }

        try {
            player.playLeaderCard(cardToPlay);
        }
        catch (IllegalStateException e) { throw new CardNotPlayableException("Leader Card places are full"); }
        catch (IllegalArgumentException e) { throw new CardNotPlayableException("Position given is already full"); }

    }

    /**
     * @see ServerController#discardLeaderCard(String, int)
     */

    public void discardLeaderCard(String nickname, int cardToDiscard) throws WrongTurnException, CardNotPlayableException {
        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (game.getTurnPhase().equals(TurnPhase.FIRST_TURN)) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(currentPlayer);

        if (cardToDiscard >= player.getHand().getHandSize() || cardToDiscard < 0) {
            throw new CardNotPlayableException("Invalid index");
        }

        GameBoard gameBoard = game.getGameBoard();
        player.discardLeaderCard(cardToDiscard, gameBoard);

    }
}
