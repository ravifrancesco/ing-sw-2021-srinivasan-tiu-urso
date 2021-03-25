package it.polimi.ingsw.model;

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
