package it.polimi.ingsw.model.observerPattern.observables;

import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.observerPattern.observers.PlayerObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Player observable
 */
public class PlayerObservable {

    private final List<PlayerObserver> observers = new ArrayList<>();

    /**
     * Add observer.
     *
     * @param observer observer to add.
     */
    public void addObserver(PlayerObserver observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Remove observer.
     *
     * @param observer observer to remove.
     */
    public void removeObserver(PlayerObserver observer) {
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Notify observes.
     *
     * @param message message for notification.
     */
    public void notify(Player message) {
        synchronized (observers) {
            for (PlayerObserver observer : observers) {
                observer.update(message);
            }
        }
    }

}