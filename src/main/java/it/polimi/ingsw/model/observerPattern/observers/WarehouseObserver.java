package it.polimi.ingsw.model.observerPattern.observers;

import it.polimi.ingsw.model.Warehouse;

/**
 * Interface class for observers of Warehouse.
 */
public interface WarehouseObserver {

    /**
     * Handles the updates for the observer.
     *
     * @param message   update message.
     */
    public void update(Warehouse message);

}