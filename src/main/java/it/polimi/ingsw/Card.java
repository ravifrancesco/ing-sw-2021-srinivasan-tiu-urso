package it.polimi.ingsw;

public interface Card {

	private Deck deck;

	private Hand hand;

	public abstract void play();

	public abstract void draw();

}
