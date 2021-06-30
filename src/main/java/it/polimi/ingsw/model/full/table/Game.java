package it.polimi.ingsw.model.full.table;

import it.polimi.ingsw.controller.exceptions.GameNotFullException;
import it.polimi.ingsw.model.utils.GameError;
import it.polimi.ingsw.model.utils.GameSettings;
import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.model.observerPattern.observables.GameObservable;
import it.polimi.ingsw.model.full.tokens.*;
import it.polimi.ingsw.utils.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game extends GameObservable  {



	private int firstTurns;

	private final String gameId;


	private final int numberOfPlayers;

	private String currentPlayer;
	private String firstPlayer;

	private final LinkedHashMap<String, Player> players;
	private Iterator<String> playerOrder;

	private final GameBoard gameBoard;

	private GameSettings gameSettings;

	private TurnPhase turnPhase;

	private boolean gameEnded;
	private boolean gameStarted;

	private boolean endGamePhase;

	private final GameError gameError;

	private int maxReached;

	private Stack<Token> tokens;
	private Token lastToken;

	/**
	 * The game constructor
	 * @param gameId generated id
	 * @param numberOfPlayers the number of players
	 */
	public Game(String gameId, int numberOfPlayers) {
		this.gameId = gameId;
		this.numberOfPlayers = numberOfPlayers;
		this.players = new LinkedHashMap<>();
		this.playerOrder = players.keySet().iterator();
		this.gameBoard = new GameBoard();
		this.gameError = new GameError();
		this.tokens = new Stack<>();
	}

	/**
	 * Loads game settigns
	 * @param gameSettings the game settings to load
	 */
	public void loadGameSettings(GameSettings gameSettings) {
		this.gameSettings = gameSettings;
	}


	/**
	 * Resets the whole game (can also be seen as an initializer, distributing all cards and preparing the game to start)
	 */
	public void reset() {
		gameBoard.reset(gameSettings);
		players.values().forEach(Player::reset);
		this.playerOrder = players.keySet().iterator();
		this.gameEnded = false;
		this.gameStarted = false;
		this.endGamePhase = false;
		this.maxReached = 0;
		resetTokens();
		notify(this);
	}


	public void setEndGamePhase(boolean endGamePhase) {
		this.endGamePhase = endGamePhase;
	}

	public boolean isEndGamePhase() {
		return endGamePhase;
	}

	public boolean getGameEnded() {
		return gameEnded;
	}

	public boolean checkEnd() {
		return gameEnded;
	}

	/**
	 * Returns the winner
	 * @return the winner
	 */
	public Player checkWinner() {
		int winnerPoints  = players.values().stream().map(Player::getVictoryPoints).max(Comparator.naturalOrder()).get();

		return players.values().stream().filter(player -> player.getVictoryPoints() == winnerPoints).findFirst().get();
	}

	/**
	 * Adds a player to the game
	 * @param nickname the player nickname
	 * @param p the player
	 */
	public void addPlayer(String nickname, Player p) {
		players.put(nickname, p);
		notify(this);
	}

	/**
	 * Removes a player from the game
	 * @param nickname
	 */
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

	/**
	 * Returns the nickname of the next player in turn
	 * @return the next player
	 */
	public String getNextPlayer() {
		if (!playerOrder.hasNext()) {
			this.playerOrder = players.keySet().iterator();
		}
		return playerOrder.next();
	}

	/**
	 * Starts an unique phase in the game
	 * @param turnPhase the turn phase to start
	 */
	public void startUniquePhase(TurnPhase turnPhase) {
		this.turnPhase = turnPhase;
		notify(this);
	}

	public TurnPhase getTurnPhase() {
		return turnPhase;
	}

	/**
	 * Fills the hand of every player with leader cards
	 */
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

	/**
	 * End the game by setting the gameEnded boolean to true and calling notify so the server can handle it
	 */
	public void endGame() {
		gameEnded = true;
		notify(this);
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

	/**
	 * Sets an error, keepin track of the exception that happened and the player nickname
	 * @param exception the caused exception
	 * @param nickname the nickname of the player that caused it
	 */
	public void setError(Exception exception, String nickname) {
		System.out.println("Error is being set by " + nickname + " with the following message:");
		System.out.println(exception.getMessage());
		Pair<String, Exception> error = new Pair<>(nickname, exception);

		gameError.setError(error);
	}

	/**
	 * Starts the game
	 * @throws GameNotFullException if the game has the wrong number of players
	 */
	public void startGame() throws GameNotFullException {
		if (players.size() < numberOfPlayers) {
			notify(this);
			throw new GameNotFullException("Game not full");
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

	/**
	 * Resets and Initializes Lorenzo's tokens
	 */
	public void resetTokens() {

		this.tokens = new Stack<>();

		this.tokens.push(new DiscardDevCardBlue());
		this.tokens.push(new DiscardDevCardGreen());
		this.tokens.push(new DiscardDevCardPurple());
		this.tokens.push(new DiscardDevCardYellow());
		this.tokens.push(new DoubleBlackCrossMoveToken());
		this.tokens.push(new SingleBlackCrossMoveToken());
		Collections.shuffle(this.tokens);
	}

	/**
	 * Activates a Lorenzo token
	 */
	public void drawToken() {
		lastToken = this.tokens.pop();
		gameEnded = lastToken.useToken(this);
		notify(this);
	}

	public Token getLastToken() {
		return lastToken;
	}

	/**
	 * Checks to see who is ahead in the single player game, Lorenzo or the player.
	 */
	public void updateMaxReached() {
		int maxReachedPlayer = Collections.max(players.values().stream()
				.map(p -> p.getDashboard().getFaithTrack().getPosition())
				.collect(Collectors.toList()));
		int maxReachedLorenzo =  Collections.max(players.values().stream()
				.map(p -> p.getDashboard().getFaithTrack().getLorenzoIlMagnificoPosition())
				.collect(Collectors.toList()));
		maxReached = Math.max(maxReachedPlayer, maxReachedLorenzo);
	}

	public int getMaxReached() {
		return maxReached;
	}
}
