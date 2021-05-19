package it.polimi.ingsw.model;

import it.polimi.ingsw.model.observerPattern.observables.GameErrorObservable;
import it.polimi.ingsw.utils.Pair;

import java.io.Serializable;

public class GameError extends GameErrorObservable {

    private Pair<String, Exception> error;

    public void setError(Pair<String, Exception> error) {
        this.error = error;
    }
}
