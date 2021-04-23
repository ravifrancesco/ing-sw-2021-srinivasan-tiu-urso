package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.Pair;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.specialAbilities.*;

import java.util.ArrayList;
import java.util.Map;

public class ServerController {

    // TODO: add checks for ResourceSelector legality


    private final Game game;

    private final int numberOfPlayers;

    private String currentPlayer;
    private String firstPlayer;

    private int firstTurns;

    public ServerController(String gameId, int numberOfPlayers) {
        this.game = new Game(gameId);
        this.numberOfPlayers = numberOfPlayers;
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
            game.addPlayer(nickname, new Player());
        }
    }

    public void reset() {
        game.reset();
        currentPlayer = game.getNextPlayer();
        firstPlayer = currentPlayer;
        firstTurns = 0;
    }

    public void startGame() {
        // TODO
        GameBoard gameBoard = game.getGameBoard();
        game.startUniquePhase(TurnPhase.FIRST_TURN);
    }

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
        player.discardLeaderCardInExcess(cardToDiscard, gameboard);
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

    // TODO storeFromSupplyInExtraDeposit


    public boolean checkInitialPhaseCompletion(Dashboard d) {
        int storedResources = d.getDepositResourceQty();
        return switch(firstTurns) {
            case 0 -> storedResources >= 0;
            case 1, 2 -> storedResources >= 1;
            case 3 -> storedResources >= 2;
            default -> true;
        };
    }

    public void playLeaderCard(String nickname, int cardToPlay) throws WrongTurnException, CardNotPlayableException {

        // TODO allow the player choose the position or change Card interface

        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (game.getTurnPhase().equals(TurnPhase.FIRST_TURN)) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();
        Map<Banner, Integer> playerBanners = dashboard.getBanners();

        if (cardToPlay >= player.getHand().getCards().size() || cardToPlay < 0) {
            throw new CardNotPlayableException("Invalid index");
        } else if (!player.getFromHand(cardToPlay).isPlayable(playerBanners)) {
            throw new CardNotPlayableException("Not enough resources or banners");
        }

        int position=0;
        try {
            player.playLeaderCard(cardToPlay, position);
        }
        catch (IllegalStateException e) { throw new CardNotPlayableException("Leader Card places are full"); }
        catch (IllegalArgumentException e) { throw new CardNotPlayableException("Position given is already full"); }

    }

    public void activateLeaderCardProduction(String nickname, int cardToActivate, ResourceContainer resourcesToPayCost) throws WrongTurnException, PowerNotActivatableException {

        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (!(game.getTurnPhase().equals(TurnPhase.COMMON) || game.getTurnPhase().equals(TurnPhase.PRODUCTION))) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();

        if (cardToActivate < 0 || cardToActivate > 1 || dashboard.getLeaderCard(cardToActivate) == null) {
            throw new PowerNotActivatableException("Invalid index");
        }

        SpecialAbility specialAbility = dashboard.getLeaderCard(cardToActivate).getSpecialAbility();

        if (!specialAbility.getType().equals(SpecialAbilityType.PRODUCTION_POWER)) {
            throw new PowerNotActivatableException("Card doesn't have a production power special ability");
        }

        ProductionPower productionPower = (ProductionPower) specialAbility;
        Map<Resource, Integer> playerResources = dashboard.getAllPlayerResources();

        if (!productionPower.isActivatable()) {
            throw new PowerNotActivatableException("Production already activated");
        }
        if (!productionPower.isActivatable(playerResources)) {
            throw new PowerNotActivatableException("Not enough resources");
        }

        if (game.getTurnPhase().equals(TurnPhase.COMMON)) {
            game.startUniquePhase(TurnPhase.PRODUCTION);
        }

        productionPower.activate(player);
        dashboard.payPrice(resourcesToPayCost);

        // TODO choice of selectable resources

    }

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

    public void activateDashboardProduction(String nickname, ResourceContainer resourcesToPayCost) throws WrongTurnException, PowerNotActivatableException {

        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (!(game.getTurnPhase().equals(TurnPhase.COMMON) || game.getTurnPhase().equals(TurnPhase.PRODUCTION))) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();
        ProductionPower productionPower = dashboard.getDashBoardProductionPower();
        Map<Resource, Integer> playerResources = dashboard.getAllPlayerResources();

        if (!productionPower.isActivatable()) {
            throw new PowerNotActivatableException("Production already activated");
        }
        if (!productionPower.isActivatable(playerResources)) {
            throw new PowerNotActivatableException("Not enough resources");
        }

        if (game.getTurnPhase().equals(TurnPhase.COMMON)) {
            game.startUniquePhase(TurnPhase.PRODUCTION);
        }

        productionPower.activate(player);
        dashboard.payPrice(resourcesToPayCost);

        //TODO choice of selectable resources

    }

    public void getFromMarket(String nickname, int move, ArrayList<WhiteMarbleResource> wmrs) throws WrongTurnException, WrongMoveException {
        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (!game.getTurnPhase().equals(TurnPhase.COMMON)) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(nickname);

        if (!player.checkWMR(wmrs)) {
            // check that each marble in WMRS is actually an activated special ability
            throw new WrongMoveException(currentPlayer + " doesn't have these white marble resources");
        }

        GameBoard gameboard = game.getGameBoard();
        Market market = gameboard.getMarket();
        Dashboard dashboard = player.getDashboard();

        if (move < 0 || move > 6) {
            throw new WrongMoveException("Desired move is out of index");
        }

        game.startUniquePhase(TurnPhase.MARKET);

        ArrayList<Resource> marketRes = market.getResources(move, player);
        // marketRes will return an arrayList of resources, where the white marbles will be converted to Resource.ANY
        // if two whiteMarbleResource special abilities are activated (see WhiteMarble.getRes()) -- this doesn't happen
        // when only WMR is activated.


        // check to see that the WMRS size corresponds to the amount of white marbles (against hacked clients)
        if (marketRes.stream().filter(r -> r == Resource.ANY).count() != wmrs.size()) {
            throw new IllegalArgumentException(currentPlayer + " asked for too many white marble transformed resources.");
        }

        // removes all ANYs
        marketRes.remove(Resource.ANY);
        // substitutes with the user's choice of returned resource
        wmrs.forEach(r -> marketRes.add(r.getRes()));

        dashboard.addResourcesToSupply(marketRes);
    }

    public void buyDevelopmentCard(String nickname, int row, int column, ResourceContainer resourcesToPayCost, int position)
            throws WrongTurnException, CardNotBuyableException, CardNotPlayableException {

        if (!currentPlayer.equals(nickname)) {
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
                throw new CardNotBuyableException("Not enough resources");
            }
        } catch (IllegalArgumentException e) {
            throw new CardNotBuyableException("Card doesn't exist");
        }

        developmentCard = developmentCardGrid.buy(row, column);
        game.startUniquePhase(TurnPhase.BUY);

       try {
           dashboard.placeDevelopmentCard(developmentCard, position);
       }
       catch (IllegalStateException e) {
           throw new CardNotPlayableException("Not a valid index");
       }

       dashboard.payPrice(resourcesToPayCost);
    }

    public void activateDevelopmentCardProductionPower(String nickname, int cardToActivate, ResourceContainer resourcesToPayCost)
            throws WrongTurnException, PowerNotActivatableException {

        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (!game.getTurnPhase().equals(TurnPhase.COMMON) || !game.getTurnPhase().equals(TurnPhase.PRODUCTION)) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();

        if (cardToActivate < 0 || cardToActivate > 2 || dashboard.getDevelopmentCard(cardToActivate) == null) {
            throw new PowerNotActivatableException("Invalid index");
        }

        DevelopmentCard developmentCard = dashboard.getDevelopmentCard(cardToActivate);
        ProductionPower productionPower = developmentCard.getProductionPower();
        Map<Resource, Integer> playerResources = dashboard.getAllPlayerResources();

        if (!productionPower.isActivatable(playerResources)) {
            throw new PowerNotActivatableException("Not enough resources");
        }

        if (game.getTurnPhase().equals(TurnPhase.COMMON)) {
            game.startUniquePhase(TurnPhase.PRODUCTION);
        }

        developmentCard.activate(player);
        dashboard.payPrice(resourcesToPayCost);

        // TODO choice of selectable resources
    }

    public void moveResources(String nickname, ArrayList<Pair<Integer, Integer>> moves) throws WrongTurnException, WrongMoveException, IllegalDepositStateException {
        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        }

        Player player = game.getPlayers().get(currentPlayer);
        Dashboard dashboard = player.getDashboard();

        try {
            dashboard.moveDepositResources(moves);
        } catch (IllegalArgumentException e) {
            throw new WrongMoveException("Invalid index");
        } catch (IllegalStateException e) {
            throw new IllegalDepositStateException("Invalid deposit state");
        }
    }

    public void storeFromSupply(String nickname, int from, int to) throws WrongTurnException, WrongMoveException, IllegalDepositStateException {
        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        }

        Player player = game.getPlayers().get(currentPlayer);
        Dashboard dashboard = player.getDashboard();

        try {
            dashboard.storeFromSupply(from, to);
        } catch (IllegalArgumentException e) {
            throw new WrongMoveException("Invalid index");
        } catch (IllegalStateException e) {
            throw new IllegalDepositStateException("Invalid deposit state");
        }
    }


    public void moveResources(String nickname, int leaderCardPosition, int from, int to) throws WrongTurnException, WrongMoveException, IllegalDepositStateException {
        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        }

        Player player = game.getPlayers().get(currentPlayer);
        Dashboard dashboard = player.getDashboard();

        try {
            dashboard.moveExtraDepositResources(leaderCardPosition, from, to);
        } catch (IllegalArgumentException e) {
            throw new WrongMoveException("Invalid index");
        } catch (IllegalStateException e) {
            throw new IllegalDepositStateException("Invalid extra deposit state");
        }
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