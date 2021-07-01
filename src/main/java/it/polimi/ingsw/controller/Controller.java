package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.model.full.table.*;
import it.polimi.ingsw.model.utils.GameSettings;
import it.polimi.ingsw.model.utils.ResourceContainer;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

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

public class Controller {

    // TODO: check that illegal actions don't change the state
    private final Game game;
    private final ProductionController productionController;

    // controllers
    private final WarehouseController warehouseController;
    private final MarketController marketController;
    private final LeaderCardController leaderCardController;
    private final DevelopmentCardController developmentCardController;
    GameSettings gameSettings;

    /**
     * Constructor for a Server Controller object. It creates the game and initializes all attributes.
     *
     * @param gameId          the unique id of the game.
     * @param numberOfPlayers the number of players of the game.
     */

    public Controller(String gameId, int numberOfPlayers) throws IllegalArgumentException {
        if (numberOfPlayers < 1 || numberOfPlayers > 4) throw new IllegalArgumentException();
        this.game = new Game(gameId, numberOfPlayers);
        this.productionController = new ProductionController(this.game);
        this.warehouseController = new WarehouseController(this.game);
        this.marketController = new MarketController(this.game);
        this.leaderCardController = new LeaderCardController(this.game);
        this.developmentCardController = new DevelopmentCardController(this.game);
    }

