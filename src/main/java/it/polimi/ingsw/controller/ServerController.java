package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.specialAbilities.*;

import javax.naming.InvalidNameException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class that represents the Server Controller of a game. The object memorizes the state of a Server Controller.
 * The state includes:
 * - The game.
 * - Number of players of the game.
 * - The current player.
 * - The first player.
 * - The number of first turns done.
 * - The game settings.
 * - Other controllers.
 */

public class ServerController {

    // TODO: check that illegal actions don't change the state
    // TODO: check if we need to add movements between deposit and extradeposit(s)

    private final Game game;

    private final int numberOfPlayers;

    private String currentPlayer;
    private String firstPlayer;

    private int firstTurns;

    GameSettings gameSettings;

    // controllers

    private ProductionController productionController;
    private WarehouseController warehouseController;
    private MarketController marketController;
    private LeaderCardController leaderCardController;
    private DevelopmentCardController developmentCardController;

    /**
     * Constructor for a Server Controller object. It creates the game and initializes all attributes.
     * @param gameId the unique id of the game.
     * @param numberOfPlayers the number of players of the game.
     */

    public ServerController(String gameId, int numberOfPlayers) throws IllegalArgumentException {
        if (numberOfPlayers < 2 || numberOfPlayers > 4) throw new IllegalArgumentException();
        this.game = new Game(gameId);
        this.numberOfPlayers = numberOfPlayers;
        this.productionController = new ProductionController(this.game);
        this.warehouseController = new WarehouseController(this.game);
        this.marketController = new MarketController(this.game);
        this.leaderCardController = new LeaderCardController(this.game);
        this.developmentCardController = new DevelopmentCardController(this.game);
    }

    /**
     * Method to load the game settings.
     * @param gameSettings the game settings to load.
     */
    /* RAVI */
    public void loadGameSettings(GameSettings gameSettings) {
        game.loadGameSettings(gameSettings);
    }

    /**
     * Method to allow a player to join the game.
     * @param nickname the nickname of the player who wants to join.
     * @throws GameFullException if the game is already full.
     * @throws InvalidNameException if the game contains an other player with the same nickname.
     */

    public void joinGame(String nickname) throws GameFullException, InvalidNameException {
        if (game.getPlayers().size() >= numberOfPlayers) {
            throw new GameFullException("Game " + this.game.getGameId() + " is full.");
        } else if (game.getPlayers().containsKey(nickname)) {
            throw new InvalidNameException("Nickname " + nickname + " is already in use");
        } else {
            game.addPlayer(nickname, new Player(gameSettings));
        }
    }

    /**
     * Reset method for the class. It resets the game, the current and the first player and initializes first turns.
     */

    public void reset() {
        game.reset();
        currentPlayer = game.getNextPlayer();
        firstPlayer = currentPlayer;
        firstTurns = 0;
    }

    /**
     * Starts the game.
     */

    public void startGame() {
      game.startUniquePhase(TurnPhase.FIRST_TURN); 
    }

    /**
     * It allows to discard the first three leader cards.
     * @param nickname the nickname of the player who made the move.
     * @param cardToDiscard the index of the card to be discarded.
     * @throws WrongTurnException if the player is not in turn.
     * @throws WrongMoveException if the player has already discarded the excess leader card.
     * @throws CardNotPlayableException if the index of the card is not valid.
     */

    /* ROBERT -- DONE */
    public void discardExcessLeaderCards(String nickname, int cardToDiscard) throws WrongTurnException, WrongMoveException, CardNotPlayableException {
        leaderCardController.setCurrentPlayer(this.currentPlayer);
        leaderCardController.discardExcessLeaderCards(nickname, cardToDiscard);
    }

