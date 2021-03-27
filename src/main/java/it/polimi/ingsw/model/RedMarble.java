package it.polimi.ingsw.model;

public class RedMarble implements Marble {

    private int id;

    public RedMarble() {
        this.id = 5;
    }

    /**
     * @param p
     * @see Marble#getResource(Player)
     */
    @Override
    public Resource getResource(Player p) {
        // p.getDashboard().moveFaithMarker(1); // TODO implement after dashboard implementation
        return null;
    }

    @Override
    public int getId() {
        return id;
    }
}
