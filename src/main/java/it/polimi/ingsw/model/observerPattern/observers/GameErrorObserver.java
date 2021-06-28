package it.polimi.ingsw.model.observerPattern.observers;

import it.polimi.ingsw.model.utils.GameError;

/**
 * Interface class for observers of GameError.
 */
public interface GameErrorObserver {

    /**
     * Handles the updates for the observer.
     *
     * @param message   update message.
     */
    void update(GameError message);
}
