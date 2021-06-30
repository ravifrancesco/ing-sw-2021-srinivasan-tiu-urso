package it.polimi.ingsw.model.full.marbles;

import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.full.table.Resource;

public interface Marble {

    /**
     * Returns the corrisponding resource from the marble.
     *
     * @param p the player receiving the resource.
     * @return the resource associated with the marble.
     */
    Resource getResource(Player p);

    /**
     * Returns the color of the marble.
     *
     * @return the color of the marble
     */
    MarbleColor getMarbleColor();
}

