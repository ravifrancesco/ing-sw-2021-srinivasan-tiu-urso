package it.polimi.ingsw.model;

public class RedMarble implements Marble {
    /**
     * @param p
     * @see Marble#getResource(Player)
     */
    @Override
    public Resource getResource(Player p) {
        // p.getDashboard().moveFaithMarker(1); // TODO implement after dashboard implementation
        return null;
    }
}
