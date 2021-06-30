package it.polimi.ingsw.model.observerPattern.observers;

import it.polimi.ingsw.model.full.table.Game;

/**
 * Interface class for observers of Game.
 */
public interface GameObserver {

    /**
     * Handles the updates for the observer.
     *
     * @param message update message.
     */
    void update(Game message);

}