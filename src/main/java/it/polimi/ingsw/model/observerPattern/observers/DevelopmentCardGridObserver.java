package it.polimi.ingsw.model.observerPattern.observers;

import it.polimi.ingsw.model.full.table.DevelopmentCardGrid;

/**
 * Interface class for observers of DevelopmentCardGrid.
 */
public interface DevelopmentCardGridObserver {

    /**
     * Handles the updates for the observer.
     *
     * @param message   update message.
     */
    public void update(DevelopmentCardGrid message);

}