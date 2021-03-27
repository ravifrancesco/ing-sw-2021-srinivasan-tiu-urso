package it.polimi.ingsw.model;

public interface Marble {

    /**
     * Returns the corrisponding resource from the marble.
     * @param p the player receiving the resource.
     * @return the resource associated with the marble.
     */
    Resource getResource(Player p);
}

