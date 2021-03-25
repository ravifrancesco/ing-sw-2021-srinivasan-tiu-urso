package it.polimi.ingsw.model;

public interface Card {

	void play(Hand h);

	Card draw(Deck d);

	void activate(Player p);

}
