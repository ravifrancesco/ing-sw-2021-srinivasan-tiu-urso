package it.polimi.ingsw.model;

public class GameBoard {

	private Deck discardDeck;
	private Deck developmentDeck;
	private Deck leaderDeck;

	private Market market;

	private DevelopmentCardGrid developmentCardGrid;

	public void init() {

	}

	public void discardCard(Card c) {
		discardDeck.discard(c);
	}

}
