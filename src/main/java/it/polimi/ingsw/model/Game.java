package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.observerPattern.observables.GameObservable;
import it.polimi.ingsw.model.observerPattern.observers.GameObserver;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.model.GameError;

import java.util.*;
import java.util.stream.IntStream;

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
		this.playerOrder = players.keySet().iterator();
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
		players.values().forEach(Player::reset);
		this.gameEnded = false;
		notify(this);

	}

	public boolean checkEnd() {
		return gameEnded;
	}

	public Player checkWinner() {
		int winnerPoints  = players.values().stream().map(Player::getVictoryPoints).max(Comparator.naturalOrder()).get();

		return players.values().stream().filter(player -> player.getVictoryPoints() == winnerPoints).findFirst().get();
	}

	public void addPlayer(String nickname, Player p) {
		players.put(nickname, p);
		notify(this);
	}

	public void removePlayer(String nickname) {
		players.remove(nickname);
		notify(this);
	}

	public LinkedHashMap<String, Player> getPlayers() {
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

	public void distributeCards() {
		IntStream.range(0, 4).forEach(i ->
				players.values().forEach(p ->
						p.addCard((LeaderCard) gameBoard.getLeaderDeck().getCard())
				)
		);

	}

	public void endGame() {
		gameEnded = true;
		// TODO
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
		notify(this);
	}

	public void changePlayer() {
		this.currentPlayer = getNextPlayer();
		notify(this);
	}

	public void setError(Exception exception, String nickname) {
		Pair<String, Exception> error = new Pair<>(nickname, exception);

		gameError.setError(error);
	}
}
