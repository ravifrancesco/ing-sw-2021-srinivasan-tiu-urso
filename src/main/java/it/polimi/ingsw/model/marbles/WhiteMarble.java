package it.polimi.ingsw.model.marbles;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;


public class WhiteMarble implements Marble {
    /**
     * @see Marble#getResource(Player)
     */
    @Override
    public Resource getResource(Player p) {
        return p.checkActiveWMR() ? (p.getActivatedWMR().length == 1 ? p.getActivatedWMR()[0] : Resource.ANY) : (null);
        /*
        If no WMR special ability is activated, null will be returned.
        If there is only one WMR special ability activated, player input is not required and the associated resource
        will automatically be returned.
        If there are two WMR special abilities activated, the resource 'ANY' will be returned and the controller will
        manage it correctly. (see ServerController.java for more)
         */
    }
}
