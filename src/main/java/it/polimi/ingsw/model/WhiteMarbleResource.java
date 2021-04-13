package it.polimi.ingsw.model;

public class WhiteMarbleResource implements SpecialAbility {

	Resource res;

	/**
	 * The constructor for the class: changes the effect of a WhiteMarble's getResources method (effect)
	 * by providing an actual resource instead of null.
	 * @param res the resource to be returned.
	 */
	public WhiteMarbleResource(Resource res) {
		this.res = res;
	}

	/**
	 * @see it.polimi.ingsw.model.SpecialAbility#activate(it.polimi.ingsw.model.Player)
	 */
	@Override
	public void activate(Player p) {
		p.addWMR(this);
	}

	public Resource getRes() {
		return res;
	}
}
