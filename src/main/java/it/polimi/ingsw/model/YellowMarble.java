package it.polimi.ingsw.model;

public class YellowMarble implements Marble {

    /**
     * @see Marble#getResource(Player)
     * @param p
     */
    @Override
    public Resource getResource(Player p) {
        return Resource.GOLD;
    }

}
