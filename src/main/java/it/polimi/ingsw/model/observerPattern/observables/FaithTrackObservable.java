package it.polimi.ingsw.model.observerPattern.observables;

import it.polimi.ingsw.model.full.table.FaithTrack;
import it.polimi.ingsw.model.observerPattern.observers.FaithTrackObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * FaithTrack observable
 */
public class FaithTrackObservable {

    private List<FaithTrackObserver> observers = new ArrayList<>();

    /**
     * Add observer.
     *
     * @param observer observer to add.
     */
    public void addObserver(FaithTrackObserver observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Remove observer.
     *
     * @param observer  observer to remove.
     */
    public void removeObserver(FaithTrackObserver observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Notify observes.
     *
     * @param message   message for notification.
     */
    public void notify(FaithTrack message){
        synchronized (observers) {
            for (FaithTrackObserver observer : observers) {
                observer.update(message);
            }
        }
    }

}

