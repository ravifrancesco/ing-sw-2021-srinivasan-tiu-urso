package it.polimi.ingsw.model.specialAbilities;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;

public class WarehouseExtraSpace implements SpecialAbility {

	private final Resource storedResource;

	private int leaderCardPos;

	public void setLeaderCardPos(int leaderCardPos) {
		this.leaderCardPos = leaderCardPos;
	}


	public WarehouseExtraSpace(Resource storedResource) {
		this.storedResource = storedResource;
	}

	/**
	 * @see SpecialAbility#activate(it.polimi.ingsw.model.Player)
	 */
	@Override
	public void activate(Player p) {
		p.getDashboard().activateExtraDeposit(leaderCardPos);
	}

	/**
	 * Method to get the type of this special ability.
	 * @return the type of this special ability.
	 */
	public SpecialAbilityType getType() {
		return SpecialAbilityType.WAREHOUSE_EXTRA_SPACE;
	}

	public Resource getStoredResource() {
		return storedResource;
	}

	/**
	 * To string method of the class.
	 * TODO test
	 * @return a string representation of the object.
	 */
	public String toString() {

		String result = "";

		result += "SA=WES;";

		result += "R=" + storedResource.toString() + ";";

		return result;

	}

}
