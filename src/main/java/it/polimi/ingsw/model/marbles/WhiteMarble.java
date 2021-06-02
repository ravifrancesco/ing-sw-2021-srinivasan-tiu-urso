package it.polimi.ingsw.model.marbles;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;

import java.io.Serializable;

public class WhiteMarble implements Marble, Serializable {
    /**
     * @see Marble#getResource(Player)
     */
    @Override
    public Resource getResource(Player p) {
        return p.checkActiveWMR() ? Resource.ANY : null;
        /*
        Extra details: if there are two WhiteMarbleResource activated leader cards, then the controller will have to
        ask the player to make a choice for which resource to be returned. At the moment null is returned but will
        have to be changed with the player's choice.
         */
    }
}
