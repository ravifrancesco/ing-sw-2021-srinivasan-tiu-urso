package it.polimi.ingsw.model;

public class YellowMarble implements Marble {

    private int id;

    public YellowMarble() {
        id = 3;
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
        return Resource.GOLD;
    }


    @Override
    public int hashCode() {
        return 3;
    }

}
