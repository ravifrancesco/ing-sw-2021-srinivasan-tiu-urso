package it.polimi.ingsw.model;

public class WhiteMarble implements Marble {
    /**
     * @see Marble#getResource(Player)
     */
    @Override
    public Resource getResource(Player p) {
        // TODO: change getResource in order to get the choice from the player in case there are two WMR activated
        return p.checkActiveWMR() ? (p.getActivatedWMR().length == 1 ? p.getActivatedWMR()[0].getRes() : null) : (null);
        /*
        Extra details: if there are two WhiteMarbleResource activated leader cards, then the controller will have to
        ask the player to make a choice for which resource to be returned. At the moment null is returned but will
        have to be changed with the player's choice.
         */
    }
}
