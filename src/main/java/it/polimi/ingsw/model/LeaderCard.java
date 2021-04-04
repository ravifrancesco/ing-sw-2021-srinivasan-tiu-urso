package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The class represents a Leader Card of the game.
 * The object memorizes the state of a Leader Card.
 */

public class LeaderCard implements Card {

	private final int id;
	private final int victoryPoints;
	private final Map<Resource, Integer> resourceCost;
	private final Map<Banner, Integer> bannerCost;
	private final SpecialAbility specialAbility;

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
	 * Getter for resourceCost
	 * @return the resource cost of the card
	 */

	public Map<Resource, Integer> getResourceCost() {
		return resourceCost;
	}

	/**
	 * Getter for bannerCost
	 * @return the banner cost of the card
	 */

	public Map<Banner, Integer> getBannerCost() {
		return bannerCost;
	}

	/**
	 * Getter for specialAbility
	 * @return the special ability of the card
	 */

	public SpecialAbility getSpecialAbility() {
		return specialAbility;
	}

	/**
	 * To string method of the class
	 * @return a string representation of the object
	 */
	@Override
	public String toString(){
		String result="";
		result+="ID="+id+";VP="+victoryPoints+";";

		result += resourceCost.keySet().stream()
				.map(key -> key + ":" + resourceCost.get(key))
				.collect(Collectors.joining(",", "RC=", ";"));

		result += bannerCost.keySet().stream()
				.map(key -> key.getColor() + ":" + key.getLevel() + ":" + bannerCost.get(key))
				.collect(Collectors.joining(",", "BC=", ";"));

		result += "SA=" + specialAbility.toString();
		return result;
	}

	/**
	 * Allows to know if the card is playable
	 * @param playerResources all the resources of the player
	 * @param playerBanners all the banner of the player
	 * @return if the card is playable or not with the given resources and banners
	 */

	public boolean isPlayable(Map<Resource, Integer> playerResources, Map<Banner, Integer> playerBanners){
		long contResources;
		Map<Banner, Integer> playerBannersCopy = playerBanners.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		Map<Banner, Integer> bannerCostCopy = bannerCost.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		contResources=resourceCost.entrySet().stream()
				.filter(entry -> playerResources.get(entry.getKey())!=null && playerResources.get(entry.getKey())>=entry.getValue())
				.count();

		if(contResources<resourceCost.size()){ return false; }

		bannerCostCopy.entrySet().forEach(e-> playerBannersCopy.entrySet().stream()
				.filter(e2 -> e.getKey().equalsColor(e2.getKey()) && e.getKey().equalsLevel(e2.getKey()))
				.forEach(e2 -> {int diff = e.getValue()-e2.getValue(); e.setValue(Math.max(diff, 0));
					e2.setValue(diff<0 ? -diff : 0); } ));

		bannerCostCopy.entrySet().forEach(e-> playerBannersCopy.entrySet().stream()
				.filter(e2 -> e.getKey().equalsColor(e2.getKey()) && e2.getKey().getLevel()>e.getKey().getLevel())
				.forEach(e2 -> {int diff = e.getValue()-e2.getValue(); e.setValue(Math.max(diff, 0));
					e2.setValue(diff<0 ? -diff : 0); } ));

		return bannerCostCopy.entrySet().stream().allMatch(e->e.getValue()==0);
	}
}
