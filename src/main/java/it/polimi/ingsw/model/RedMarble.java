package it.polimi.ingsw.model;

public class RedMarble implements Marble {

    /**
     * @see Marble#getResource(Player)
     * @param p
     */
    @Override
    public Resource getResource(Player p) {
        // p.getDashboard().moveFaithMarker(1); // not implemented yet
        return null;
    }

    public int getNum() {
        return 4;
    }
}
