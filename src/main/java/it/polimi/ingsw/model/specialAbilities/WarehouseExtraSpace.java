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

	//TODO doc
	public SpecialAbilityType getType() {
		return SpecialAbilityType.WAREHOUSE_EXTRA_SPACE;
	}

	public Resource getStoredResource() {
		return storedResource;
	}

}
