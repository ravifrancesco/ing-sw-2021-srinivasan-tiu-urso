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
	 * @param id
	 * @param victoryPoints
	 * @param resourceCost
	 * @param bannerCost
	 * @param specialAbility
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
	public void play(Dashboard dashboard) {
		if(dashboard!=null) {
			dashboard.insertLeaderCard(this);
		}
		this.played=true;
	}

	/**
	 * @see it.polimi.ingsw.model.Card#activate(it.polimi.ingsw.model.Player)
	 */
	@Override
	public void activate(Player p) {
		//Implementing later..
	}

	/**
	 * Set the state of the card as discarded
	 */
	public void discard(){
		this.discarded=true;
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

	/*public boolean isPlayable(Map<Resource, Integer> userResources, Map<Banner, Integer> userBanners){
		for (Map.Entry<Resource, Integer> pair : resourceCost.entrySet()) {
			if(!userResources.containsKey(pair.getKey()) || (userResources.containsKey(pair.getKey()) && userResources.get(pair.getKey())<pair.getValue())){
				return false;
			}
		}
		for (Map.Entry<Banner, Integer> pair : bannerCost.entrySet()) {
			if(!userBanners.containsKey(pair.getKey()) || (userBanners.containsKey(pair.getKey()) && userBanners.get(pair.getKey())<pair.getValue())){
				return false;
			}
		}
		return true;
	}*/

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
