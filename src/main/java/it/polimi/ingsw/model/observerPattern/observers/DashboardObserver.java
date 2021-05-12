package it.polimi.ingsw.model.observerPattern.observers;

import it.polimi.ingsw.model.Dashboard;

/**
 * Interface class for observers of Dashboard.
 */
public interface DashboardObserver {

    /**
     * Handles the updates for the observer.
     *
     * @param message   update message.
     */
    public void update(Dashboard message);

}