    /**
     * It allows to get the initial resources of the game.
     * @param nickname the nickname of the player who made the move.
     * @param resource the resource chosen by the player.
     * @param position the position where to store the resource.
     * @throws WrongTurnException if the player is not in turn.
     * @throws WrongMoveException if the player has already acquired all due resources.
     * @throws DepositCellNotEmpty if the position given is already full.
     * @throws IllegalDepositStateException if the state of the deposit is invalid.
     */
    /* RAVI */
    public void getInitialResources(String nickname, Resource resource, int position) throws WrongTurnException, WrongMoveException, DepositCellNotEmpty, IllegalDepositStateException {
        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (!game.getTurnPhase().equals(TurnPhase.FIRST_TURN)) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(currentPlayer);
        Dashboard dashboard = player.getDashboard();

        if (checkInitialPhaseCompletion(dashboard)) {
            throw new WrongMoveException(currentPlayer + "has already acquired all due resources");
        }

        try {
            dashboard.storeResourceInDeposit(resource, position);
        } catch (IllegalArgumentException e) {
            throw new DepositCellNotEmpty("Deposit cell " + position + " not empty");
        } catch (IllegalStateException e) {
            throw new IllegalDepositStateException("Invalid deposit state");
        }
    }

    /**
     * Checks if the initial phase of the game is completed or not for the player.
     * @param d the dashboard of the player.
     * @return true if the initial phase is completed, false otherwise.
     */

    private boolean checkInitialPhaseCompletion(Dashboard d) {
        int storedResources = d.getDepositResourceQty();
        return switch(firstTurns) {
            case 0 -> storedResources >= 0;
            case 1, 2 -> storedResources >= 1;
            case 3 -> storedResources >= 2;
            default -> true;
        };
    }

    /**
     * It allows to play a leader card.
     * @param nickname the nickname of the player who made the move.
     * @param cardToPlay the index of the card to be played.
     * @throws WrongTurnException if the player is not in turn.
     * @throws CardNotPlayableException if the card is not playable due to position full or not enough resources/banners.
     */
    /* GIUSEPPE */
    public void playLeaderCard(String nickname, int cardToPlay) throws WrongTurnException, CardNotPlayableException {
        leaderCardController.setCurrentPlayer(this.currentPlayer);
        leaderCardController.playLeaderCard(nickname, cardToPlay);
    }

    /**
     * It allows to activate the production of a leader card.
     * @param nickname the nickname of the player who made the move.
     * @param cardToActivate the index of the card whose production power is to be activated.
     * @param resourcesToPayCost the resources to pay for the resources required by the production power.
     * @param resourceRequiredOptional the resources required that replace the selectable resources (if present).
     * @param resourceProducedOptional the resources produced that replace the selectable resources (if present).
     * @throws WrongTurnException if the player is not in turn.
     * @throws PowerNotActivatableException if the production power is not activatable.
     * @throws WrongMoveException if the resources do not match the cost.
     */
    /* GIUSEPPE */
    public void activateLeaderCardProduction(String nickname, int cardToActivate, ResourceContainer resourcesToPayCost,
                                             Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) throws WrongTurnException, PowerNotActivatableException, WrongMoveException {

        productionController.setCurrentPlayer(this.currentPlayer);
        productionController.activateLeaderCardProduction(nickname, cardToActivate, resourcesToPayCost, resourceRequiredOptional, resourceProducedOptional);
    }

    /**
     * It allows to discard a leader card.
     * @param nickname the nickname of the player who made the move.
     * @param cardToDiscard the index of the card to be discarded.
     * @throws WrongTurnException if the player is not in turn.
     * @throws CardNotPlayableException if the index of the card is not valid.
     */

    /* ROBERT DONE */
    public void discardLeaderCard(String nickname, int cardToDiscard) throws WrongTurnException, CardNotPlayableException {
        leaderCardController.setCurrentPlayer(this.currentPlayer);
        leaderCardController.discardLeaderCard(nickname, cardToDiscard);
    }

    /**
     * It allows to activate the production power of the dashboard.
     * @param nickname the nickname of the player who made the move.
     * @param resourcesToPayCost the resources to pay for the resources required by the production power.
     * @param resourceRequiredOptional the resources required that replace the selectable resources (if present).
     * @param resourceProducedOptional the resources produced that replace the selectable resources (if present).
     * @throws WrongTurnException if the player is not in turn.
     * @throws PowerNotActivatableException if the production power is not activatable.
     * @throws WrongMoveException if the resources do not match the cost.
     */
    /* GIUSEPPE */
    public void activateDashboardProduction(String nickname, ResourceContainer resourcesToPayCost,
                                            Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) throws WrongTurnException, PowerNotActivatableException, WrongMoveException {

        productionController.setCurrentPlayer(this.currentPlayer);
        productionController.activateDashboardProduction(nickname, resourcesToPayCost, resourceRequiredOptional, resourceProducedOptional);
    }

