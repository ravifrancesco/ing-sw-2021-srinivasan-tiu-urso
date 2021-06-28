package it.polimi.ingsw.model.observerPattern.observers;

import it.polimi.ingsw.model.full.table.GameBoard;

/**
 * Interface class for observers of GameBoard.
 */
public interface GameBoardObserver {

    /**
     * Handles the updates for the observer.
     *
     * @param message   update message.
     */
    public void update(GameBoard message);

}