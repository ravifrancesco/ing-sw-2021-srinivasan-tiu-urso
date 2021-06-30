package it.polimi.ingsw.model.observerPattern.observers;

import it.polimi.ingsw.model.full.table.Warehouse;

/**
 * Interface class for observers of Warehouse.
 */
public interface WarehouseObserver {

    /**
     * Handles the updates for the observer.
     *
     * @param message   update message.
     */
    void update(Warehouse message);

}