package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.LeaderCard;

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

	/**
	 * The constructor for a GameBoard object.
	 * It initializes the three decks from the game settings and it creates the market and the development card grid.
	 */

	public GameBoard() {
		discardDeck = new Deck();
		developmentDeck = new Deck();
		leaderDeck = new Deck();
		market = new Market();
		developmentCardGrid = new DevelopmentCardGrid();
	}

	/**
	 * The init method for the class. It shuffles the development cards and the leader cards decks.
	 * It also resets the market and it resets and fills the development card grid.
	 */

	public void reset(GameSettings gameSettings) {
		developmentDeck.reset(Arrays.asList(gameSettings.getDevelopmentCards()));
		leaderDeck.reset(Arrays.asList(gameSettings.getLeaderCards()));
		developmentDeck.shuffle();
		leaderDeck.shuffle();
		market.reset();
		developmentCardGrid.reset();
		developmentCardGrid.fillCardGrid(developmentDeck);
	}

	/**
	 * Allows to add a card to the discard deck.
	 * @param c the card to be discarded.
	 * @param d the dashboard of the player who discarded the card.
	 */

	public void discardCard(Card c, Dashboard d) {
		LeaderCard leaderCard = (LeaderCard) c;
		discardDeck.add(leaderCard);
		leaderCard.discard(d);
	}

	/**
	 * Allows to add a card to the discard deck without moving the faith marker (first discarding).
	 * @param c the card to be discarded.
	 */

	public void discardCardInExcess(Card c) {
		LeaderCard leaderCard = (LeaderCard) c;
		discardDeck.add(leaderCard);
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
		return discardDeck.copy();
	}

	/**
	 * Getter for the development cards deck.
	 * @return the development cards deck.
	 */

	public Deck getDevelopmentDeck() {
		return developmentDeck.copy();
	}

	/**
	 * Getter for the leader cards deck.
	 * @return the leader cards deck.
	 */

	public Deck getLeaderDeck() {
		return leaderDeck.copy();
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
