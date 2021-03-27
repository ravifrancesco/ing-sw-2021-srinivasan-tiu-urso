package it.polimi.ingsw.model;

public class WhiteMarble implements Marble {

    private int id;

    public WhiteMarble() {
        this.id = 6;
    }

    @Override
    public int getId() {
        return id;
    }

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
