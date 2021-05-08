package it.polimi.ingsw.model.observerPattern.observers;

import it.polimi.ingsw.model.FaithTrack;

/**
 * Interface class for observers of faith trakc.
 */
public interface FaithTrackObserver {

    /**
     * Handles the updates for the observer.
     *
     * @param message   update message.
     */
    public void update(FaithTrack message);

}
