package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.*;

import java.util.Map;

public class ServerController {


    private final Game game;

    private final int numberOfPlayers;
    private String currentPlayer;

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
        currentPlayer = game.getNextPlayer();
        game.init();
    }

    public void startGame() {
        GameBoard gameBoard = game.getGameBoard();

    }

    public void playLeaderCard(String nickname, int cardToPlay) throws WrongTurnException, CardNotPlayableException {

        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        }

        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();
        Map<Resource, Integer> playerResources = dashboard.getResources();
        Map<Banner, Integer> playerBanners = dashboard.getBanners();

        if (cardToPlay >= player.getHand().getCards().size() || cardToPlay < 0) {
            throw new CardNotPlayableException("Invalid index");
        } else if (!player.getFromHand(cardToPlay).isPlayable(playerResources, playerBanners)) {
            throw new CardNotPlayableException("Not enough resources or banners");
        }

        player.playLeaderCard(cardToPlay);

    }

    public void activateLeaderCardProduction(String nickname, int cardToActivate) throws WrongTurnException, PowerNotActivatableException {

        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (!game.getTurnPhase().equals(TurnPhase.COMMON) || !game.getTurnPhase().equals(TurnPhase.PRODUCTION)) {
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
        Map<Resource, Integer> playerResources = dashboard.getResources();

        if (!productionPower.isActivatable(playerResources)) {
            throw new PowerNotActivatableException("Not enough resources");
        }

        if (game.getTurnPhase().equals(TurnPhase.COMMON)) {
            game.startUniquePhase(TurnPhase.PRODUCTION);
        }

        productionPower.activate(player);

    }

    public void discardLeaderCard(String nickname, int cardToDiscard) throws WrongTurnException, CardNotPlayableException {

        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        }

        Player player = game.getPlayers().get(nickname);

        if (cardToDiscard >= player.getHand().getCards().size() || cardToDiscard < 0) {
            throw new CardNotPlayableException("Invalid index");
        }

        GameBoard gameBoard = game.getGameBoard();
        player.discardLeaderCard(cardToDiscard, gameBoard);

    }
  
    public void activateDashboardProduction(String nickname) throws WrongTurnException, PowerNotActivatableException {

        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (!game.getTurnPhase().equals(TurnPhase.COMMON) || !game.getTurnPhase().equals(TurnPhase.PRODUCTION)) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();
        ProductionPower productionPower = dashboard.getDashBoardProductionPower();
        Map<Resource, Integer> playerResources = dashboard.getResources();

        if (!productionPower.isActivatable(playerResources)) {
            throw new PowerNotActivatableException("Not enough resources");
        }

        if (game.getTurnPhase().equals(TurnPhase.COMMON)) {
            game.startUniquePhase(TurnPhase.PRODUCTION);
        }

        productionPower.activate(player);

    }

    public void getFromMarket(String nickname, int move) throws WrongTurnException, WrongMoveException {
        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (!game.getTurnPhase().equals(TurnPhase.COMMON)) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }
        if(game.getTurnPhase().equals(TurnPhase.COMMON)) {
            game.startUniquePhase(TurnPhase.MARKET);
        }

        Player player = game.getPlayers().get(nickname);
        GameBoard gameboard = game.getGameBoard();
        Market market = gameboard.getMarket();

        if(move < 0 || move > 6) { throw new WrongMoveException("Desired move is out of index!"); }

        if(game.getTurnPhase().equals(TurnPhase.COMMON)) {
            game.startUniquePhase(TurnPhase.MARKET);
        }

        market.getResources(move, player);
    }


    public void discardResource(String nickname) {

    }



}
