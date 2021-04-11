package it.polimi.ingsw.model;

import java.util.*;

/**
 * The class represents a deck of cards.
 */

public class Deck {

	private final Stack<Card> deck;

	/**
	 * The constructor for a Deck object.
	 */

	Deck() {
		deck = new Stack<>();
	}

	private Deck(Stack<Card> oldDeck) {
		deck = new Stack<>();
		deck.addAll(oldDeck);
	}

	/**
	 * Initializes the deck of cards from a list of cards.
	 * @param cards the cards to add to the deck.
	 */

	public void init(List<Card> cards) {
			cards.forEach(deck::push);
	}

	/**
	 * Allows to shuffle the deck.
	 */

	public void shuffle() {
		Collections.shuffle(deck);
	}

	/**
	 * Allows to add a card to the deck.
	 * @param c the card to be added.
	 */

	public void add(Card c){
		deck.push(c);
	}

	/**
	 * Allows to get the top card of the deck.
	 * @return the top card of the deck.
	 */

	public Card getCard(){
		return deck.pop();
	}

	/**
	 * Getter for the size of the deck.
	 * @return the size of the deck.
	 */

	public int getSize() { return deck.size(); }

	public Deck copy() {
		return new Deck(this.deck);
	}

}
