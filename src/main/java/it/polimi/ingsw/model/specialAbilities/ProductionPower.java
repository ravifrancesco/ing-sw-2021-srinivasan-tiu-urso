package it.polimi.ingsw.model.specialAbilities;

import it.polimi.ingsw.model.Banner;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The class represents a Production Power of the game.
 * The object memorizes the state of a Production Power of a Development Card or of a Leader Card.
 */

public class ProductionPower implements SpecialAbility {

	private Map<Resource, Integer> resourceRequired;
	private Map<Resource, Integer> resourceProduced;
	private int numberFaithPoints;
	private boolean selectableResource;
	private boolean activated;

	/**
	 * The constructor for a Production Power object.
	 * @param resourceRequired represents the cost to activate the production power.
	 * @param resourceProduced represents the resources produced.
	 * @param numberFaithPoints represents the faith points given by the production power.
	 * @param selectableResource represents if the production power gives a selectable resource or not.
	 */

	public ProductionPower(Map<Resource, Integer> resourceRequired, Map<Resource, Integer> resourceProduced, int numberFaithPoints, boolean selectableResource) {
		this.resourceRequired = resourceRequired.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		this.resourceProduced = resourceProduced != null ? resourceProduced.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)) : null;
		this.numberFaithPoints = numberFaithPoints;
		this.selectableResource = selectableResource;
		this.activated = false;
	}

	/**
	 * It allows to move the faith marker and to store the resources given by the production power.
	 * @param p the player who activated the production power.
	 */

	@Override
	public void activate(Player p) {
		if (numberFaithPoints != 0) {
			p.getDashboard().moveFaithMarker(numberFaithPoints);
		}
		if (resourceProduced != null && !resourceProduced.isEmpty()) {
			resourceProduced.entrySet()
					.stream()
					.filter(entry -> entry.getKey() != Resource.ANY)
					.forEach(entry -> p.getDashboard().storeResourceInLocker(entry.getKey(), entry.getValue()));
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

		resourceRequiredModified.entrySet().forEach(e-> playerResources.entrySet().stream()
				.filter(e2 -> e.getKey() == e2.getKey())
				.forEach(e2 -> {int diff = e.getValue()-e2.getValue(); e.setValue(Math.max(diff, 0));
					e2.setValue(-diff); } ));

		if(playerResourcesCopy.values().stream().anyMatch(v -> v<0)) { return false; }

		if(selectableResource) {
			contAny = resourceRequired.get(Resource.ANY);
			return playerResourcesCopy.values().stream().reduce(0, Integer::sum) >= contAny;
		}

		return true;
	}

	/**
	 * Allows to know if this production power has been activated in the current turn.
	 * @return true if the production power is activatable, false otherwise.
	 */

	public boolean isActivatable() {
		return !activated;
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

	/**
	 * Method to get the type of this special ability.
	 * @return the type of this special ability.
	 */

	public SpecialAbilityType getType() {
		return SpecialAbilityType.PRODUCTION_POWER;
	}

	public void setSelectableResource(Optional<Map<Resource, Integer>> resourceRequiredOptional,
									  Optional<Map<Resource, Integer>> resourceProducedOptional)
			throws IllegalArgumentException, IllegalStateException {

		int numOfResourceRequiredOptional = resourceRequiredOptional.isPresent() ? resourceRequiredOptional.get().values().stream().reduce(0, Integer::sum) : 0;
		int numOfResourceRequiredAvailable = resourceRequired.get(Resource.ANY) != null ? resourceRequired.get(Resource.ANY) : 0;

		int numOfResourceProducedOptional = resourceProducedOptional.isPresent() ? resourceProducedOptional.get().values().stream().reduce(0, Integer::sum) : 0;
		int numOfResourceProducedAvailable = resourceProduced.get(Resource.ANY) != null ? resourceProduced.get(Resource.ANY) : 0;

		if (numOfResourceProducedAvailable != numOfResourceProducedOptional || numOfResourceRequiredAvailable != numOfResourceRequiredOptional) {
			throw new IllegalArgumentException();
		}

		resourceRequiredModified = resourceRequired.entrySet().stream()
				.filter(e -> e.getKey() != Resource.ANY).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		resourceRequiredOptional.ifPresent(rro -> rro.
				forEach((k, v) -> resourceRequiredModified.merge(k, v, Integer::sum)));

		resourceProducedModified = resourceProduced.entrySet().stream()
				.filter(e -> e.getKey() != Resource.ANY).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		resourceProducedOptional.ifPresent(rpo -> rpo.
				forEach((k, v) -> resourceProducedModified.merge(k, v, Integer::sum)));

		if (resourceRequiredModified.containsKey(Resource.ANY) || resourceProduced.containsKey(Resource.ANY)) {
			throw new IllegalStateException();
		}

	}

}