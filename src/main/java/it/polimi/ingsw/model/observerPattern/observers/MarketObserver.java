package it.polimi.ingsw.model.observerPattern.observers;

import it.polimi.ingsw.model.full.table.Market;

/**
 * Interface class for observers of Market.
 */
public interface MarketObserver {

    /**
     * Handles the updates for the observer.
     *
     * @param message update message.
     */
    void update(Market message);

}
