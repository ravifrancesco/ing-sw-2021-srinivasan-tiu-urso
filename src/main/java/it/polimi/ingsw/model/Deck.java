package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.ProductionPower;
import it.polimi.ingsw.model.specialAbilities.SpecialAbilityType;

import java.util.*;

/**
 * The class represents a deck of cards.
 */

public class Deck {

	private final Stack<Card> deck;

	/**
	 * The constructor for a Deck object.
	 */

	public Deck() {
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

	public void reset(List<Card> cards) {
		deck.clear();
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

	/**
	 * Creates a copy of the deck.
	 * @return a copy of the deck.
	 */

	public Deck copy() {
		return new Deck(this.deck);
	}

	/**
	 * Resets the production power of the development cards into the deck.
	 */

	public void resetProductionPowerDevelopmentCards() {
		deck.stream().map(card -> (DevelopmentCard) card).forEach(DevelopmentCard::resetProductionPower);
	}

	/**
	 * Resets the production power of the leader cards into the deck.
	 */

	public void resetProductionPowerLeaderCards() {
		deck.stream().map(card -> (LeaderCard) card).filter(card -> card.getSpecialAbility().getType()== SpecialAbilityType.PRODUCTION_POWER)
				.map(card -> (ProductionPower) card.getSpecialAbility())
				.forEach(ProductionPower::reset);
	}

}
