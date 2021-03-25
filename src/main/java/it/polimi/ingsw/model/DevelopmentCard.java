package it.polimi.ingsw.model;

import java.util.Collection;

public class DevelopmentCard implements Card {

	private int victoryPoints;

	private Collection<Resource> resourceCost;

	private ProductionPower productionPower;


	@Override
	public void play(Dashboard d) {

	}

	/**
	 * @see it.polimi.ingsw.model.Card#activate(it.polimi.ingsw.model.Player)
	 */
	@Override
	public void activate(Player p) {

	}

}
