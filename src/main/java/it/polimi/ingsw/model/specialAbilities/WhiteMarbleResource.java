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
	 * Returns the resource associated with the ability.
	 * @return the returned resource
	 */
	public Resource getRes() {
		return res;
	}

	/**
	 * Returnes the type of the special ability
	 * @return the special ability's type
	 */
	public SpecialAbilityType getType() {
		return SpecialAbilityType.WHITE_MARBLE_RESOURCES;
	}
}
