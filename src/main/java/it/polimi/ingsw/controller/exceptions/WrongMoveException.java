package it.polimi.ingsw.controller.exceptions;

public class WrongMoveException extends Exception {

    public WrongMoveException(String errorMessage) {
        super(errorMessage);
    }
}
