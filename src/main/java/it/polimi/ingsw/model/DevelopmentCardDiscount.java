package it.polimi.ingsw.model;

/**
 * The class represents the special ability of a leader card which allows to buy a development card
 * from the grid with a discount.
 */

public class DevelopmentCardDiscount implements SpecialAbility {

	private Resource resource;
	private int quantity;

	/**
	 * The constructor for a DevelopmentCardDiscount object.
	 * @param resource the resource associated with the discount.
	 * @param quantity the quantity of the discount.
	 */

	public DevelopmentCardDiscount(Resource resource, int quantity) {
		this.resource = resource;
		this.quantity = quantity;
	}

	/**
	 * Activate the discount special ability. It adds this object to the discount collection of the player.
	 * @param p the player who activated the discount.
	 */

	@Override
	public void activate(Player p) {
		p.addActiveDiscount(this);
	}

	/**
	 * Getter for the resource.
	 * @return the resource associated with the discount.
	 */

	public Resource getResource() {
		return resource;
	}

	/**
	 * Getter for the quantity.
	 * @return the quantity of the discount.
	 */

	public int getQuantity() {
		return quantity;
	}
}