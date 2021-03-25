package it.polimi.ingsw.model;

import java.util.Map;

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

	private boolean played;

	private boolean discarded;

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
		this.played=false;
		this.discarded=false;
	}

	/**
	 * Resets the state of this object to the initial state.
	 */
	public void reset(){
		this.played=false;
		this.discarded=false;
	}

	/**
	 * Allows to put the card on the dashboard, changing its state
	 * @param dashboard the dashboard of the player who played the card
	 */
	@Override
	public void play(Dashboard dashboard, int position) {
		if(dashboard!=null) {
			dashboard.insertLeaderCard(this, position);
		}
		this.played=true;
	}

	@Override
	public void activate(Dashboard d) {
		//Implementing later..
	}

	/**
	 * Set the state of the card as discarded and add a faith point
	 */
	public void discard(Dashboard d){
		this.discarded=true;
		if(d!=null) {
			d.moveFaithMarker(1);
		}
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
	 * Getter for played
	 * @return if the card has been played or not
	 */
	public boolean isPlayed() {
		return played;
	}

	/**
	 * Getter for discarded
	 * @return if the card has been discarded or not
	 */
	public boolean isDiscarded() {
		return discarded;
	}

	/**
	 * To string method of the class
	 * @return a string representation of the object
	 */
	@Override
	public String toString(){
		String result="";
		result+="ID="+id+";VP="+victoryPoints+";RC=";

		if(resourceCost!=null) {
			for (Map.Entry<Resource, Integer> pair : resourceCost.entrySet()) {
				result += pair.getKey() + "," + pair.getValue() + ",";
			}
			result=result.substring(0, result.length()-1)+";BC=";
		}
		else{
			result+=";BC=";
		}

		if(bannerCost!=null) {
			for (Map.Entry<Banner, Integer> pair : bannerCost.entrySet()) {
				result += pair.getKey().getColor() + "," + pair.getKey().getLevel() + "," + pair.getValue() + ",";
			}
			result=result.substring(0, result.length()-1)+";";
		}
		else
		{
			result+=";";
		}


		if(specialAbility!=null) {
			result += "SA=" + specialAbility.toString() + ";";
		}
		else
		{
			result +="SA=;";
		}
		return result;
	}
}
