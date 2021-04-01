package it.polimi.ingsw.model;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * The class represents a Development Card of the game.
 * The object memorizes the state of a Development Card. The state includes:
 * The victory points.
 * The resource cost to buy the card.
 * The production power.
 */

public class DevelopmentCard implements Card {

	private int id;
	private int victoryPoints;
	private Map<Resource, Integer> resourceCost;
	private ProductionPower productionPower;
	private Banner banner;

	/**
	 * The constructor for a Development Card object.
	 * @param id represents the unique id of the card
	 * @param victoryPoints represents the victory points given by the card
	 * @param resourceCost represents the resource cost to buy the card
	 * @param productionPower represents the production power of the card
	 * @param banner represents the banner of the card
	 */

	public DevelopmentCard(int id, int victoryPoints, Map<Resource, Integer> resourceCost, ProductionPower productionPower, Banner banner) {
		this.id=id;
		this.victoryPoints = victoryPoints;
		this.resourceCost = resourceCost;
		this.productionPower = productionPower;
		this.banner=banner;
	}

	/**
	 * Allows to place the card in the player dashboard
	 * @param d represents the dashboard of the player
	 * @param position represents the position where to place the card
	 */

	@Override
	public void play(Dashboard d, int position) {
		d.insertDevelopmentCard(this, position);
	}

	/**
	 * Allows to activate the production power
	 * @param p represents the player
	 */

	@Override
	public void activate(Player p) {
		productionPower.activate(p);
	}

	/**
	 * Getter for the id
	 * @return the id of the card
	 */

	public int getId() {
		return id;
	}

	/**
	 * Getter for victory points
	 * @return the victory points given by the card
	 */

	public int getVictoryPoints() {
		return victoryPoints;
	}

	/**
	 * Getter for banner
	 * @return the banner of the card
	 */

	public Banner getBanner() {
		return banner;
	}

	/**
	 * To string method of the class
	 * @return a string representation of the object
	 */

	@Override
	public String toString(){
		String result="";

		result+="ID="+id+";VP="+victoryPoints+";" + banner.toString();

		result += resourceCost.keySet().stream()
				.map(key -> key + "," + resourceCost.get(key))
				.collect(Collectors.joining(",", "RC=", ";"));

		result += "SA=" + productionPower.toString();

		return result;
	}
}