package it.polimi.ingsw.model.observerPattern.observables;

import it.polimi.ingsw.model.full.table.Warehouse;
import it.polimi.ingsw.model.observerPattern.observers.WarehouseObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Warehouse observable
 */
public class WarehouseObservable {

    private final List<WarehouseObserver> observers = new ArrayList<>();

    /**
     * Add observer.
     *
     * @param observer observer to add.
     */
    public void addObserver(WarehouseObserver observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Remove observer.
     *
     * @param observer  observer to remove.
     */
    public void removeObserver(WarehouseObserver observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Notify observes.
     *
     * @param message   message for notification.
     */
    public void notify(Warehouse message){
        synchronized (observers) {
            for (WarehouseObserver observer : observers) {
                observer.update(message);
            }
        }
    }

}

