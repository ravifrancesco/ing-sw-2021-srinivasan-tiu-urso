package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.observerPattern.observables.GameErrorObservable;
import it.polimi.ingsw.utils.Pair;

import java.io.Serializable;

public class GameError extends GameErrorObservable {

    private Pair<String, Exception> error;

    public void setError(Pair<String, Exception> error) {
        this.error = error;
        notify(this);
    }

    /**
     * Used to retrieve the last error.
     *
     * @return the last error and the nickname of the player who caused the error.
     */
    public Pair<String, Exception> getError() {
        return error;
    }

}
