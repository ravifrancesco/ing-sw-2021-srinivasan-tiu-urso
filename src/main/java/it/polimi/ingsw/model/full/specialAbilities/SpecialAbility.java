package it.polimi.ingsw.model.full.specialAbilities;

import it.polimi.ingsw.model.full.table.Player;

public interface SpecialAbility {

	void activate(Player p);

	SpecialAbilityType getType();
}
