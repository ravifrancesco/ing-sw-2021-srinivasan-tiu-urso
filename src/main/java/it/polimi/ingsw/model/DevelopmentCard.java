package it.polimi.ingsw.model;

import java.util.Map;

/**
 * The class represents a Development Card of the game.
 * The object memorizes the state of a Development Card. The state includes:
 * The victory points.
 * The resource cost to buy the card.
 * The production power.
 * If the card has been played and if the card is activatable
 */

public class DevelopmentCard implements Card {

	private int victoryPoints;

	private Map<Resource, Integer> resourceCost;

	private ProductionPower productionPower;

	private boolean played;

	private boolean activatable;

	/**
	 * The constructor for a Development Card object.
	 * It initializes played and activatable.
	 * @param victoryPoints represents the victory points given by the card
	 * @param resourceCost represents the resource cost to buy the card
	 * @param productionPower represents the production power of the card
	 */

	public DevelopmentCard(int victoryPoints, Map<Resource, Integer> resourceCost, ProductionPower productionPower) {
		this.victoryPoints = victoryPoints;
		this.resourceCost = resourceCost;
		this.productionPower = productionPower;
		this.played=false;
		this.activatable=false;
	}

	/**
	 * Resets the state of this object to the initial state.
	 */

	public void reset(){
		this.played=false;
		this.activatable=false;
	}

	/**
	 * Allows to place the card in the player dashboard
	 * @param d represents the dashboard of the player
	 * @param position represents the position where to place the card
	 */

	@Override
	public void play(Dashboard d, int position) {
		if(d!=null) {
			d.insertDevelopmentCard(this, position);
		}
		this.played=true;
		this.activatable=true;
	}

	/**
	 * Allows to activate the production power
	 * @param d represents the dashboard of the player
	 */

	@Override
	public void activate(Dashboard d) {
		if(played && activatable && productionPower!=null && d!=null){
			productionPower.activate(d.getPlayer());
		}
	}

	/**
	 * Allows to place another card onto this card
	 */

	public void substitute(){
		if(played) {
			this.activatable = false;
		}
	}

	/**
	 * Getter for played
	 * @return if the card has been played or not
	 */

	public boolean isPlayed(){
		return played;
	}

	/**
	 * Getter for activatable
	 * @return if the card is still activatable or not
	 */

	public boolean isActivatable(){
		return activatable;
	}

}
