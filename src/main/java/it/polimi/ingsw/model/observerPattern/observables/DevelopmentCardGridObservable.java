package it.polimi.ingsw.model.observerPattern.observables;

import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.DevelopmentCardGrid;
import it.polimi.ingsw.model.observerPattern.observers.DashboardObserver;
import it.polimi.ingsw.model.observerPattern.observers.DevelopmentCardGridObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * DevelopmentCardGrid observable
 */
public class DevelopmentCardGridObservable {

    private List<DevelopmentCardGridObserver> observers = new ArrayList<>();

    /**
     * Add observer.
     *
     * @param observer observer to add.
     */
    public void addObserver(DevelopmentCardGridObserver observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Remove observer.
     *
     * @param observer  observer to remove.
     */
    public void removeObserver(DevelopmentCardGridObserver observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Notify observes.
     *
     * @param message   message for notification.
     */
    public void notify(DevelopmentCardGrid message){
        synchronized (observers) {
            for (DevelopmentCardGridObserver observer : observers) {
                observer.update(message);
            }
        }
    }

}