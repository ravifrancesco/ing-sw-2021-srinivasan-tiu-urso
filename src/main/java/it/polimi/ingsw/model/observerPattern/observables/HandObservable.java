package it.polimi.ingsw.model.observerPattern.observables;

import it.polimi.ingsw.model.Hand;
import it.polimi.ingsw.model.observerPattern.observers.HandObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Hand observable
 */
public class HandObservable {

    private List<HandObserver> observers = new ArrayList<>();

    /**
     * Add observer.
     *
     * @param observer observer to add.
     */
    public void addObserver(HandObserver observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Remove observer.
     *
     * @param observer  observer to remove.
     */
    public void removeObserver(HandObserver observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Notify observes.
     *
     * @param message   message for notification.
     */
    public void notify(Hand message){
        synchronized (observers) {
            for (HandObserver observer : observers) {
                observer.update(message);
            }
        }
    }

}