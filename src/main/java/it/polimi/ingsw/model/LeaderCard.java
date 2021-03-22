package it.polimi.ingsw.model;

import java.util.Collection;

public class LeaderCard implements Card {

	private int victoryPoints;

	private Collection<Resource> resourceCost;

	private Collection<Banner> bannerCost;

	private SpecialAbility specialAbility;


	/**
	 * @see it.polimi.ingsw.model.Card#play()
	 */
	@Override
	public void play() {

	}


	/**
	 * @see it.polimi.ingsw.model.Card#draw()
	 */
	@Override
	public void draw() {

	}


	/**
	 * @see it.polimi.ingsw.model.Card#activate(it.polimi.ingsw.model.Player)
	 */
	@Override
	public void activate(Player p) {

	}

}
