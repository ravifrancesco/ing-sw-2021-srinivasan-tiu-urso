package it.polimi.ingsw.model;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * The class represents a Leader Card of the game.
 * The object memorizes the state of a Leader Card.
 */

public class LeaderCard implements Card {

	private int id;
	private int victoryPoints;
	private Map<Resource, Integer> resourceCost;
	private Map<Banner, Integer> bannerCost;
	private SpecialAbility specialAbility;

	/**
	 * The constructor for a LeaderCard object.
	 * It initializes the id, the victory points, the resource cost, the banner cost,
	 * and the special ability.
	 * @param id represents the unique id of the card
	 * @param victoryPoints represents the victory points given by the card
	 * @param resourceCost represents the resource cost to buy the card
	 * @param bannerCost represents the banner cost to buy the card
	 * @param specialAbility represents the special ability of the card
	 */

	public LeaderCard(int id, int victoryPoints, Map<Resource, Integer> resourceCost, Map<Banner, Integer> bannerCost, SpecialAbility specialAbility) {
		this.id = id;
		this.victoryPoints = victoryPoints;
		this.resourceCost = resourceCost;
		this.bannerCost = bannerCost;
		this.specialAbility = specialAbility;
	}

	/**
	 * Allows to put the card on the dashboard, changing its state
	 * @param dashboard the dashboard of the player who played the card
	 */
	@Override
	public void play(Dashboard dashboard, int position) {
		dashboard.insertLeaderCard(this, position);
	}

	@Override
	public void activate(Player p) {
		//Implementing later..
	}

	/**
	 * Set the state of the card as discarded and add a faith point
	 */
	public void discard(Dashboard d){
		d.moveFaithMarker(1);
	}

	/**
	 * Getter for the id
	 * @return the id of the card
	 */
	public int getId() {
		return id;
	}

	/**
	 * Getter for the victory points
	 * @return the victory points given by the card
	 */
	public int getVictoryPoints() {
		return victoryPoints;
	}

	/**
	 * To string method of the class
	 * @return a string representation of the object
	 */
	@Override
	public String toString(){
		String result="";
		result+="ID="+id+";VP="+victoryPoints+"";

		result += resourceCost.keySet().stream()
				.map(key -> key + "," + resourceCost.get(key))
				.collect(Collectors.joining(",", ";RC=", ";"));

		result += bannerCost.keySet().stream()
				.map(key -> key.getColor() + "," + key.getLevel() + "," + bannerCost.get(key))
				.collect(Collectors.joining(",", "BC=", ";"));

		result += "SA=" + specialAbility.toString();
		return result;
	}
}