    /**
     * It allows to get resources from the market.
     * @param nickname the nickname of the player who made the move.
     * @param move the number that represents the selected row or column in the market.
     * @param wmrs the collection of activated power related to the white marble resources.
     * @throws WrongTurnException if the player is not in turn.
     * @throws WrongMoveException if the player does not have the same white marble resources of the parameter or if the move is not valid.
     */
    /* ROBERT DONE */
    public void getFromMarket(String nickname, int move, ArrayList<WhiteMarbleResource> wmrs) throws WrongTurnException, WrongMoveException {
        marketController.setCurrentPlayer(this.currentPlayer);
        marketController.getFromMarket(nickname, move, wmrs);
    }

    /**
     * It allows to buy a development card from the development card grid.
     * @param nickname the nickname of the player who made the move.
     * @param row the row of the grid selected.
     * @param column the column of the grid selected.
     * @param resourcesToPayCost the resources to pay for the cost of the development card.
     * @param position the index that represent where to place the card.
     * @throws WrongTurnException if the player is not in turn.
     * @throws CardNotBuyableException if the card does not exists or if the player does not have enough resources to buy the card.
     * @throws CardNotPlayableException if the position given onto the dashboard is not valid.
     * @throws WrongMoveException if the resources to pay does not match the cost.
     */
    /* RAVI */
    public void buyDevelopmentCard(String nickname, int row, int column, ResourceContainer resourcesToPayCost, int position)
            throws WrongTurnException, CardNotBuyableException, CardNotPlayableException, WrongMoveException {

        developmentCardController.setCurrentPlayer(this.currentPlayer);
        developmentCardController.buyDevelopmentCard(nickname, row, column, resourcesToPayCost, position);
    }

    /**
     * It allows to activate the production power of a development card.
     * @param nickname                      the nickname of the player who made the move.
     * @param cardToActivate                the index of the card whose production power is to be activated
     * @param resourcesToPayCost            the resources to pay for the resources required by the production power.
     * @param resourceRequiredOptional      the resources required that replace the selectable resources (if present).
     * @param resourceProducedOptional      the resources produced that replace the selectable resources (if present).
     * @throws WrongTurnException           if the player is not in turn.
     * @throws PowerNotActivatableException if the production power is not activatable.
     * @throws WrongMoveException           if the resources do not match the cost.
     */

    /* GIUSEPPE */
    public void activateDevelopmentCardProductionPower(String nickname, int cardToActivate, ResourceContainer resourcesToPayCost,
                                                       Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) throws WrongTurnException, PowerNotActivatableException, WrongMoveException {

        productionController.setCurrentPlayer(this.currentPlayer);
        productionController.activateDevelopmentCardProductionPower(nickname, cardToActivate, resourcesToPayCost, resourceRequiredOptional, resourceProducedOptional);
    }

    /**
     * Moves resources between the deposit
     * @param nickname                      the player nickname
     * @param deposit                       the new deposit
     * @throws WrongTurnException           if it is not the player's turn
     * @throws WrongMoveException           if the move is illegal
     * @throws IllegalDepositStateException if the move creates an illegal deposit
     */
    public void changeDeposit(String nickname, Resource[] deposit) throws WrongTurnException, WrongMoveException, IllegalDepositStateException {
        warehouseController.setCurrentPlayer(this.currentPlayer);
        warehouseController.changeResourcesDeposit(nickname, deposit);
    }

