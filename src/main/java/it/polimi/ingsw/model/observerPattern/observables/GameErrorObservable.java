package it.polimi.ingsw.model.observerPattern.observables;

import it.polimi.ingsw.model.observerPattern.observers.GameErrorObserver;
import it.polimi.ingsw.model.utils.GameError;

import java.util.ArrayList;
import java.util.List;

public class GameErrorObservable {

    private final List<GameErrorObserver> observers = new ArrayList<>();

    /**
     * Add observer.
     *
     * @param observer observer to add.
     */
    public void addObserver(GameErrorObserver observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Notify observes.
     *
     * @param message message for notification.
     */
    public void notify(GameError message) {
        synchronized (observers) {
            for (GameErrorObserver observer : observers) {
                observer.update(message);
            }
        }
    }
}
