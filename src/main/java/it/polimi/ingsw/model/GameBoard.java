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

	private Game game;

	/**
	 * The constructor for a GameBoard object.
	 * It initializes the three decks, the market and the development card grid.
	 */

	public GameBoard() {
		discardDeck = new Deck();
		developmentDeck = new Deck();
		leaderDeck = new Deck();
		market = new Market();
		developmentCardGrid = new DevelopmentCardGrid();
	}

	/**
	 * The init method for the class. It loads the development cards and the leader cards from the game settings and
	 * it adds them to the decks. It also reset the market and it fills the development card grid.
	 * @param gameSettings the settings for the current game.
	 */

	public void init(GameSettings gameSettings) {
		developmentDeck.init(Arrays.asList(gameSettings.getDevelopmentCards()));
		developmentDeck.shuffle();
		leaderDeck.init(Arrays.asList(gameSettings.getLeaderCards()));
		leaderDeck.shuffle();
		market.reset();
		developmentCardGrid.init(developmentDeck);
	}

	/**
	 * Allows to add a card to the discard deck
	 * @param c the card to be discarded
	 */

	public void discardCard(Card c) {
		discardDeck.add(c);
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
}
