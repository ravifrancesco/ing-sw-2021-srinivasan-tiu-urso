package it.polimi.ingsw.model.observerPattern.observers;

import it.polimi.ingsw.model.FaithTrack;
import it.polimi.ingsw.model.Market;

/**
 * Interface class for observers of Market.
 */
public interface MarketObserver {

    /**
     * Handles the updates for the observer.
     *
     * @param message   update message.
     */
    public void update(Market message);

}
