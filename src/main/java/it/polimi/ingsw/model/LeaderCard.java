package it.polimi.ingsw.model;

import java.util.Collection;

public class LeaderCard implements Card {

	private int victoryPoints;

	private Collection<Resource> resourceCost;

	private Collection<Banner> bannerCost;

	private SpecialAbility specialAbility;


	/**
	 * @see Card#play(Hand)
     * @param h
	 */
	@Override
	public void play(Hand h) {

	}


	/**
	 * @see Card#draw(Deck)
	 * @param d
	 * @return
	 */
	@Override
	public Card draw(Deck d) {
		return null;
	}


	/**
	 * @see it.polimi.ingsw.model.Card#activate(it.polimi.ingsw.model.Player)
	 */
	@Override
	public void activate(Player p) {

	}

}
