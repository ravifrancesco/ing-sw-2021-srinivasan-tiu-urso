package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.specialAbilities.*;
import it.polimi.ingsw.server.Connection;

import javax.naming.InvalidNameException;
import java.util.ArrayList;
import java.util.List;
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
    private final Game game;

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
     * @param gameId                        the unique id of the game.
     * @param numberOfPlayers               the number of players of the game.
     */

    public ServerController(String gameId, int numberOfPlayers) throws IllegalArgumentException {
        if (numberOfPlayers < 2 || numberOfPlayers > 4) throw new IllegalArgumentException();
        this.game = new Game(gameId, numberOfPlayers);
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
    public void loadGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
        game.loadGameSettings(gameSettings);
    }

    /**
     * Method to allow a player to join the game.
     * @param nickname                      the nickname of the player who wants to join.
     * @throws GameFullException            if the game is already full.
     * @throws InvalidNameException         if the game contains an other player with the same nickname.
     */
    public void joinGame(String nickname) throws GameFullException, InvalidNameException {
        if (game.getPlayers().size() >= game.getNumberOfPlayers()) {
            throw new GameFullException("Game " + this.game.getGameId() + " is full.");
        } else if (game.getPlayers().containsKey(nickname)) {
            throw new InvalidNameException("Nickname " + nickname + " is already in use");
        } else {
            game.addPlayer(nickname, new Player(gameSettings));
        }
    }

    /**
     * Adds the connection as the observer of all observable classes
     *
     * @param c observer
     */
    public void addObservers(Connection c) {

        game.addObserver(c);

        GameBoard gameBoard = game.getGameBoard();
        gameBoard.addObserver(c);
        gameBoard.getMarket().addObserver(c);
        gameBoard.getDevelopmentCardGrid().addObserver(c);

        List<Player> players = new ArrayList<>(game.getPlayers().values());
        players.forEach(p -> p.addObserver(c));
        players.forEach(p -> p.getDashboard().addObserver(c));
        players.forEach(p -> p.getDashboard().getFaithTrack().addObserver(c));
        players.forEach(p -> p.getDashboard().getWarehouse().addObserver(c));

    }

    /**
     * Removes the connection from the observers of all observable classes
     *
     * @param c observer
     */
    public void removeObservers(Connection c) {

        game.removeObserver(c);

        GameBoard gameBoard = game.getGameBoard();
        gameBoard.removeObserver(c);
        gameBoard.getMarket().removeObserver(c);
        gameBoard.getDevelopmentCardGrid().removeObserver(c);

        List<Player> players = new ArrayList<>(game.getPlayers().values());
        players.forEach(p -> p.removeObserver(c));
        players.forEach(p -> p.getDashboard().removeObserver(c));
        players.forEach(p -> p.getDashboard().getFaithTrack().removeObserver(c));
        players.forEach(p -> p.getDashboard().getWarehouse().removeObserver(c));

    }

    /**
     * Reset method for the class. It resets the game, the current and the first player and initializes first turns.
     */
    public void reset() {
        game.reset();
        firstTurns = 0;
    }

    /**
     * Starts the game.
     */
    public void startGame() {
      game.startUniquePhase(TurnPhase.FIRST_TURN);
        game.changePlayer();
        game.setFirstPlayer(game.getCurrentPlayer());
    }

    /**
     * It allows to discard the first three leader cards.
     * @param nickname                      the nickname of the player who made the move.
     * @param cardToDiscard                 the index of the card to be discarded.
     */
    public void discardExcessLeaderCards(String nickname, int cardToDiscard) {
        try {
            leaderCardController.setCurrentPlayer(game.getCurrentPlayer());
            leaderCardController.discardExcessLeaderCards(nickname, cardToDiscard);
        } catch (WrongTurnException | WrongMoveException | CardNotPlayableException e) {
            game.setError(e, nickname);
        }
    }

    /**
     * It allows to get the initial resources of the game.
     * @param nickname                      the nickname of the player who made the move.
     * @param resource                      the resource chosen by the player.
     * @param position                      the position where to store the resource.
     */
    public void getInitialResources(String nickname, Resource resource, int position) {
        String currentPlayer = game.getCurrentPlayer();
        if (!currentPlayer.equals(nickname)) {
            game.setError(new WrongTurnException("Not " + nickname + " turn"), nickname);
        } else if (!game.getTurnPhase().equals(TurnPhase.FIRST_TURN)) {
            game.setError(new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name()), nickname);
        }

        Player player = game.getPlayers().get(currentPlayer);
        Dashboard dashboard = player.getDashboard();

        if (checkInitialPhaseCompletion(dashboard)) {
            game.setError(new WrongMoveException(currentPlayer + "has already acquired all due resources"), nickname);
        }

        try {
            dashboard.storeResourceInDeposit(resource, position);
        } catch (IllegalArgumentException e) {
            game.setError(new DepositCellNotEmpty("Deposit cell " + position + " not empty"), nickname);
        } catch (IllegalStateException e) {
            game.setError(new IllegalDepositStateException("Invalid deposit state"), nickname);
        }
    }

    /**
     * Checks if the initial phase of the game is completed or not for the player.
     * @param d                             the dashboard of the player.
     * @return                              true if the initial phase is completed, false otherwise.
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
     * @param nickname                      the nickname of the player who made the move.
     * @param cardToPlay                    the index of the card to be played.
     */
    public void playLeaderCard(String nickname, int cardToPlay) {
        try {
            leaderCardController.setCurrentPlayer(game.getCurrentPlayer());
            leaderCardController.playLeaderCard(nickname, cardToPlay);
        } catch (WrongTurnException | CardNotPlayableException e) {
            game.setError(e, nickname);
        }
    }

    /**
     * It allows to activate the production of a leader card.
     * @param nickname                      the nickname of the player who made the move.
     * @param cardToActivate                the index of the card whose production power is to be activated.
     * @param resourcesToPayCost            the resources to pay for the resources required by the production power.
     * @param resourceRequiredOptional      the resources required that replace the selectable resources (if present).
     * @param resourceProducedOptional      the resources produced that replace the selectable resources (if present).
     */
    public void activateLeaderCardProductionPower(String nickname, int cardToActivate, ResourceContainer resourcesToPayCost,
                                             Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) {

        try {
            productionController.setCurrentPlayer(game.getCurrentPlayer());
            productionController.activateLeaderCardProduction(nickname, cardToActivate, resourcesToPayCost, resourceRequiredOptional, resourceProducedOptional);
        } catch (WrongTurnException | PowerNotActivatableException | WrongMoveException e) {
            game.setError(e, nickname);
        }
    }

    /**
     * It allows to discard a leader card.
     * @param nickname                      the nickname of the player who made the move.
     * @param cardToDiscard                 the index of the card to be discarded.
     */
    public void discardLeaderCard(String nickname, int cardToDiscard) {
        try {
            leaderCardController.setCurrentPlayer(game.getCurrentPlayer());
            leaderCardController.discardLeaderCard(nickname, cardToDiscard);
        } catch (WrongTurnException | CardNotPlayableException e) {
            game.setError(e, nickname);
        }
    }

    /**
     * It allows to activate the production power of the dashboard.
     * @param nickname                      the nickname of the player who made the move.
     * @param resourcesToPayCost            the resources to pay for the resources required by the production power.
     * @param resourceRequiredOptional      the resources required that replace the selectable resources (if present).
     * @param resourceProducedOptional      the resources produced that replace the selectable resources (if present).
     */
    public void activateDashboardProductionPower(String nickname, ResourceContainer resourcesToPayCost,
                                            Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) {

        try {
            productionController.setCurrentPlayer(game.getCurrentPlayer());
            productionController.activateDashboardProduction(nickname, resourcesToPayCost, resourceRequiredOptional, resourceProducedOptional);
        } catch (WrongTurnException | PowerNotActivatableException | WrongMoveException e) {
            game.setError(e, nickname);
        }
    }

    /**
     * It allows to get resources from the market.
     * @param nickname                      the nickname of the player who made the move.
     * @param move                          the number that represents the selected row or column in the market.
     * @param wmrs                          the collection of activated power related to the white marble resources.
     */
    public void getFromMarket(String nickname, int move, ArrayList<WhiteMarbleResource> wmrs) {
        try {
            marketController.setCurrentPlayer(game.getCurrentPlayer());
            marketController.getFromMarket(nickname, move, wmrs);
        } catch (WrongTurnException | WrongMoveException e) {
            game.setError(e, nickname);
        }
    }

    /**
     * It allows to buy a development card from the development card grid.
     * @param nickname                      the nickname of the player who made the move.
     * @param row                           the row of the grid selected.
     * @param column                        the column of the grid selected.
     * @param resourcesToPayCost            the resources to pay for the cost of the development card.
     * @param position                      the index that represent where to place the card.
     */
    public void buyDevelopmentCard(String nickname, int row, int column, ResourceContainer resourcesToPayCost, int position)  {
        try {
            developmentCardController.setCurrentPlayer(game.getCurrentPlayer());
            developmentCardController.buyDevelopmentCard(nickname, row, column, resourcesToPayCost, position);
        } catch (WrongTurnException | CardNotBuyableException | CardNotPlayableException | WrongMoveException e) {
            game.setError(e, nickname);
        }
    }

    /**
     * It allows to activate the production power of a development card.
     * @param nickname                      the nickname of the player who made the move.
     * @param cardToActivate                the index of the card whose production power is to be activated
     * @param resourcesToPayCost            the resources to pay for the resources required by the production power.
     * @param resourceRequiredOptional      the resources required that replace the selectable resources (if present).
     * @param resourceProducedOptional      the resources produced that replace the selectable resources (if present).
     */
    public void activateDevelopmentCardProductionPower(String nickname, int cardToActivate, ResourceContainer resourcesToPayCost,
                                                       Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) {

        try {
            productionController.setCurrentPlayer(game.getCurrentPlayer());
            productionController.activateDevelopmentCardProductionPower(nickname, cardToActivate, resourcesToPayCost, resourceRequiredOptional, resourceProducedOptional);
        } catch (WrongTurnException | PowerNotActivatableException | WrongMoveException e) {
            game.setError(e, nickname);
        }
    }

    /**
     * Moves resources between the deposit
     * @param nickname                      the player nickname
     * @param deposit                       the new deposit
     */
    public void changeDeposit(String nickname, Resource[] deposit) {
        try {
            warehouseController.setCurrentPlayer(game.getCurrentPlayer());
            warehouseController.changeResourcesDeposit(nickname, deposit);
        } catch (WrongTurnException | WrongMoveException | IllegalDepositStateException e) {
            game.setError(e, nickname);
        }
    }

    // TODO doc (paste from history)
    public void changeDepositExtraDeposit(String nickname, Resource[] deposit, Resource[] extraDeposit, int lcIndex) {
        try {
            warehouseController.setCurrentPlayer(game.getCurrentPlayer());
            warehouseController.changeResourcesDepositExtraDeposit(nickname, deposit, extraDeposit, lcIndex);
        } catch (WrongTurnException | IllegalDepositStateException | WrongMoveException e) {
            game.setError(e, nickname);
        }
    }

    /**
     * It allows to move the resources from the supply to the warehouse.
     * @param nickname                      the nickname of the player who made the move.
     * @param from                          the index of the supply where the resources are stored.
     * @param to                            the index of the warehouse where to store the resources.
     */
    public void storeFromSupply(String nickname, int from, int to) {
        try {
            warehouseController.setCurrentPlayer(game.getCurrentPlayer());
            warehouseController.storeFromSupply(nickname, from, to);
        } catch (WrongTurnException | WrongMoveException | IllegalDepositStateException e) {
            game.setError(e, nickname);
        }
    }

    /**
     * It allows to move the resources from the supply to the extra deposit.
     * @param nickname                      the nickname of the player who made the move.
     * @param leaderCardPos                 the position of the leader card which has extra deposit special ability.
     * @param from                          the index of the supply where the resources are stored.
     * @param to                            the index of the extra deposit where to store the resources.
     */
    public void storeFromSupplyInExtraDeposit(String nickname, int leaderCardPos, int from, int to) {
        try {
            warehouseController.setCurrentPlayer(game.getCurrentPlayer());
            warehouseController.storeFromSupplyInExtraDeposit(nickname, leaderCardPos, from, to);
        } catch (WrongTurnException | WrongMoveException | IllegalDepositStateException e) {
            game.setError(e, nickname);
        }
    }

    /**
     * It allows to end a turn.
     * @param nickname                      the nickname of the player in turn.
     * @return                              true if the game is ended, false otherwise.
     */
    public boolean endTurn(String nickname) {
        String currentPlayer = game.getCurrentPlayer();
        if (!currentPlayer.equals(nickname)) {
            game.setError(new WrongTurnException("Not " + nickname + " turn"), nickname);
        }
        // no turn phase check needed: player may stupidly pass the turn whilst having done nothing.

        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();

        if (player.getHandSize() > 2) {
            game.setError(new LeaderCardInExcessException(currentPlayer + " hasn't discarded enough cards"), nickname);
        }

        int faithPoints = dashboard.discardResources();

        game.getPlayers()
                .entrySet()
                .stream()
                .filter(p -> !p.getKey().equals(currentPlayer))
                .forEach(p -> p.getValue().getDashboard().moveFaithMarker(faithPoints));

        dashboard.resetProductionPowers();

        if(game.getTurnPhase().equals(TurnPhase.FIRST_TURN) && !checkInitialPhaseCompletion(dashboard)) {
            game.setError(new WrongMoveException(currentPlayer + " has not acquired all due resources."), nickname);
        }

        if(player.getDashboard().checkGameEnd() && game.getTurnPhase() != TurnPhase.ENDGAME) {
            game.startUniquePhase(TurnPhase.ENDGAME);
        } else if(firstTurns < game.getNumberOfPlayers()) {
            dashboard.moveFaithMarker(firstTurns < 2 ? 0 : 1);
            firstTurns += 1;
            game.startUniquePhase(TurnPhase.FIRST_TURN);
        } else {
            game.startUniquePhase(TurnPhase.COMMON);
        }

        game.changePlayer();

        return game.getTurnPhase() == TurnPhase.ENDGAME && game.getCurrentPlayer().equals(game.getFirstPlayer());
    }

    /**
     * Getter for the game status.
     * @return the game status.
     */
    public Game getGameStatus() {
        return game.getGameStatus();
    }

}