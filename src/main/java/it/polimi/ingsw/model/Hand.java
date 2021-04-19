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

	public void discardCardInExcess(int c) {
		// TODO
	}

	public void discardCard(int c) {
		// TODO
	}

	public void playLeaderCard(int c) {
		// TODO
	}

	public ArrayList<LeaderCard> getCards() {
		return cards;
	}
}