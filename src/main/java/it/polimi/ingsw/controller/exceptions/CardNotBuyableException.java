package it.polimi.ingsw.controller.exceptions;

public class CardNotBuyableException extends Exception {

    public CardNotBuyableException(String errorMessage) {
        super(errorMessage);
    }
}
