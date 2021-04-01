package it.polimi.ingsw.model;

import java.util.*;

public class Deck {

	private Stack<Card> deck;

	Deck(){
		deck = new Stack<>();
	}

	public void init(List<Card> cards){
		if(cards!=null) {
			cards.forEach(c -> deck.push(c));
		}
	}

	public void shuffle() {
		Collections.shuffle(deck);
	}

	public void discard(Card c){
		deck.push(c);
	}

	public Card getCard(){
		return deck.pop();
	}

}