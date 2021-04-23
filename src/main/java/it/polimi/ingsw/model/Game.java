package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class Game {

	private String gameId;

	private LinkedHashMap<String, Player> players;
	private Iterator<String> playerOrder;

	private GameBoard gameBoard;

	private GameSettings gameSettings;

	private TurnPhase turnPhase;

	private boolean gameEnded;

	public Game(String gameId) {
		this.gameId = gameId;
		this.players = new LinkedHashMap<>();
	}

	public void loadGameSettings(GameSettings gameSettings) {
		this.gameSettings = gameSettings;
	}

	public void reset() {
		gameBoard.reset(gameSettings);
		players.values().forEach(p -> p.reset(gameSettings));
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
}
