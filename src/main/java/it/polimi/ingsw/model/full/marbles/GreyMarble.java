package it.polimi.ingsw.model.full.marbles;

import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.full.table.Resource;

import java.io.Serializable;

public class GreyMarble implements Marble, Serializable {
    /**
     * @param p
     * @see Marble#getResource(Player)
     */
    @Override
    public Resource getResource(Player p) {
        return Resource.STONE;
    }

    /**
     * @see Marble#getMarbleColor()
     */
    @Override
    public MarbleColor getMarbleColor() {
        return MarbleColor.GREY;
    }
}
