package it.polimi.ingsw.model.full.cards;

import it.polimi.ingsw.model.full.table.Player;

/**
 * The interface represents a card of the game.
 */

public interface Card {

    /**
     * Allows to activate the special ability of a card.
     *
     * @param p represents the player who activated the card.
     */
    void activate(Player p);

}