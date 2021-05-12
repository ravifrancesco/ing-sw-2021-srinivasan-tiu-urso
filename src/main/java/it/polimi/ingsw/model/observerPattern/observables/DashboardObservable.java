package it.polimi.ingsw.model.observerPattern.observables;

import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.observerPattern.observers.DashboardObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Dashboard observable
 */
public class DashboardObservable {

    private List<DashboardObserver> observers = new ArrayList<>();

    /**
     * Add observer.
     *
     * @param observer observer to add.
     */
    public void addObserver(DashboardObserver observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Remove observer.
     *
     * @param observer  observer to remove.
     */
    public void removeObserver(DashboardObserver observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Notify observes.
     *
     * @param message   message for notification.
     */
    public void notify(Dashboard message){
        synchronized (observers) {
            for (DashboardObserver observer : observers) {
                observer.update(message);
            }
        }
    }

}