package it.polimi.ingsw.model;

public class GreyMarble implements Marble {

    private int id;

    public GreyMarble() {
        this.id = 1;
    }

    /**
     * @see Marble#getResource(Player)
     * @param p
     */


    @Override
    public Resource getResource(Player p) {
        return Resource.STONE;
    }

    @Override
    public int getId() {
        return this.id;
    }

}
