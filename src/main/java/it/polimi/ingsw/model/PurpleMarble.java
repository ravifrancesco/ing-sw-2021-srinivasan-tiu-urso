package it.polimi.ingsw.model;

public class PurpleMarble implements Marble {

    private int id;

    public PurpleMarble() {
        this.id = 2;
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
        return Resource.SERVANT;
    }

    @Override
    public int hashCode() {
        return 2;
    }

}
