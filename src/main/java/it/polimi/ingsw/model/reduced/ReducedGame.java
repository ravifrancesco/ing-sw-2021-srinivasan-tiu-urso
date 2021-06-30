package it.polimi.ingsw.model.reduced;

import it.polimi.ingsw.model.full.table.TurnPhase;
import it.polimi.ingsw.model.full.tokens.Token;

import java.util.*;

public class ReducedGame {

    private int numberOfPlayers;

    private boolean gameStarted;

    private String clientPlayer;

    private String currentPlayer;

    private Map<String, ReducedPlayer> players;

    private TurnPhase turnPhase;

    private final ReducedModel reducedModel;

    private int firstTurns;

    public Token getToken() {
        return token;
    }

    private Token token;

    public ReducedGame(ReducedModel reducedModel) {
        this.reducedModel = reducedModel;
        this.numberOfPlayers = 0;
        this.gameStarted = false;
        this.clientPlayer = "";
        this.players = new HashMap<>();
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

    public Map<String, ReducedPlayer> getPlayers() {
        return players;
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

    public void setToken(Token token) {
        this.token = token;
        this.reducedModel.updateToken(token);
    }
}
