package it.polimi.ingsw.model.marbles;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;

import java.io.Serializable;

public class RedMarble implements Marble, Serializable {
    /**
     * @param p
     * @see Marble#getResource(Player)
     */
    @Override
    public Resource getResource(Player p) {
        p.getDashboard().moveFaithMarker(1);
        return null;
    }

    @Override
    public String getType() {
        return "RED";
    }

}
