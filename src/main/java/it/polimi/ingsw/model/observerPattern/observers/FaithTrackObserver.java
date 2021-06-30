package it.polimi.ingsw.model.observerPattern.observers;

import it.polimi.ingsw.model.full.table.FaithTrack;

/**
 * Interface class for observers of FaithTrack.
 */
public interface FaithTrackObserver {

    /**
     * Handles the updates for the observer.
     *
     * @param message update message.
     */
    void update(FaithTrack message);

}
