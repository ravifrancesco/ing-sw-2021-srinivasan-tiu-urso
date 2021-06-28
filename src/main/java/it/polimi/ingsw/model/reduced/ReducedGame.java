package it.polimi.ingsw.model.reduced;

import it.polimi.ingsw.model.full.table.TurnPhase;
import it.polimi.ingsw.model.full.tokens.Token;

import java.util.*;

public class ReducedGame {

    private String gameId;

    private int numberOfPlayers;

    private boolean gameStarted;

    private String clientPlayer;

    private String currentPlayer;

    private String firstPlayer;

    private Map<String, ReducedPlayer> players;

    private TurnPhase turnPhase;

    private ReducedModel reducedModel;

    private int firstTurns;

    public Token getToken() {
        return token;
    }

    private Stack<Token> tokens;
    private Token token;


    public boolean isGameEnded() {
        return gameEnded;
    }

    public boolean isEndGamePhase() {
        return endGamePhase;
    }

    private boolean gameEnded;

    private boolean endGamePhase;

    public ReducedGame(ReducedModel reducedModel) {
        this.reducedModel = reducedModel;
        this.gameId = "";
        this.numberOfPlayers = 0;
        this.gameStarted = false;
        this.clientPlayer = "";
        this.players = new HashMap<>();
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getClientPlayer() {
        return clientPlayer;
    }

    public void setClientPlayer(String clientPlayer) {
        this.clientPlayer = clientPlayer;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(String firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public Map<String, ReducedPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, ReducedPlayer> players) {
        this.players = players;
    }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    public void setTurnPhase(TurnPhase turnPhase) {
        this.turnPhase = turnPhase;
    }

    public void updatePlayers(List<String> playersNicknames) {
        // TODO add and remove players after disconnections
        playersNicknames.forEach(nickname -> this.players.putIfAbsent(nickname, new ReducedPlayer(reducedModel)));
    }

    public void createPlayer(String playerNickname) {
        players.putIfAbsent(playerNickname, new ReducedPlayer(reducedModel));
    }

    public void createPlayer(ReducedPlayer reducedPlayer) {
        players.put(reducedPlayer.getNickname(), reducedPlayer);
    }

    public void setGameStarted(boolean gameStarted) {
        if (!this.gameStarted && gameStarted) {
            this.gameStarted = gameStarted;
            this.reducedModel.hideStartGameAlert();
        }
    }

    public void setFirstTurns(int firstTurns) {
        this.firstTurns = firstTurns;
    }

    public int getFirstTurns() {
        return firstTurns;
    }

    public ReducedPlayer getReducedPlayer(String nickname) {
        return this.players.get(nickname);
    }

    public void setTokens(Stack<Token> tokens) {
        this.tokens = tokens;
    }

    public void setToken(Token token) {
        this.token = token;
        this.reducedModel.updateToken(token);
    }

    public void setEndGamePhase(boolean endGamePhase) {
    }

    public void setGameEnded(boolean gameEnded) {
    }
}
