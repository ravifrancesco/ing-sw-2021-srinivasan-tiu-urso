package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.exceptions.GameNotFullException;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.observerPattern.observables.GameObservable;
import it.polimi.ingsw.model.observerPattern.observers.GameObserver;
import it.polimi.ingsw.model.singlePlayer.tokens.*;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.model.GameError;

import java.util.*;
import java.util.stream.IntStream;

public class Game extends GameObservable  {



	private int firstTurns;

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
	private boolean gameStarted;

	private GameError gameError;

	private Stack<Token> tokens;
	private Token lastToken;

	public Game(String gameId, int numberOfPlayers) {
		this.gameId = gameId;
		this.numberOfPlayers = numberOfPlayers;
		this.players = new LinkedHashMap<>();
		this.playerOrder = players.keySet().iterator();
		this.gameBoard = new GameBoard();
		this.gameError = new GameError();
		this.tokens = new Stack<>();
	}

	public void loadGameSettings(GameSettings gameSettings) {
		this.gameSettings = gameSettings;
	}

	public void reset() {
		gameBoard.reset(gameSettings);
		players.values().forEach(Player::reset);
		this.playerOrder = players.keySet().iterator();
		this.gameEnded = false;
		this.gameStarted = false;
		resetTokens();
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
		// notify(this);
		return playerOrder.next();
	}

	public void startUniquePhase(TurnPhase turnPhase) {
		this.turnPhase = turnPhase;
		notify(this);
	}

	public TurnPhase getTurnPhase() {
		return turnPhase;
	}

	public void distributeCards() {
		Deck leaderDeck = gameBoard.getLeaderDeck();
		List<LeaderCard> leaderCards = new ArrayList<>();

		players.forEach((key, value) -> {
			IntStream.range(0, Hand.MAX_HAND_SIZE)
					.forEach(i -> leaderCards.add((LeaderCard) leaderDeck.getCard()));
			value.fillHand(leaderCards);
			leaderCards.clear();
		});


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
		System.out.println("Error is being set by " + nickname + " with the following message:");
		System.out.println(exception.getMessage());
		Pair<String, Exception> error = new Pair<>(nickname, exception);

		gameError.setError(error);
	}

	public void startGame() throws GameNotFullException {
		if (players.size() < numberOfPlayers) {
			notify(this);
			throw new GameNotFullException("Game Not Full");
		}
		this.gameStarted = true;
		notify(this);
	}

	public boolean isGameStarted() {
		return gameStarted;
	}


	public int getFirstTurns() {
		return firstTurns;
	}

	public void setFirstTurns(int firstTurns) {
		this.firstTurns = firstTurns;
	}

	public void resetTokens() {
		this.tokens = new Stack<>();
		this.tokens.addAll(Arrays
				.asList(new DiscardDevCardBlue(), new DiscardDevCardGreen(), new DiscardDevCardYellow(), new DiscardDevCardPurple(),
						new DoubleBlackCrossMoveToken(), new SingleBlackCrossMoveToken()));
		Collections.shuffle(this.tokens);
	}

	public void drawToken() {
		lastToken = this.tokens.peek();
		gameEnded = lastToken.useToken(this);
		notify(this);
	}

	public Stack<Token> getTokens() {
		return tokens;
	}

	public Token getLastToken() {
		return lastToken;
	}
}
