package it.polimi.ingsw.model;

public class BlueMarble implements Marble {

    private int id;

    public BlueMarble() {
        id = 4;
    }

    public int getId() {
        return id;
    }

    /**
     * @see Marble#getResource(Player)
     * @param p
     */
    @Override
    public Resource getResource(Player p) {
        return Resource.SHIELD;
    }

}
