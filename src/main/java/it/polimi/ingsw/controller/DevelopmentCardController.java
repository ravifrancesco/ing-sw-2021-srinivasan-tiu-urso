package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.model.full.specialAbilities.DevelopmentCardDiscount;
import it.polimi.ingsw.model.full.table.*;
import it.polimi.ingsw.model.utils.ResourceContainer;

import java.util.ArrayList;
import java.util.Map;

public class DevelopmentCardController {
    private final Game game;

    /**
     * Constructor for a DevelopmentCard Controller object.
     * @param game represents the game which the controller belongs to.
     */

    public DevelopmentCardController(Game game) {
        this.game = game;
    }

    /**
     * @see Controller#buyDevelopmentCard(String, int, int, ResourceContainer, int)
     */
    public void buyDevelopmentCard(String nickname, int row, int column, ResourceContainer resourcesToPayCost, int position)
            throws WrongTurnException, CardNotBuyableException, CardNotPlayableException, WrongMoveException {

        if (!game.getCurrentPlayer().equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (!game.getTurnPhase().equals(TurnPhase.COMMON)) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();
        GameBoard gameBoard = game.getGameBoard();
        DevelopmentCardGrid developmentCardGrid = gameBoard.getDevelopmentCardGrid();

        Map<Resource, Integer> playerResources = dashboard.getAllPlayerResources();
        ArrayList<DevelopmentCardDiscount> activeDiscounts = player.getActiveDiscounts();
        DevelopmentCard developmentCard;

        try {
            if (!developmentCardGrid.isBuyable(row, column, playerResources, activeDiscounts)) {
                throw new CardNotBuyableException("Incorrect resource selection, please try again. ");
            }
        } catch (IllegalArgumentException e) {
            throw new CardNotBuyableException("Card doesn't exist, please try again. ");
        }


        developmentCard = developmentCardGrid.peek(row, column);

        Map<Resource, Integer> cost = developmentCard.getResourceCost();
        activeDiscounts.forEach(discount -> cost.entrySet().stream().filter(e -> e.getKey() == discount.getResource())
                .forEach(e -> cost.put(e.getKey(), Math.max(e.getValue() - discount.getQuantity(), 0))));

        try {
            dashboard.simulatePayment(resourcesToPayCost, cost);
        } catch (IllegalArgumentException e) {
            throw new WrongMoveException("Resources do not match the cost");
        }

        try {
            dashboard.placeDevelopmentCard(developmentCard, position);
        }
        catch (IllegalStateException e) {
            throw new CardNotPlayableException("Not a valid index");
        }



        dashboard.payPrice(resourcesToPayCost, cost);

        developmentCardGrid.buy(row, column);
        game.startUniquePhase(TurnPhase.BUY);
    }
}
