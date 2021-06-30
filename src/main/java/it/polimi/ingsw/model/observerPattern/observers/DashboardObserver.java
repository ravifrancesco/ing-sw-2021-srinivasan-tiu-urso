package it.polimi.ingsw.model.observerPattern.observers;

import it.polimi.ingsw.model.full.table.Dashboard;

/**
 * Interface class for observers of Dashboard.
 */
public interface DashboardObserver {

    /**
     * Handles the updates for the observer.
     *
     * @param message   update message.
     */
    void update(Dashboard message);

}