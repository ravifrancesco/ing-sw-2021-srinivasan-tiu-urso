package it.polimi.ingsw.model;

public class WarehouseExtraSpace implements SpecialAbility {

	Resource storedResource;

	public WarehouseExtraSpace(Resource storedResource) {
		this.storedResource = storedResource;
	}

	/**
	 * @see it.polimi.ingsw.model.SpecialAbility#activate(it.polimi.ingsw.model.Player)
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
