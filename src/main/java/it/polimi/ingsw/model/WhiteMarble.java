package it.polimi.ingsw.model;

public class WhiteMarble implements Marble {

    /**
     * @see Marble#getResource(Player)
     * @param p
     */
    @Override
    public Resource getResource(Player p) {
        // need to add a check to wheter there is an active power
        /*
                if(!checkWhiteMarbleResource()) {

        }
         */
        return null;
    }
}
