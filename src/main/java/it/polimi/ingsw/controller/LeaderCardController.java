package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.CardNotPlayableException;
import it.polimi.ingsw.controller.exceptions.WrongMoveException;
import it.polimi.ingsw.controller.exceptions.WrongTurnException;
import it.polimi.ingsw.controller.exceptions.WrongTurnPhaseException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.LeaderCard;

import java.util.Map;

public class LeaderCardController {

    private final Game game;

    /**
     * Constructor for a LeaderCard Controller object.
     * @param game represents the game which the controller belongs to.
     */

    public LeaderCardController(Game game) {
        this.game = game;
    }

    /**
     * @see ServerController#discardExcessLeaderCards(String, int)
     */

    public void discardExcessLeaderCards(String nickname, int cardToDiscard) throws WrongTurnException, WrongMoveException, CardNotPlayableException {
        if (!game.getCurrentPlayer().equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (!game.getTurnPhase().equals(TurnPhase.FIRST_TURN)) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(game.getCurrentPlayer());
        int handSize = player.getHandSize();

        if (handSize <= 2) {
            throw new WrongMoveException("Already discarded initial leader cards.");
        } else if (cardToDiscard >= handSize || cardToDiscard < 0) {
            throw new CardNotPlayableException("Invalid Index");
        }

        GameBoard gameboard = game.getGameBoard();
        player.discardLeaderCard(cardToDiscard, gameboard);
    }


    public void playLeaderCard(String nickname, int cardToPlay, ResourceContainer resourceContainer) throws WrongTurnException, CardNotPlayableException, WrongMoveException {

        if (!game.getCurrentPlayer().equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (game.getTurnPhase().equals(TurnPhase.FIRST_TURN)) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();
        Map<Banner, Integer> playerBanners = dashboard.getBanners();
        Map<Resource, Integer> playerResources = dashboard.getAllPlayerResources();
        LeaderCard lc = player.getHand().get(cardToPlay);


        if (cardToPlay >= player.getHandSize() || cardToPlay < 0) {
            throw new CardNotPlayableException("Invalid index");
        } else if (!player.getCard(cardToPlay).isPlayable(playerBanners, playerResources)) {
            throw new CardNotPlayableException("Not enough resources or banners");
        }

        try {
            player.playLeaderCard(cardToPlay);
        }
        catch (IllegalStateException e) { throw new CardNotPlayableException("Leader Card places are full"); }
        catch (IllegalArgumentException e) { throw new CardNotPlayableException("Position given is already full"); }


        if(lc.getResourceCost().size() != 0) {
            System.out.println("SERVER: leadercard resource cost is not null");
            try {
                player.getDashboard().payPrice(resourceContainer, lc.getResourceCost());
            } catch (IllegalArgumentException e) {
                throw new WrongMoveException("Resources do not match the cost");
            }
        } else {
            System.out.println("LeaderCard resource cost is null, no price to pay");
        }
    }

    /**
     * @see ServerController#discardLeaderCard(String, int)
     */

    public void discardLeaderCard(String nickname, int cardToDiscard) throws WrongTurnException, CardNotPlayableException {
        if (!game.getCurrentPlayer().equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (game.getTurnPhase().equals(TurnPhase.FIRST_TURN)) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(game.getCurrentPlayer());

        if (cardToDiscard >= player.getHandSize() || cardToDiscard < 0) {
            throw new CardNotPlayableException("Invalid index");
        }

        GameBoard gameBoard = game.getGameBoard();
        player.getDashboard().moveFaithMarker(1);
        player.discardLeaderCard(cardToDiscard, gameBoard);

    }
}
