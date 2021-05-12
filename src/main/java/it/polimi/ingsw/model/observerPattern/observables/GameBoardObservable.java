package it.polimi.ingsw.model.observerPattern.observables;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.observerPattern.observers.GameBoardObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * GameBoard observable
 */
public class GameBoardObservable {

    private List<GameBoardObserver> observers = new ArrayList<>();

    /**
     * Add observer.
     *
     * @param observer observer to add.
     */
    public void addObserver(GameBoardObserver observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Remove observer.
     *
     * @param observer  observer to remove.
     */
    public void removeObserver(GameBoardObserver observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Notify observes.
     *
     * @param message   message for notification.
     */
    public void notify(GameBoard message){
        synchronized (observers) {
            for (GameBoardObserver observer : observers) {
                observer.update(message);
            }
        }
    }

}