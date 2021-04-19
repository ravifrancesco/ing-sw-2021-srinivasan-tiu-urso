package it.polimi.ingsw.controller.exceptions;

public class IllegalDepositStateException extends Exception {
    public IllegalDepositStateException(String errorMessage) {
        super(errorMessage);
    }
}
