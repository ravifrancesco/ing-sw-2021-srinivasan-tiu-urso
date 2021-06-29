package it.polimi.ingsw.model.full.specialAbilities;

import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.full.table.Resource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The class represents a Production Power of the game.
 * The object memorizes the state of a Production Power of a Development Card or of a Leader Card.
 */

public class ProductionPower implements SpecialAbility, Serializable {

	private final Map<Resource, Integer> resourceRequired;
	private Map<Resource, Integer> resourceRequiredModified;
	private final Map<Resource, Integer> resourceProduced;
	private Map<Resource, Integer> resourceProducedModified;
	private int numberFaithPoints;
	private boolean activated;

	/**
	 * The constructor for a Production Power object.
	 * @param 	resourceRequired represents the cost to activate the production power.
	 * @param 	resourceProduced represents the resources produced.
	 * @param 	numberFaithPoints represents the faith points given by the production power.
	 */
	public ProductionPower(Map<Resource, Integer> resourceRequired, Map<Resource, Integer> resourceProduced, int numberFaithPoints) {
		this.resourceRequired = resourceRequired.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		this.resourceProduced = resourceProduced != null ? resourceProduced.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)) : null;
		this.numberFaithPoints = numberFaithPoints;
		this.activated = false;
	}

	/**
	 * It allows to move the faith marker and to store the resources given by the production power.
	 * @param 	p the player who activated the production power.
	 */
	@Override
	public void activate(Player p) throws UnsupportedOperationException {
		if(!this.isActivatable()) {
			throw new UnsupportedOperationException();
		} else if (!this.isActivatable(p.getDashboard().getAllPlayerResources())) {
			throw new IllegalStateException();
		}
		if (numberFaithPoints != 0) {
			p.getDashboard().moveFaithMarker(numberFaithPoints);
		}
		if (resourceProducedModified != null && !resourceProducedModified.isEmpty()) {
			resourceProducedModified.forEach((k, v) -> p.getDashboard().storeResourceInLocker(k, v));
		}
		// In order to not activate the same production power twice in one turn, after activation 'activated'
		// flag is set to true.
		activated = true;
	}

	/**
	 * Allows to activate again the production power in the next turns.
	 */
	public void reset() {
		activated = false;
	}

	/**
	 * Getter for resource required.
	 * @return 	the resource cost of the production power.
	 */
	public Map<Resource, Integer> getResourceRequired() {
		return resourceRequired.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	/**
	 * Getter for resource produced.
	 * @return 	the resources produced by the production power.
	 */
	public Map<Resource, Integer> getResourceProduced() {
		return resourceProduced != null ? resourceProduced.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)) : null;
	}

	/**
	 * Getter for resource required modified.
	 * @return 	the resource cost of the production power (without selectable resources).
	 */
	public Map<Resource, Integer> getResourceRequiredModified() {
		return resourceRequiredModified.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	/**
	 * Getter for resource produced modified.
	 * @return 	the resources produced by the production power (without selectable resources).
	 */
	public Map<Resource, Integer> getResourceProducedModified() {
		return resourceProducedModified != null ? resourceProducedModified.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)) : null;
	}

	/**
	 * Getter for number faith points.
	 * @return 	the number of faith points given by the production power.
	 */
	public int getNumberFaithPoints() {
		return numberFaithPoints;
	}

	/**
	 * Allows to know if this card is activatable.
	 * @param 	playerResources all the resources of the player.
	 * @return 	true if this card is activatable with the given resources, false otherwise.
	 */
	public boolean isActivatable(Map<Resource, Integer> playerResources) {
		Set<Map.Entry<Resource, Integer>> entries = resourceRequiredModified.entrySet();
		HashMap<Resource, Integer> copy = (HashMap<Resource, Integer>) entries.stream()
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		copy.entrySet().forEach(e-> playerResources.entrySet().stream()
				.filter(e2 -> e.getKey() == e2.getKey())
				.forEach(e2 -> {int diff = e.getValue()-e2.getValue(); e.setValue(Math.max(diff, 0));
					e2.setValue(-diff); } ));

		return playerResources.values().stream().noneMatch(v -> v < 0);
	}

	/**
	 * Allows to know if this production power has been activated in the current turn.
	 * @return 	true if the production power is activatable, false otherwise.
	 */
	public boolean isActivatable() {
		return !activated;
	}

	/**
	 * To string method of the class.
	 * @return 	a string representation of the object.
	 */
	public String toString() {
		String result="";

		result+="SA=PP;";

		result += resourceRequired.keySet().stream()
				.map(key -> key + ":" + resourceRequired.get(key))
				.collect(Collectors.joining(",", "RR=", ";"));

		result += resourceProduced.keySet().stream()
				.map(key -> key + ":" + resourceProduced.get(key))
				.collect(Collectors.joining(",", "RP=", ";"));

		result += "FP=" + numberFaithPoints + ";";

		return result;
	}

	/**
	 * Method to get the type of this special ability.
	 * @return 	the type of this special ability.
	 */
	@Override
	public SpecialAbilityType getType() {
		return SpecialAbilityType.PRODUCTION_POWER;
	}

	/**
	 * Allows to change resource required and produced in order to remove the selectable resources.
	 * @param 	resourceRequiredOptional the resource required which replace the selectable resources.
	 * @param 	resourceProducedOptional the resource produced which replace the selectable resources.
	 * @throws 	IllegalArgumentException if the number of resources chosen doesn't match.
	 * @throws 	IllegalStateException if the resource required or produced still contain selectable resources.
	 */
	public void setSelectableResource(Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional)
			throws IllegalArgumentException, IllegalStateException {

		int numOfResourceRequiredOptional = resourceRequiredOptional.values().stream().reduce(0, Integer::sum);
		int numOfResourceRequiredAvailable = resourceRequired.get(Resource.ANY) != null ? resourceRequired.get(Resource.ANY) : 0;

		int numOfResourceProducedOptional = resourceProducedOptional.values().stream().reduce(0, Integer::sum);
		int numOfResourceProducedAvailable = resourceProduced.get(Resource.ANY) != null ? resourceProduced.get(Resource.ANY) : 0;

		if (numOfResourceProducedAvailable != numOfResourceProducedOptional || numOfResourceRequiredAvailable != numOfResourceRequiredOptional) {
			throw new IllegalArgumentException();
		}

		resourceRequiredModified = resourceRequired.entrySet().stream()
				.filter(e -> e.getKey() != Resource.ANY).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		resourceRequiredOptional.forEach((k, v) -> resourceRequiredModified.merge(k, v, Integer::sum));

		resourceProducedModified = resourceProduced.entrySet().stream()
				.filter(e -> e.getKey() != Resource.ANY).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		resourceProducedOptional.forEach((k, v) -> resourceProducedModified.merge(k, v, Integer::sum));

		if (resourceRequiredModified.containsKey(Resource.ANY) || resourceProducedModified.containsKey(Resource.ANY)) {
			throw new IllegalStateException();
		}

	}

	/**
	 * Getter for the number of required selectable resources.
	 * @return	the number of required selectable resources.
	 */
	public int getNumRequiredAny() {
		return resourceRequired.get(Resource.ANY) == null ? 0 : resourceRequired.get(Resource.ANY);
	}

	/**
	 * Getter for the number of produced selectable resources.
	 * @return	the number of produced selectable resources.
	 */
	public int getNumProducedAny() {
		return resourceProduced.get(Resource.ANY) == null ? 0 : resourceProduced.get(Resource.ANY);
	}

}