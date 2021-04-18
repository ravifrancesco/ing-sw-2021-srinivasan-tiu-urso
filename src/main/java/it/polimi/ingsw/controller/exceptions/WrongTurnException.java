package it.polimi.ingsw.controller.exceptions;

public class WrongTurnException extends Exception {

    public WrongTurnException(String errorMessage) {
        super(errorMessage);
    }

}
