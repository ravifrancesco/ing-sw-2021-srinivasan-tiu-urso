package it.polimi.ingsw.model;

import it.polimi.ingsw.model.observerPattern.observables.GameObservable;
import it.polimi.ingsw.model.observerPattern.observers.GameObserver;
import it.polimi.ingsw.server.Connection;

import java.util.*;

public class Game extends GameObservable {

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
		this.gameBoard = new GameBoard();
		this.playerOrder = players.keySet().iterator();

	}

	public void loadGameSettings(GameSettings gameSettings) {
		this.gameSettings = gameSettings;
	}

	public void reset() {
		gameBoard.reset(gameSettings);
		players.values().forEach(Player::reset);
		this.gameEnded = false;
	}

	public boolean checkEnd() {
		return gameEnded;
	}

	public Player checkWinner() {
		int winnerPoints  = players.values().stream().map(Player::getVictoryPoints).min(Comparator.reverseOrder()).get();

		return players.values().stream().filter(player -> player.getVictoryPoints() == winnerPoints).findFirst().get();
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
		gameEnded = true;
		// TODO
	}

	public Game getGameStatus() {
		players.values().forEach(Player::updateVictoryPoints);
		return this;
	}

}