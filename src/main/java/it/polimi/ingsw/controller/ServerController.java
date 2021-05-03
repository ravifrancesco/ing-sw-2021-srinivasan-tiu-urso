package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.Pair;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.specialAbilities.*;

import java.util.ArrayList;
import java.util.Map;

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

    public ServerController(String gameId, int numberOfPlayers) {
        this.game = new Game(gameId);
        this.numberOfPlayers = numberOfPlayers;
        this.productionController = new ProductionController(this.game);
        this.warehouseController = new WarehouseController(this.game);
        this.marketController = new MarketController(this.game);
        this.leaderCardController = new LeaderCardController(this.game);
        this.developmentCardController = new DevelopmentCardController(this.game);
    }

    public void loadGameSettings(GameSettings gameSettings) {
        game.loadGameSettings(gameSettings);
    }

    public void JoinGame(String nickname) throws GameFullException, NicknameException {
        if (game.getPlayers().size() >= numberOfPlayers) {
            throw new GameFullException("Game " + this.game.getGameId() + " is full.");
        } else if (game.getPlayers().containsKey(nickname)) {
            throw new NicknameException("Nickname " + nickname + " is already in use");
        } else {
            game.addPlayer(nickname, new Player(gameSettings));
        }
    }

    public void reset() {
        game.reset();
        currentPlayer = game.getNextPlayer();
        firstPlayer = currentPlayer;
        firstTurns = 0;
    }

    public void startGame() {
        GameBoard gameBoard = game.getGameBoard();
        game.startUniquePhase(TurnPhase.FIRST_TURN);
    }

    public void discardExcessLeaderCards(String nickname, int cardToDiscard) throws WrongTurnException, WrongMoveException, CardNotPlayableException {
        leaderCardController.setCurrentPlayer(this.currentPlayer);
        leaderCardController.discardExcessLeaderCards(nickname, cardToDiscard);
    }

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

    public boolean checkInitialPhaseCompletion(Dashboard d) {
        int storedResources = d.getDepositResourceQty();
        return switch(firstTurns) {
            case 0 -> storedResources >= 0;
            case 1, 2 -> storedResources >= 1;
            case 3 -> storedResources >= 2;
            default -> true;
        };
    }

    public void playLeaderCard(String nickname, int cardToPlay, int position) throws WrongTurnException, CardNotPlayableException {
        leaderCardController.setCurrentPlayer(this.currentPlayer);
        leaderCardController.playLeaderCard(nickname, cardToPlay, position);
    }

    public void activateLeaderCardProduction(String nickname, int cardToActivate, ResourceContainer resourcesToPayCost,
                                             Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) throws WrongTurnException, PowerNotActivatableException, WrongMoveException {

        productionController.setCurrentPlayer(this.currentPlayer);
        productionController.activateLeaderCardProduction(nickname, cardToActivate, resourcesToPayCost, resourceRequiredOptional, resourceProducedOptional);
    }


    public void discardLeaderCard(String nickname, int cardToDiscard) throws WrongTurnException, CardNotPlayableException {
        leaderCardController.setCurrentPlayer(this.currentPlayer);
        leaderCardController.discardLeaderCard(nickname, cardToDiscard);
    }

    public void activateDashboardProduction(String nickname, ResourceContainer resourcesToPayCost,
                                            Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) throws WrongTurnException, PowerNotActivatableException, WrongMoveException {

        productionController.setCurrentPlayer(this.currentPlayer);
        productionController.activateDashboardProduction(nickname, resourcesToPayCost, resourceRequiredOptional, resourceProducedOptional);
    }

    public void getFromMarket(String nickname, int move, ArrayList<WhiteMarbleResource> wmrs) throws WrongTurnException, WrongMoveException {
        marketController.setCurrentPlayer(this.currentPlayer);
        marketController.getFromMarket(nickname, move, wmrs);
    }

    public void buyDevelopmentCard(String nickname, int row, int column, ResourceContainer resourcesToPayCost, int position)
            throws WrongTurnException, CardNotBuyableException, CardNotPlayableException, WrongMoveException {

        developmentCardController.setCurrentPlayer(this.currentPlayer);
        developmentCardController.buyDevelopmentCard(nickname, row, column, resourcesToPayCost, position);
    }

    public void activateDevelopmentCardProductionPower(String nickname, int cardToActivate, ResourceContainer resourcesToPayCost,
                                                       Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) throws WrongTurnException, PowerNotActivatableException, WrongMoveException {

        productionController.setCurrentPlayer(this.currentPlayer);
        productionController.activateDevelopmentCardProductionPower(nickname, cardToActivate, resourcesToPayCost, resourceRequiredOptional, resourceProducedOptional);
    }

    public void moveResourcesDepositDeposit(String nickname, Pair<Integer, Integer> move) throws WrongTurnException, WrongMoveException, IllegalDepositStateException {
        warehouseController.setCurrentPlayer(this.currentPlayer);
        warehouseController.moveResourcesDepositDeposit(nickname, move);
    }

    /**
     * Swaps two resources from a deposit to an extraDeposit (or viceversa).
     * @param nickname player nickname
     * @param move a Pair instance containing the two indexes to swap
     * @param lcPos an integer indicating which one of the Pair indexes is the extra deposit one
     * @param extraDepositIndex the integer representing the leader card which has the extra deposit
     * @throws WrongTurnException if it is not the player's turn
     * @throws IllegalDepositStateException if the move would create an illegal deposit
     * @throws WrongMoveException if one or more indexes are illegal
     */
    public void moveResourceDepositExtraDeposit(String nickname, Pair<Integer, Integer> move, int lcPos, int extraDepositIndex) throws WrongTurnException, WrongMoveException, IllegalDepositStateException {
        warehouseController.setCurrentPlayer(this.currentPlayer);
        warehouseController.moveResourcesDepositExtraDeposit(nickname, move, lcPos, extraDepositIndex);

    }

    public void storeFromSupply(String nickname, int from, int to) throws WrongTurnException, WrongMoveException, IllegalDepositStateException {
        warehouseController.setCurrentPlayer(this.currentPlayer);
        warehouseController.storeFromSupply(nickname, from, to);
    }

    public void storeFromSupplyInExtraDeposit(String nickname, int leaderCardPos, int from, int to) throws WrongTurnException, WrongMoveException, IllegalDepositStateException {
        warehouseController.setCurrentPlayer(this.currentPlayer);
        warehouseController.storeFromSupplyInExtraDeposit(nickname, leaderCardPos, from, to);
    }


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

    public Game getGameStatus() {
        return game.getGameStatus();
    }
}