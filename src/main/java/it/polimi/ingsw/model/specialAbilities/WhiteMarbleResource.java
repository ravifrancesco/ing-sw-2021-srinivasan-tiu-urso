package it.polimi.ingsw.model.specialAbilities;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;

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
	 * @see SpecialAbility#activate(it.polimi.ingsw.model.Player)
	 */
	@Override
	public void activate(Player p) {
		p.addWMR(this);
	}

	/**
	 * Getter for the associated resource with the WMR
	 * @return the WMR associated resource
	 */
	public Resource getRes() {
		return res;
	}

	/**
	 * Returns the type of the special ability
	 * @return the special ability type
	 */
	public SpecialAbilityType getType() {
		return SpecialAbilityType.WHITE_MARBLE_RESOURCES;
	}
}
