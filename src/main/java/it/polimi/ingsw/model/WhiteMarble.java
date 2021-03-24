package it.polimi.ingsw.model;

public class WhiteMarble implements Marble {

    /**
     * @see Marble#getResource(Player)
     * @param p
     */
    @Override
    public Resource getResource(Player p) {
        return null;
    }

    public int getNum() {
        return 5;
    }

}
