package it.polimi.ingsw.model.observerPattern.observers;

import it.polimi.ingsw.model.Game;

/**
 * Interface class for observers of Game.
 */
public interface GameObserver {

    /**
     * Handles the updates for the observer.
     *
     * @param message   update message.
     */
    public void update(Game message);

}