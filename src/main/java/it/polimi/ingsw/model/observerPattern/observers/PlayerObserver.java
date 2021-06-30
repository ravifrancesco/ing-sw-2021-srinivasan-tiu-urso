package it.polimi.ingsw.model.observerPattern.observers;

import it.polimi.ingsw.model.full.table.Player;

/**
 * Interface class for observers of Player.
 */
public interface PlayerObserver {

    /**
     * Handles the updates for the observer.
     *
     * @param message update message.
     */
    void update(Player message);

}