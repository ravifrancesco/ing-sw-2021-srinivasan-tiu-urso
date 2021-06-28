package it.polimi.ingsw.model.observerPattern.observables;

import it.polimi.ingsw.model.full.table.Game;
import it.polimi.ingsw.model.observerPattern.observers.GameObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Game observable
 */
public class GameObservable {

    private List<GameObserver> observers = new ArrayList<>();

    /**
     * Add observer.
     *
     * @param observer observer to add.
     */
    public void addObserver(GameObserver observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Remove observer.
     *
     * @param observer  observer to remove.
     */
    public void removeObserver(GameObserver observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Notify observes.
     *
     * @param message   message for notification.
     */
    public void notify(Game message){
        synchronized (observers) {
            for (GameObserver observer : observers) {
                observer.update(message);
            }
        }
    }

}