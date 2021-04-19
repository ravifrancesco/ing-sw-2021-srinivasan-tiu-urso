package it.polimi.ingsw.model;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * The class represents a Production Power of the game.
 * The object memorizes the state of a Production Power of a Development Card or of a Leader Card.
 */

public class ProductionPower implements SpecialAbility {

	//TODO allow to have any resourceRequired (for the base production power of the dashboard)

	private Map<Resource, Integer> resourceRequired;
	private Map<Resource, Integer> resourceProduced;
	private int numberFaithPoints;
	private boolean selectableResource;

	/**
	 * The constructor for a Production Power object.
	 * @param resourceRequired represents the cost to activate the production power.
	 * @param resourceProduced represents the resources producted.
	 * @param numberFaithPoints represents the faith points given by the production power.
	 * @param selectableResource represents if the production power gives a selectable resource or not.
	 */

	public ProductionPower(Map<Resource, Integer> resourceRequired, Map<Resource, Integer> resourceProduced, int numberFaithPoints, boolean selectableResource) {
		this.resourceRequired = resourceRequired.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		this.resourceProduced = resourceProduced != null ? resourceProduced.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)) : null;
		this.numberFaithPoints = numberFaithPoints;
		this.selectableResource = selectableResource;
	}

	/**
	 * It allows to move the faith marker and to store the resources given by the production power.
	 * @param p the player who activated the production power.
	 */

	@Override
	public void activate(Player p) {
		if(p!=null) {
			if (numberFaithPoints != 0) {
				p.getDashboard().moveFaithMarker(numberFaithPoints);
			}
			if (resourceProduced != null && !resourceProduced.isEmpty()) {
				resourceProduced.forEach((k, v) -> p.getDashboard().storeResourceInLocker(k, v)); //TODO store separatly
			}
		}
	}

	/**
	 * It allows to store one selectable resource.
	 * @param p the player who activated the production power.
	 * @param r the resource chosen by the player.
	 */

	public void giveOneSelectableResource(Player p, Resource r) {
		if(selectableResource){
			p.getDashboard().storeResourceInLocker(r, 1);
		}
	}

	/**
	 * Getter for resource required.
	 * @return the resource cost of the production power.
	 */

	public Map<Resource, Integer> getResourceRequired() {
		return resourceRequired.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	/**
	 * Getter for resource produced.
	 * @return the resources produced by the production power.
	 */

	public Map<Resource, Integer> getResourceProduced() {
		return resourceProduced != null ? resourceProduced.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)) : null;
	}

	/**
	 * Getter for number faith points.
	 * @return the number of faith points given by the production power.
	 */

	public int getNumberFaithPoints() {
		return numberFaithPoints;
	}

	/**
	 * Getter for selectable resources.
	 * @return if the production power gives a selectable resource or not.
	 */

	public boolean isSelectableResource() {
		return selectableResource;
	}


	/**
	 * Allows to know if this card is activatable.
	 * @param playerResources all the resources of the player.
	 * @return if this card is activatable or not with the given resources.
	 */

	public boolean isActivatable(Map<Resource, Integer> playerResources) {
		long contResources;

		contResources=resourceRequired.entrySet().stream()
				.filter(entry -> playerResources.get(entry.getKey())!=null && playerResources.get(entry.getKey())>=entry.getValue())
				.count();

		return contResources>=resourceRequired.size();
	}

	/**
	 * To string method of the class.
	 * @return a string representation of the object.
	 */
	public String toString() {
		String result="";

		result+="SA=PP;";

		result += resourceRequired.keySet().stream()
				.map(key -> key + ":" + resourceRequired.get(key))
				.collect(Collectors.joining(",", "RR=", ";"));

		if(resourceProduced!=null) { //it could be null in case it is the production power of a leader card
			result += resourceProduced.keySet().stream()
					.map(key -> key + ":" + resourceProduced.get(key))
					.collect(Collectors.joining(",", "RP=", ";"));
		}

		result += "FP=" + numberFaithPoints + ";";

		result += "SR=" + (selectableResource ? "y" : "n") + ";";

		return result;
	}

	//TODO doc
	public SpecialAbilityType getType() {
		return SpecialAbilityType.PRODUCTION_POWER;
	}
}