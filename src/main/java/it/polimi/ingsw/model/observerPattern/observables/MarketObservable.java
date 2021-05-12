package it.polimi.ingsw.model.observerPattern.observables;

import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.observerPattern.observers.MarketObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Market observable
 */
public class MarketObservable {

    private List<MarketObserver> observers = new ArrayList<>();

    /**
     * Add observer.
     *
     * @param observer observer to add.
     */
    public void addObserver(MarketObserver observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Remove observer.
     *
     * @param observer  observer to remove.
     */
    public void removeObserver(MarketObserver observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Notify observes.
     *
     * @param message   message for notification.
     */
    public void notify(Market message){
        synchronized (observers) {
            for (MarketObserver observer : observers) {
                observer.update(message);
            }
        }
    }

}