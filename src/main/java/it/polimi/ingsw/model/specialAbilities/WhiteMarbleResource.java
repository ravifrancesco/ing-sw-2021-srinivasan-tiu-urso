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

	public Resource getRes() {
		return res;
	}

	/**
	 * Method to get the type of this special ability.
	 * @return the type of this special ability.
	 */
	@Override
	public SpecialAbilityType getType() {
		return SpecialAbilityType.WHITE_MARBLE_RESOURCES;
	}

	/**
	 * To string method of the class.
	 * TODO test
	 * @return a string representation of the object.
	 */
	public String toString() {

		String result = "";

		result += "SA=WMR;";

		result += "R=" + res.toString() + ";";

		return result;

	}


}
