package it.polimi.ingsw.model.marbles;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;

public class BlueMarble implements Marble {
    /**
     * @see Marble#getResource(Player)
     * @param p
     */
    @Override
    public Resource getResource(Player p) {
        return Resource.SHIELD;
    }

}
