package it.polimi.ingsw.model.observerPattern.observables;

import it.polimi.ingsw.model.utils.GameError;
import it.polimi.ingsw.model.observerPattern.observers.GameErrorObserver;

import java.util.ArrayList;
import java.util.List;

public class GameErrorObservable {

    private List<GameErrorObserver> observers = new ArrayList<>();

    /**
     * Add observer.
     *
     * @param observer observer to add.
     */
    public void addObserver(GameErrorObserver observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Remove observer.
     *
     * @param observer  observer to remove.
     */
    public void removeObserver(GameErrorObserver observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Notify observes.
     *
     * @param message   message for notification.
     */
    public void notify(GameError message){
        synchronized (observers) {
            for (GameErrorObserver observer : observers) {
                observer.update(message);
            }
        }
    }
}
