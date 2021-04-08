package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The class represents the only GameBoard of a game.
 * The object memorizes the state of the GameBoard. The state includes:
 * The discard deck
 * The development card deck
 * The leader card deck
 * The market
 * The development card grid
 * The game it belongs to
 */

public class GameBoard {

	private Deck discardDeck;
	private Deck developmentDeck;
	private Deck leaderDeck;

	private Market market;

	private DevelopmentCardGrid developmentCardGrid;

	private Game game; // TODO

	/**
	 * The constructor for a GameBoard object.
	 * It initializes the three decks from the game settings and it creates the market and the development card grid.
	 * @param gameSettings the settings for the current game.
	 */

	public GameBoard(GameSettings gameSettings) {
		discardDeck = new Deck();
		developmentDeck = new Deck();
		developmentDeck.init(Arrays.asList(gameSettings.getDevelopmentCards()));
		leaderDeck = new Deck();
		leaderDeck.init(Arrays.asList(gameSettings.getLeaderCards()));
		market = new Market();
		developmentCardGrid = new DevelopmentCardGrid();
	}

	/**
	 * The init method for the class. It shuffles the development cards and the leader cards decks.
	 * It also reset the market and it fills the development card grid.
	 */

	public void init() {
		developmentDeck.shuffle();
		leaderDeck.shuffle();
		market.reset();
		developmentCardGrid.init(developmentDeck);
	}

	/**
	 * Allows to add a card to the discard deck
	 * @param c the card to be discarded
	 */

	public void discardCard(Card c, Dashboard d) {
		LeaderCard leaderCard = (LeaderCard) c;
		leaderCard.discard(d, discardDeck);
	}

	/**
	 * To be called when the game starts. It allows to get a card from the leader deck.
	 * @return the top card of the leader card deck.
	 */

	public LeaderCard getLeaderCard() {
		return (LeaderCard) leaderDeck.getCard();
	}

	/**
	 * @see Market#getResources(int move, Player p)
	 */

	public ArrayList<Resource> getResourcesFromMarket(int move, Player p) {
		return market.getResources(move, p);
	}

	/**
	 * Getter for the discard deck.
	 * @return the discard deck.
	 */

	public Deck getDiscardDeck() {
		return discardDeck;
	}

	/**
	 * Getter for the development cards deck.
	 * @return the development cards deck.
	 */

	public Deck getDevelopmentDeck() {
		return developmentDeck;
	}

	/**
	 * Getter for the leader cards deck.
	 * @return the leader cards deck.
	 */

	public Deck getLeaderDeck() {
		return leaderDeck;
	}

	/**
	 * Getter for the market.
	 * @return the market.
	 */

	public Market getMarket() {
		return market;
	}

	/**
	 * Getter for the development cards grid.
	 * @return the development cards grid.
	 */

	public DevelopmentCardGrid getDevelopmentCardGrid() {
		return developmentCardGrid;
	}
}
