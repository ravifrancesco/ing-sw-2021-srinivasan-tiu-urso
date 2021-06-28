package it.polimi.ingsw.model.singlePlayer.tokens;

import it.polimi.ingsw.model.Game;

public interface Token {

    boolean useToken(Game game);

    String getType();
}
