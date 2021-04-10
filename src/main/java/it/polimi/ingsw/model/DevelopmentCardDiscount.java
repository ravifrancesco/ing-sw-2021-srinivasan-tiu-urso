package it.polimi.ingsw.model;

public class DevelopmentCardDiscount implements SpecialAbility {

	private Resource resource;
	private int quantity;

	public DevelopmentCardDiscount(Resource resource, int quantity) {
		this.resource = resource;
		this.quantity = quantity;
	}

	@Override
	public void activate(Player p) {
		p.addActiveDiscount(this);
	}

	public Resource getResource() {
		return resource;
	}

	public int getQuantity() {
		return quantity;
	}
}