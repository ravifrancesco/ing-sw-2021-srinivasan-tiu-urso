package it.polimi.ingsw.model.full.tokens;

import it.polimi.ingsw.model.full.table.Game;

public interface Token {

    boolean useToken(Game game);

    String getType();
}
