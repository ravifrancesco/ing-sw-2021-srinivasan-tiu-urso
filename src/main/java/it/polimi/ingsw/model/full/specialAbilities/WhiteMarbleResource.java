package it.polimi.ingsw.model.full.specialAbilities;

import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.full.table.Resource;

import java.io.Serializable;

public class WhiteMarbleResource implements SpecialAbility, Serializable {

    Resource res;

    /**
     * The constructor for the class: changes the effect of a WhiteMarble's getResources method (effect)
     * by providing an actual resource instead of null.
     *
     * @param res the resource to be returned.
     */
    public WhiteMarbleResource(Resource res) {
        this.res = res;
    }

    /**
     * @see SpecialAbility#activate(Player)
     */
    @Override
    public void activate(Player p) {
        p.addWMR(this);
    }

    /**
     * Getter for the associated resource with the WMR
     *
     * @return the WMR associated resource
     */
    public Resource getRes() {
        return res;
    }

    /**
     * Method to get the type of this special ability.
     *
     * @return the type of this special ability.
     */
    @Override
    public SpecialAbilityType getType() {
        return SpecialAbilityType.WHITE_MARBLE_RESOURCES;
    }

    /**
     * To string method of the class.
     * TODO test
     *
     * @return a string representation of the object.
     */
    public String toString() {

        String result = "";

        result += "SA=WMR;";

        result += "R=" + res.toString() + ";";

        return result;

    }


}
