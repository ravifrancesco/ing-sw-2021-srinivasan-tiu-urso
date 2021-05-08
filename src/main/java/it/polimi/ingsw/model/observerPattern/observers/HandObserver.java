package it.polimi.ingsw.model.observerPattern.observers;

import it.polimi.ingsw.model.Hand;

/**
 * Interface class for observers of Hand.
 */
public interface HandObserver {

    /**
     * Handles the updates for the observer.
     *
     * @param message   update message.
     */
    public void update(Hand message);

}