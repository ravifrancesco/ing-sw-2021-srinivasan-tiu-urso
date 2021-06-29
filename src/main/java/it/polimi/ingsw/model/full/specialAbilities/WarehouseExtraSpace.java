package it.polimi.ingsw.model.full.specialAbilities;

import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.full.table.Resource;

import java.io.Serializable;

public class WarehouseExtraSpace implements SpecialAbility, Serializable {

	private final Resource storedResource;

	private int leaderCardPos;

	/**
	 * Sets a leader card position
	 * @param leaderCardPos the position
	 */
	public void setLeaderCardPos(int leaderCardPos) {
		this.leaderCardPos = leaderCardPos;
	}


	public WarehouseExtraSpace(Resource storedResource) {
		this.storedResource = storedResource;
	}

	/**
	 * @see SpecialAbility#activate(Player)
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
	 * @return a string representation of the object.
	 */
	public String toString() {

		String result = "";

		result += "SA=WES;";

		result += "R=" + storedResource.toString() + ";";

		return result;

	}

}