    /**
     * Method to load the game settings.
     *
     * @param gameSettings the game settings to load.
     */
    public void loadGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
        game.loadGameSettings(gameSettings);
    }

    /**
     * Method to allow a player to join the game.
     *
     * @param nickname the nickname of the player who wants to join.
     * @throws GameFullException    if the game is already full.
     * @throws InvalidNameException if the game contains an other player with the same nickname.
     */
    public void joinGame(String nickname) throws GameFullException, InvalidNameException {
        if (game.getPlayers().size() >= game.getNumberOfPlayers()) {
            throw new GameFullException("Game " + this.game.getGameId() + " is full.");
        } else if (game.getPlayers().containsKey(nickname)) {
            throw new InvalidNameException("Nickname " + nickname + " is already in use");
        } else {
            game.addPlayer(nickname, new Player(gameSettings, nickname));
        }
    }

    public void leaveGame(String nickname) throws InvalidNameException {
        if (!game.getPlayers().containsKey(nickname)) {
            throw new InvalidNameException("Nickname " + nickname + " is not a part of the game");
        } else {
            // TODO reconnection?
            game.removePlayer(nickname);
        }
    }

    /**
     * Adds the connection as the observer of all observable classes
     *
     * @param c observer
     */
    public void addObservers(ServerVirtualView c, Map<String, ServerVirtualView> connectedPlayers) {

        game.addObserver(c);
        game.getGameError().addObserver(c);

        GameBoard gameBoard = game.getGameBoard();
        gameBoard.getMarket().addObserver(c);
        gameBoard.getDevelopmentCardGrid().addObserver(c);

        updatePlayerObservers(c, connectedPlayers);
    }

    //TODO doc
    public void updatePlayerObservers(ServerVirtualView c, Map<String, ServerVirtualView> connectedPlayers) {
        List<Player> players = new ArrayList<>(game.getPlayers().values());
        players.forEach(p -> p.addObserver(c));
        players.forEach(p -> p.getDashboard().addObserver(c));
        players.forEach(p -> p.getDashboard().getFaithTrack().addObserver(c));
        players.forEach(p -> p.getDashboard().getWarehouse().addObserver(c));

        Player currentPlayer = game.getPlayers().get(c.getNickname());
        connectedPlayers.values().stream().filter(connection -> !connection.getNickname().equals(currentPlayer.getNickname()))
                .forEach(connection -> {
                    currentPlayer.addObserver(connection);
                    currentPlayer.getDashboard().addObserver(connection);
                    currentPlayer.getDashboard().getFaithTrack().addObserver(connection);
                    currentPlayer.getDashboard().getWarehouse().addObserver(connection);
                });
    }

    // TODO doc
    public void addObserversLocal(OfflineClientVirtualView offlineClientVirtualView) {

        game.addObserver(offlineClientVirtualView);
        game.getGameError().addObserver(offlineClientVirtualView);

        GameBoard gameBoard = game.getGameBoard();
        gameBoard.getMarket().addObserver(offlineClientVirtualView);
        gameBoard.getDevelopmentCardGrid().addObserver(offlineClientVirtualView);

        updatePlayerObserversLocal(offlineClientVirtualView);

    }

    public void updatePlayerObserversLocal(OfflineClientVirtualView offlineClientVirtualView) {
        List<Player> players = new ArrayList<>(game.getPlayers().values());
        players.forEach(p -> p.addObserver(offlineClientVirtualView));
        players.forEach(p -> p.getDashboard().addObserver(offlineClientVirtualView));
        players.forEach(p -> p.getDashboard().getFaithTrack().addObserver(offlineClientVirtualView));
        players.forEach(p -> p.getDashboard().getWarehouse().addObserver(offlineClientVirtualView));

        Player currentPlayer = game.getPlayers().get(offlineClientVirtualView.getNickname());
        currentPlayer.addObserver(offlineClientVirtualView);
        currentPlayer.getDashboard().addObserver(offlineClientVirtualView);
        currentPlayer.getDashboard().getFaithTrack().addObserver(offlineClientVirtualView);
        currentPlayer.getDashboard().getWarehouse().addObserver(offlineClientVirtualView);

    }

    /**
     * Removes the connection from the observers of all observable classes
     *
     * @param c observer
     */
    public void removeObservers(ServerVirtualView c, Map<String, ServerVirtualView> connectedPlayers) {

        game.removeObserver(c);

        GameBoard gameBoard = game.getGameBoard();
        gameBoard.getMarket().removeObserver(c);
        gameBoard.getDevelopmentCardGrid().removeObserver(c);

        removePlayerObservers(c, connectedPlayers);
    }

    //TODO doc
    public void removePlayerObservers(ServerVirtualView c, Map<String, ServerVirtualView> connectedPlayers) {
        List<Player> players = new ArrayList<>(game.getPlayers().values());
        players.forEach(p -> p.removeObserver(c));
        players.forEach(p -> p.getDashboard().removeObserver(c));
        players.forEach(p -> p.getDashboard().getFaithTrack().removeObserver(c));
        players.forEach(p -> p.getDashboard().getWarehouse().removeObserver(c));

        Player currentPlayer = game.getPlayers().get(c.getNickname());
        connectedPlayers.values().stream().filter(connection -> !connection.getNickname().equals(currentPlayer.getNickname()))
                .forEach(connection -> {
                    currentPlayer.removeObserver(connection);
                    currentPlayer.getDashboard().removeObserver(connection);
                    currentPlayer.getDashboard().getFaithTrack().removeObserver(connection);
                    currentPlayer.getDashboard().getWarehouse().removeObserver(connection);
                });
    }

    /**
     * Starts the game.
     */
    public int startGame(String nickname) {
        try {
            game.reset();
            game.startGame();
            game.changePlayer();
            game.startUniquePhase(TurnPhase.FIRST_TURN);
            game.setFirstPlayer(game.getCurrentPlayer());
            game.distributeCards();
        } catch (Exception e) {
            game.setError(e, nickname);
            return -1;
        }
        return 0;
    }

    /**
     * It allows to discard the first three leader cards.
     *
     * @param nickname      the nickname of the player who made the move.
     * @param cardToDiscard the index of the card to be discarded.
     */
    public int discardExcessLeaderCards(String nickname, int cardToDiscard) {
        try {
            leaderCardController.discardExcessLeaderCards(nickname, cardToDiscard);
            return 0;
        } catch (WrongTurnException | WrongMoveException | CardNotPlayableException e) {
            game.setError(e, nickname);
            return -1;
        }
    }

    /**
     * It allows to get the initial resources of the game.
     *
     * @param nickname the nickname of the player who made the move.
     * @param resource the resource chosen by the player.
     * @param position the position where to store the resource.
     */
    public int getInitialResources(String nickname, Resource resource, int position) {
        String currentPlayer = game.getCurrentPlayer();
        if (!currentPlayer.equals(nickname)) {
            game.setError(new WrongTurnException("Not " + nickname + " turn"), nickname);
            return -1;
        } else if (!game.getTurnPhase().equals(TurnPhase.FIRST_TURN)) {
            game.setError(new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name()), nickname);
            return -1;
        }

        Player player = game.getPlayers().get(currentPlayer);
        Dashboard dashboard = player.getDashboard();

        if (checkInitialPhaseCompletion(dashboard)) {
            game.setError(new WrongMoveException(currentPlayer + "has already acquired all due resources"), nickname);
            return -1;
        }

        try {
            dashboard.storeResourceInDeposit(resource, position);
        } catch (IllegalArgumentException e) {
            game.setError(new DepositCellNotEmpty("Deposit cell " + position + " not empty"), nickname);
            return -1;
        } catch (IllegalStateException e) {
            game.setError(new IllegalDepositStateException("Invalid deposit state"), nickname);
            return -1;
        }
        return 0;
    }

    /**
     * Checks if the initial phase of the game is completed or not for the player.
     *
     * @param d the dashboard of the player.
     * @return true if the initial phase is completed, false otherwise.
     */

    private boolean checkInitialPhaseCompletion(Dashboard d) {
        int storedResources = d.getDepositResourceQty();
        return switch (game.getFirstTurns()) {
            case 0 -> storedResources >= 0;
            case 1, 2 -> storedResources >= 1;
            case 3 -> storedResources >= 2;
            default -> true;
        };
    }

    /**
     * It allows to play a leader card.
     *
     * @param nickname   the nickname of the player who made the move.
     * @param cardToPlay the index of the card to be played.
     */
    public int playLeaderCard(String nickname, int cardToPlay) {
        try {
            leaderCardController.playLeaderCard(nickname, cardToPlay);
            return 0;
        } catch (WrongTurnException | CardNotPlayableException | WrongMoveException e) {
            game.setError(e, nickname);
        }
        return 1;
    }

    /**
     * It allows to activate the production of a leader card.
     *
     * @param nickname                 the nickname of the player who made the move.
     * @param cardToActivate           the index of the card whose production power is to be activated.
     * @param resourcesToPayCost       the resources to pay for the resources required by the production power.
     * @param resourceRequiredOptional the resources required that replace the selectable resources (if present).
     * @param resourceProducedOptional the resources produced that replace the selectable resources (if present).
     */
    public int activateLeaderCardProductionPower(String nickname, int cardToActivate, ResourceContainer resourcesToPayCost,
                                                 Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) {

        try {
            productionController.activateLeaderCardProduction(nickname, cardToActivate, resourcesToPayCost, resourceRequiredOptional, resourceProducedOptional);
            checkVaticanReports();
            return 0;
        } catch (WrongTurnException | PowerNotActivatableException | WrongMoveException e) {
            game.setError(e, nickname);
        }
        return -1;
    }

    /**
     * It allows to discard a leader card.
     *
     * @param nickname      the nickname of the player who made the move.
     * @param cardToDiscard the index of the card to be discarded.
     */
    public int discardLeaderCard(String nickname, int cardToDiscard) {
        try {
            leaderCardController.discardLeaderCard(nickname, cardToDiscard);
            checkVaticanReports();
            return 0;
        } catch (WrongTurnException | CardNotPlayableException e) {
            game.setError(e, nickname);
            return -1;
        }
    }

    /**
     * It allows to activate the production power of the dashboard.
     *
     * @param nickname                 the nickname of the player who made the move.
     * @param resourcesToPayCost       the resources to pay for the resources required by the production power.
     * @param resourceRequiredOptional the resources required that replace the selectable resources (if present).
     * @param resourceProducedOptional the resources produced that replace the selectable resources (if present).
     */
    public int activateDashboardProductionPower(String nickname, ResourceContainer resourcesToPayCost,
                                                Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) {

        try {
            productionController.activateDashboardProduction(nickname, resourcesToPayCost, resourceRequiredOptional, resourceProducedOptional);
            return 0;
        } catch (WrongTurnException | PowerNotActivatableException | WrongMoveException e) {
            game.setError(e, nickname);
        }
        return -1;
    }

    /**
     * It allows to get resources from the market.
     *
     * @param nickname the nickname of the player who made the move.
     * @param move     the number that represents the selected row or column in the market.
     * @param wmrs     the collection of activated power related to the white marble resources.
     * @return 0 if the move is done, -1 if an error occurred.
     */
    public int getFromMarket(String nickname, int move, ArrayList<Resource> wmrs) {
        try {
            marketController.getFromMarket(nickname, move, wmrs);
            checkVaticanReports();
            return 0;
        } catch (WrongTurnException | WrongMoveException e) {
            game.setError(e, nickname);
            return -1;
        }
    }

    /**
     * It allows to buy a development card from the development card grid.
     *
     * @param nickname           the nickname of the player who made the move.
     * @param row                the row of the grid selected.
     * @param column             the column of the grid selected.
     * @param resourcesToPayCost the resources to pay for the cost of the development card.
     * @param position           the index that represent where to place the card.
     */
    public int buyDevelopmentCard(String nickname, int row, int column, ResourceContainer resourcesToPayCost, int position) {
        try {
            developmentCardController.buyDevelopmentCard(nickname, row, column, resourcesToPayCost, position);
            return 0;
        } catch (WrongTurnException | CardNotBuyableException | CardNotPlayableException | WrongMoveException e) {
            game.setError(e, nickname);
            return -1;
        }
    }

    /**
     * It allows to activate the production power of a development card.
     *
     * @param nickname                 the nickname of the player who made the move.
     * @param cardToActivate           the index of the card whose production power is to be activated
     * @param resourcesToPayCost       the resources to pay for the resources required by the production power.
     * @param resourceRequiredOptional the resources required that replace the selectable resources (if present).
     * @param resourceProducedOptional the resources produced that replace the selectable resources (if present).
     * @return 0 if the move is done, -1 if an error occurred.
     */
    public int activateDevelopmentCardProductionPower(String nickname, int cardToActivate, ResourceContainer resourcesToPayCost,
                                                      Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) {

        try {
            productionController.activateDevelopmentCardProductionPower(nickname, cardToActivate, resourcesToPayCost, resourceRequiredOptional, resourceProducedOptional);
            checkVaticanReports();
            return 0;
        } catch (WrongTurnException | PowerNotActivatableException | WrongMoveException e) {
            game.setError(e, nickname);
        }
        return -1;
    }

    /**
     * Moves resources between the deposit
     *
     * @param nickname the player nickname
     * @param deposit  the new deposit
     * @return 0 if the move is done, -1 if an error occurred.
     */
    public int changeDeposit(String nickname, Resource[] deposit) {
        try {
            warehouseController.changeResourcesDeposit(nickname, deposit);
            return 0;
        } catch (WrongTurnException | WrongMoveException | IllegalDepositStateException e) {
            game.setError(e, nickname);
            return -1;
        }
    }

    public void hack(String nickname) {
        game.getPlayers().get(nickname).getDashboard().getWarehouse().storeInLocker(Resource.GOLD, 999);
        game.getPlayers().get(nickname).getDashboard().getWarehouse().storeInLocker(Resource.STONE, 999);
        game.getPlayers().get(nickname).getDashboard().getWarehouse().storeInLocker(Resource.SHIELD, 999);
        game.getPlayers().get(nickname).getDashboard().getWarehouse().storeInLocker(Resource.SERVANT, 999);
    }

    /**
     * Allows to move resources between the deposit and extra deposit.
     *
     * @param nickname        the nickname of the player who made the move.
     * @param deposit      the new deposit with the moved resources
     * @param extraDeposit the new extra deposit with the moved resources
     * @param lcIndex         the index of the leader card where the extra deposit is
     * @return 0 if the move is done, -1 if an error occurred.
     */
    public void changeDepositExtraDeposit(String nickname, Resource[] deposit, Resource[] extraDeposit, int lcIndex) {
        try {
            warehouseController.changeResourcesDepositExtraDeposit(nickname, deposit, extraDeposit, lcIndex);
        } catch (WrongTurnException | IllegalDepositStateException | WrongMoveException e) {
            game.setError(e, nickname);
        }
    }

    /**
     * It allows to move the resources from the supply to the warehouse.
     *
     * @param nickname the nickname of the player who made the move.
     * @param from     the index of the supply where the resources are stored.
     * @param to       the index of the warehouse where to store the resources.
     */
    public int storeFromSupply(String nickname, int from, int to) {
        try {
            warehouseController.storeFromSupply(nickname, from, to);
            return 0;
        } catch (WrongTurnException | WrongMoveException | IllegalDepositStateException e) {
            game.setError(e, nickname);
            return -1;
        }
    }

    /**
     * It allows to move the resources from the supply to the extra deposit.
     *
     * @param nickname      the nickname of the player who made the move.
     * @param leaderCardPos the position of the leader card which has extra deposit special ability.
     * @param from          the index of the supply where the resources are stored.
     * @param to            the index of the extra deposit where to store the resources.
     */
    public int storeFromSupplyInExtraDeposit(String nickname, int leaderCardPos, int from, int to) {
        try {
            warehouseController.storeFromSupplyInExtraDeposit(nickname, leaderCardPos, from, to);
            return 0;
        } catch (WrongTurnException | WrongMoveException | IllegalDepositStateException e) {
            game.setError(e, nickname);
            return -1;
        }
    }

    /**
     * It allows to end a turn.
     *
     * @param nickname the nickname of the player in turn.
     * @return true if the game is ended, false otherwise.
     */
    public int endTurn(String nickname) {
        String currentPlayer = game.getCurrentPlayer();
        if (!currentPlayer.equals(nickname)) {
            game.setError(new WrongTurnException("Not " + nickname + " turn"), nickname);
            return -1;
        }
        // no turn phase check needed: player may stupidly pass the turn whilst having done nothing.

        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();

        if (player.getHandSize() > 2) {
            game.setError(new LeaderCardInExcessException(currentPlayer + " hasn't discarded enough cards"), nickname);
            return -1;
        }

        int faithPoints = dashboard.discardResources();

        game.getPlayers()
                .entrySet()
                .stream()
                .filter(p -> !p.getKey().equals(currentPlayer))
                .forEach(p -> p.getValue().getDashboard().moveFaithMarker(faithPoints));

        dashboard.resetProductionPowers();

        if (game.getTurnPhase().equals(TurnPhase.FIRST_TURN) && !checkInitialPhaseCompletion(dashboard)) {
            game.setError(new WrongMoveException(currentPlayer + " has not acquired all due resources."), nickname);
            return -1;
        }


        if (game.getFirstTurns() < game.getNumberOfPlayers() - 1) {
            dashboard.moveFaithMarker(game.getFirstTurns() < 2 ? 0 : 1);
            game.setFirstTurns(game.getFirstTurns() + 1);
            game.startUniquePhase(TurnPhase.FIRST_TURN);
        } else {
            game.startUniquePhase(TurnPhase.COMMON);
        }

        checkVaticanReports();


        if (player.getDashboard().checkGameEnd() && !game.isEndGamePhase()) {
            game.setEndGamePhase(true);
        }

        game.changePlayer();

        if (game.isEndGamePhase() && game.getCurrentPlayer().equals(game.getFirstPlayer())) {
            game.endGame();
            return 1;
        }
        return 0;

    }

    // TODO doc
    public int endTurnSinglePlayer(String nickname) {
        String currentPlayer = game.getCurrentPlayer();
        if (!currentPlayer.equals(nickname)) {
            game.setError(new WrongTurnException("Not " + nickname + " turn"), nickname);
            return -1;
        }
        // no turn phase check needed: player may stupidly pass the turn whilst having done nothing.

        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();

        if (player.getHandSize() > 2) {
            game.setError(new LeaderCardInExcessException(currentPlayer + " hasn't discarded enough cards"), nickname);
            return -1;
        }

        int faithPoints = dashboard.discardResources();

        game.getPlayers()
                .entrySet()
                .stream()
                .filter(p -> !p.getKey().equals(currentPlayer))
                .forEach(p -> p.getValue().getDashboard().moveFaithMarker(faithPoints));

        dashboard.resetProductionPowers();

        if (game.getTurnPhase().equals(TurnPhase.FIRST_TURN) && !checkInitialPhaseCompletion(dashboard)) {
            game.setError(new WrongMoveException(currentPlayer + " has not acquired all due resources."), nickname);
            return -1;
        }

        game.drawToken();

        if (player.getDashboard().checkGameEnd() || game.checkEnd()) {
            game.endGame();
            return 1;
        } else if (game.getFirstTurns() < game.getNumberOfPlayers() - 1) {
            dashboard.moveFaithMarker(game.getFirstTurns() < 2 ? 0 : 1);
            game.setFirstTurns(game.getFirstTurns() + 1);
            game.startUniquePhase(TurnPhase.FIRST_TURN);
        } else {
            game.startUniquePhase(TurnPhase.COMMON);
        }

        checkVaticanReports();

        game.changePlayer();

        return 0;
    }


    /**
     * Getter for the game status.
     *
     * @return the game status.
     */
    public Game getGameStatus() {
        return game.getGameStatus();
    }

    public void gimme(String nickname, int row, int column, int index) {
        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();
        GameBoard gameBoard = game.getGameBoard();
        DevelopmentCardGrid developmentCardGrid = gameBoard.getDevelopmentCardGrid();

        DevelopmentCard developmentCard = developmentCardGrid.peek(row, column);

        dashboard.placeDevelopmentCard(developmentCard, index);

        developmentCardGrid.buy(row, column);
        game.startUniquePhase(TurnPhase.BUY);
    }

    public void play(String nickname, int index) {
        Player player = game.getPlayers().get(nickname);
        player.playLeaderCard(index);
    }

    private void checkVaticanReports() {
        game.updateMaxReached();
        game.getPlayers().values()
                .stream().map(p -> p.getDashboard().getFaithTrack())
                .forEach(f -> f.checkVaticanVictoryPoints(game.getMaxReached()));
    }

    public int getNumberOfPlayers() {
        return this.game.getNumberOfPlayers();
    }


    public void end(String nickname) {
        Player player = game.getPlayers().get(nickname);
        player.getDashboard().moveFaithMarker(23);
    }
}

