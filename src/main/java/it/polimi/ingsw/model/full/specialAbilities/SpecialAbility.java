package it.polimi.ingsw.model.full.specialAbilities;

import it.polimi.ingsw.model.full.table.Player;

/**
 * The interface represent a special ability of a game's card.
 */

public interface SpecialAbility {

    /**
     * Allows to activate the special ability.
     *
     * @param p represents the player.
     */
    void activate(Player p);

    /**
     * Allows to know the type of special ability.
     *
     * @return the special ability type.
     */
    SpecialAbilityType getType();
}