    /**
     * Moves resources between the deposit and an extra deposit
     * @param nickname                      the player nickname
     * @param deposit                       the new deposit
     * @param extraDeposit                  the new extra deposit
     * @param lcIndex                       the index of the placed leader card where the extra deposit is
     * @throws WrongTurnException           if it is not the player's turn
     * @throws WrongMoveException           if the move is illegal
     * @throws IllegalDepositStateException if the move creates an illegal deposit
     */
    public void changeDepositExtraDeposit(String nickname, Resource[] deposit, Resource[] extraDeposit, int lcIndex) throws WrongTurnException, WrongMoveException, IllegalDepositStateException {
        warehouseController.setCurrentPlayer(this.currentPlayer);
        warehouseController.changeResourcesDepositExtraDeposit(nickname, deposit, extraDeposit, lcIndex);

    }

    /**
     * It allows to move the resources from the supply to the warehouse.
     * @param nickname the nickname of the player who made the move.
     * @param from the index of the supply where the resources are stored.
     * @param to the index of the warehouse where to store the resources.
     * @throws WrongTurnException if the player is not in turn.
     * @throws WrongMoveException if the index of the warehouse is not valid.
     * @throws IllegalDepositStateException if the warehouse is in an invalid state.
     */
    public void storeFromSupply(String nickname, int from, int to) throws WrongTurnException, WrongMoveException, IllegalDepositStateException {
        warehouseController.setCurrentPlayer(this.currentPlayer);
        warehouseController.storeFromSupply(nickname, from, to);
    }

    /**
     * It allows to move the resources from the supply to the extra deposit.
     * @param nickname the nickname of the player who made the move.
     * @param leaderCardPos the position of the leader card which has extra deposit special ability.
     * @param from the index of the supply where the resources are stored.
     * @param to the index of the extra deposit where to store the resources.
     * @throws WrongTurnException if the player is not in turn.
     * @throws WrongMoveException if the indexes are not valid.
     * @throws IllegalDepositStateException if the extra deposit is in an invalid state.
     */
    /* ROBERT DONE */
    public void storeFromSupplyInExtraDeposit(String nickname, int leaderCardPos, int from, int to) throws WrongTurnException, WrongMoveException, IllegalDepositStateException {
        warehouseController.setCurrentPlayer(this.currentPlayer);
        warehouseController.storeFromSupplyInExtraDeposit(nickname, leaderCardPos, from, to);
    }

    /**
     * It allows to end a turn.
     * @param nickname the nickname of the player in turn.
     * @return true if the game is ended, false otherwise.
     * @throws WrongTurnException if the player is not in turn.
     * @throws LeaderCardInExcessException if the player has not discarded enough cards.
     * @throws WrongMoveException if the player has not acquired all due resources.
     */
    public boolean endTurn(String nickname) throws WrongTurnException, LeaderCardInExcessException, WrongMoveException {
        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        }
        // no turn phase check needed: player may stupidly pass the turn whilst having done nothing.
        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();

        if (player.getHand().getHandSize() > 2) {
            throw new LeaderCardInExcessException(currentPlayer + " hasn't discarded enough cards");
        }

        int faithPoints = dashboard.discardResources();

        game.getPlayers()
                .entrySet()
                .stream()
                .filter(p -> !p.getKey().equals(currentPlayer))
                .forEach(p -> p.getValue().getDashboard().moveFaithMarker(faithPoints));

        dashboard.resetProductionPowers();

        if(game.getTurnPhase().equals(TurnPhase.FIRST_TURN) && !checkInitialPhaseCompletion(dashboard)) {
            throw new WrongMoveException(currentPlayer + " has not acquired all due resources.");
        }

        if(player.getDashboard().checkGameEnd() && game.getTurnPhase() != TurnPhase.ENDGAME) {
            game.startUniquePhase(TurnPhase.ENDGAME);
        } else if(firstTurns < numberOfPlayers) {
            dashboard.moveFaithMarker(firstTurns < 2 ? 0 : 1);
            firstTurns += 1;
            game.startUniquePhase(TurnPhase.FIRST_TURN);
        } else {
            game.startUniquePhase(TurnPhase.COMMON);
        }

        currentPlayer = game.getNextPlayer();

        return game.getTurnPhase() == TurnPhase.ENDGAME && currentPlayer.equals(firstPlayer);
    }

    /**
     * Getter for the game status.
     * @return the game status.
     */
    public Game getGameStatus() {
        return game.getGameStatus();
    }

}