package it.polimi.ingsw.model.specialAbilities;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;

public class WarehouseExtraSpace implements SpecialAbility {

	Resource storedResource;

	public WarehouseExtraSpace(Resource storedResource) {
		this.storedResource = storedResource;
	}

	/**
	 * @see SpecialAbility#activate(it.polimi.ingsw.model.Player)
	 */
	@Override
	public void activate(Player p) {
		p.getDashboard().getWarehouse().activateExtraDeposit(storedResource);
	}

	//TODO doc
	public SpecialAbilityType getType() {
		return SpecialAbilityType.WAREHOUSE_EXTRA_SPACE;
	}
}
