package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.exceptions.PowerNotActivatableException;
import it.polimi.ingsw.model.Banner;
import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.specialAbilities.SpecialAbility;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * The class represents a Leader Card of the game.
 * The object memorizes the state of a Leader Card.
 */

public class LeaderCard implements Card {

	private final int id;
	private final int victoryPoints;
	private final Map<Banner, Integer> bannerCost;
	private final SpecialAbility specialAbility;

	/**
	 * The constructor for a LeaderCard object.
	 * It initializes the id, the victory points, the resource cost, the banner cost,
	 * and the special ability.
	 * @param id represents the unique id of the card.
	 * @param victoryPoints represents the victory points given by the card.
	 * @param bannerCost represents the banner cost to buy the card.
	 * @param specialAbility represents the special ability of the card.
	 */
	public LeaderCard(int id, int victoryPoints, Map<Resource, Integer> resourceCost, Map<Banner, Integer> bannerCost, SpecialAbility specialAbility) {
		this.id = id;
		this.victoryPoints = victoryPoints;
		this.bannerCost = bannerCost.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		this.specialAbility = specialAbility;
	}


	/**
	 * Allows to activate the special ability of the leader card.
	 * @param p represents the player.
	 */

	@Override
	public void activate(Player p) {
		specialAbility.activate(p);
	}

	/**
	 * Getter for the id.
	 * @return the id of the card.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Getter for the victory points.
	 * @return the victory points given by the card.
	 */
	public int getVictoryPoints() {
		return victoryPoints;
	}

	/**
	 * Getter for bannerCost.
	 * @return the banner cost of the card.
	 */

	public Map<Banner, Integer> getBannerCost() {
		return bannerCost.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	/**
	 * Getter for specialAbility.
	 * @return the special ability of the card.
	 */
	public SpecialAbility getSpecialAbility() {
		return specialAbility;
	}

	/**
	 * To string method of the class.
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		String result="";
		result+="ID="+id+";VP="+victoryPoints+";";

		result += bannerCost.keySet().stream()
				.map(key -> key.getColor().name() + ":" + key.getLevel() + ":" + bannerCost.get(key))
				.collect(Collectors.joining(",", "BC=", ";"));

		result += specialAbility.toString();
		return result;
	}

	/**
	 * Allows to know if the card is playable.
	 * @param playerBanners all the banner of the player.
	 * @return if the card is playable or not with the given banners.
	 */
	public boolean isPlayable(Map<Banner, Integer> playerBanners) {
		Map<Banner, Integer> playerBannersCopy = playerBanners.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		Map<Banner, Integer> bannerCostCopy = bannerCost.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		bannerCostCopy.entrySet().forEach(e -> playerBannersCopy.entrySet().stream()
				.filter(e2 -> e.getKey().equalsColor(e2.getKey()) && e.getKey().equalsLevel(e2.getKey()))
				.forEach(e2 -> {int diff = e.getValue()-e2.getValue(); e.setValue(Math.max(diff, 0));
					e2.setValue(diff<0 ? -diff : 0); } ));

		return bannerCostCopy.entrySet().stream().allMatch(e -> e.getValue()==0);
	}

	/**
	 * Equals method for the class.
	 * @param o the other leader card to compare.
	 * @return true if the two cards have the same id.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		LeaderCard that = (LeaderCard) o;

		return id == that.id;
	}


}
