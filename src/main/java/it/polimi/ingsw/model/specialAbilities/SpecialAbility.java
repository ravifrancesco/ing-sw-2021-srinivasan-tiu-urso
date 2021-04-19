package it.polimi.ingsw.model.specialAbilities;

import it.polimi.ingsw.model.Player;

public interface SpecialAbility {

	void activate(Player p);

	SpecialAbilityType getType(); // TODO add

}
