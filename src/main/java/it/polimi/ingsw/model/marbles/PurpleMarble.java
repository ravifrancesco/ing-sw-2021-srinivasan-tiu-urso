package it.polimi.ingsw.model.marbles;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;

import java.io.Serializable;

public class PurpleMarble implements Marble, Serializable {
    /**
     * @see Marble#getResource(Player)
     * @param p
     */
    @Override
    public Resource getResource(Player p) {
        return Resource.SERVANT;
    }
}
