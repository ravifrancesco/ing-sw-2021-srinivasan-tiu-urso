package it.polimi.ingsw.model;

public class PurpleMarble implements Marble {

    /**
     * @see Marble#getResource(Player)
     * @param p
     */
    @Override
    public Resource getResource(Player p) {
        return Resource.SERVANT;
    }

    public int getNum() {
        return 1;
    }

}
