package it.polimi.ingsw.model;

import it.polimi.ingsw.model.observerPattern.observables.GameObservable;
import it.polimi.ingsw.model.observerPattern.observers.GameObserver;
import it.polimi.ingsw.server.Connection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class Game extends GameObservable {

	private String gameId;

	private final int numberOfPlayers;

	private String currentPlayer;
	private String firstPlayer;

	private LinkedHashMap<String, Player> players;
	private Iterator<String> playerOrder;

	private GameBoard gameBoard;

	private GameSettings gameSettings;

	private TurnPhase turnPhase;

	private boolean gameEnded;

	private GameError gameError;

	public Game(String gameId, int numberOfPlayers) {
		this.gameId = gameId;
		this.numberOfPlayers = numberOfPlayers;
		this.players = new LinkedHashMap<>();
		this.gameBoard = new GameBoard();
		this.gameError = new GameError();
	}

	public void loadGameSettings(GameSettings gameSettings) {
		this.gameSettings = gameSettings;
	}

	public void reset() {
		gameBoard.reset(gameSettings);
		players.values().forEach(Player::reset);
		this.playerOrder = players.keySet().iterator();
		this.gameEnded = false;
	}

	public boolean checkEnd() {
		return false;
	}

	public Player checkWinner() {
		return null;
	}

	public boolean checkDiscardedResources() {
		return false;
	}

	public void addPlayer(String nickname, Player p) {
		players.put(nickname, p);
	}

	public HashMap<String, Player> getPlayers() {
		return players;
	}

	public String getGameId() {
		return gameId;
	}

	public GameSettings getGameSettings() {
		return gameSettings;
	}

	public GameBoard getGameBoard() {
		return gameBoard;
	}

	public String getNextPlayer() {
		if (!playerOrder.hasNext()) {
			this.playerOrder = players.keySet().iterator();
		}
		return playerOrder.next();
	}

	public void startUniquePhase(TurnPhase turnPhase) {
		this.turnPhase = turnPhase;
	}

	public TurnPhase getTurnPhase() {
		return turnPhase;
	}

	public void endGame() {

	}

	public Game getGameStatus() {
		players.values().forEach(Player::updateVictoryPoints);
		return this;
	}

	public GameError getGameError() {
		return gameError;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public String getFirstPlayer() {
		return firstPlayer;
	}

	public void setFirstPlayer(String firstPlayer) {
		this.firstPlayer = firstPlayer;
	}

	public void changePlayer() {
		this.currentPlayer = getNextPlayer();
	}

	public void setError(Exception error, String nickname) {
		gameError.setError(error, nickname);
	}


}
