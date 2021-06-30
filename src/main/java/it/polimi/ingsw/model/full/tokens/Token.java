package it.polimi.ingsw.model.full.tokens;

import it.polimi.ingsw.model.full.table.Game;

public interface Token {

    /**
     * Activates a token
     *
     * @param game the associated game
     * @return true if token end the game with a Lorenzo move
     */
    boolean useToken(Game game);

    /**
     * Returns the type of the Token
     *
     * @return the type of the Token
     */
    String getType();
}
