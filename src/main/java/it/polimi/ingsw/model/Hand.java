package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.LeaderCard;

import java.util.ArrayList;

public class Hand {

	private ArrayList<LeaderCard> cards;

	public Hand() {
		this.cards = new ArrayList<>();
	}

	public void addCard(LeaderCard c) {
		cards.add(c);
	}

	public LeaderCard removeCard(int c) {
		return cards.remove(c);
	}

	public ArrayList<LeaderCard> getCards() {
		return cards;
	}
}