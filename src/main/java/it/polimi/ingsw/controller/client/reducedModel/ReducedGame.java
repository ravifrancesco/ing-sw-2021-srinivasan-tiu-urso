package it.polimi.ingsw.controller.client.reducedModel;

import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.model.specialAbilities.ProductionPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReducedGame {

    private String gameId;

    private int numberOfPlayers;

    private String clientPlayer;

    private String currentPlayer;

    private String firstPlayer;

    private Map<String, ReducedPlayer> players;

    private TurnPhase turnPhase;

    private int firstTurns;

    public ReducedGame() {
        this.gameId = "";
        this.numberOfPlayers = 0;
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
        playersNicknames.forEach(nickname -> this.players.putIfAbsent(nickname, new ReducedPlayer()));
    }

    public void createPlayer(String playerNickname) {
        players.putIfAbsent(playerNickname, new ReducedPlayer());
    }

    public void createPlayer(ReducedPlayer reducedPlayer) {
        players.put(reducedPlayer.getNickname(), reducedPlayer);
    }

    public void setFirstTurns(int firstTurns) {
        this.firstTurns = firstTurns;
    }

    public int getFirstTurns() {
        return firstTurns;
    }
}
