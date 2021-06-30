package it.polimi.ingsw.model.full.marbles;

import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.full.table.Resource;

import java.io.Serializable;

public class YellowMarble implements Marble, Serializable {
    /**
     * @param p
     * @see Marble#getResource(Player)
     */
    @Override
    public Resource getResource(Player p) {
        return Resource.GOLD;
    }

    /**
     * @see Marble#getMarbleColor()
     */
    @Override
    public MarbleColor getMarbleColor() {
        return MarbleColor.YELLOW;
    }
}
