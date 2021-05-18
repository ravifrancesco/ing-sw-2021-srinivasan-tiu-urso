package it.polimi.ingsw.model;

import it.polimi.ingsw.model.observerPattern.observables.GameErrorObservable;

import java.io.Serializable;

public class GameError extends GameErrorObservable implements Serializable {

    private Exception error;
    private String nickname;

    public GameError() {
        this.error = new Exception();
        this.nickname = "";
    }

    public void setError(Exception error, String nickname) {
        this.error = error;
        this.nickname = nickname;
    }
}
