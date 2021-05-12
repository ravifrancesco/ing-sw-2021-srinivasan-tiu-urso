package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.LeaderCard;

import java.util.ArrayList;

/**
 * This class represents the player's hand.
 *
 * The class is observable and notifies the observers on a change of the state.
 */
public class Hand extends HandObservable {

	private ArrayList<LeaderCard> cards;

	/**
	 * Constructor for the class.
	 */
	public Hand() {
		this.cards = new ArrayList<>();
	}

	/**
	 * Resets the hand to the initial state.
	 */
	public void reset() {
		this.cards.clear();
		notify(this);
	}

	/**
	 * Adds card to the hand.
	 *
	 * @param c	leaderCard to add.
	 */
	public void addCard(LeaderCard c) {
		cards.add(c);
		notify(this);
	}

	/**
	 * Removes a card from the player's hand.
	 *
	 * @param c	index of the card to remove.
	 * @return	the card that was removed.
	 */
	public LeaderCard removeCard(int c) {
		LeaderCard removed = cards.remove(c);
		notify(this);
		return removed;
	}

	/**
	 * Gets a card from the player's hand.
	 *
	 * @param c	index of the card to get.
	 * @return	the card that was indexed.
	 */
	public LeaderCard getCard(int c) {
		return cards.get(c);
	}

	/**
	 * Gets the size of the player's hand.
	 *
	 * @return	the hand's size.
	 */
	public int getHandSize() {
		return cards.size();
	}
}