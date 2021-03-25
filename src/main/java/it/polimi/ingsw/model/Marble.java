package it.polimi.ingsw.model;

public interface Marble {

    /**
     * Returns the corrisponding resource from the marble.
     * @param p the player receiving the resource.
     * @return the resource associated with the marble.
     */
    Resource getResource(Player p);

    /**
     * Returns the code associated with the marble: (used for initial debugging)
     * 0 for GreyMarble
     * 1 for PurpleMarble
     * 2 for YellowMarble
     * 3 for BlueMarble
     * 4 for RedMarble
     * 5 for WhiteMarble
     * @return the corresponding resource.
     */
    int getNum();

}

