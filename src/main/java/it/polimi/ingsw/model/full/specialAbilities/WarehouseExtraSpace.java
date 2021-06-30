package it.polimi.ingsw.model.full.specialAbilities;

import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.full.table.Resource;

import java.io.Serializable;

/**
 * The class represents the warehouse extra space ability given by a leader card of the game.
 */

public class WarehouseExtraSpace implements SpecialAbility, Serializable {

    private final Resource storedResource;

    private int leaderCardPos;

    /**
     * The constructor for a Warehouse Extra Space object.
     * @param storedResource the type of stored resource.
     */
    public WarehouseExtraSpace(Resource storedResource) {
        this.storedResource = storedResource;
    }

    /**
     * Sets a leader card position
     *
     * @param leaderCardPos the position
     */
    public void setLeaderCardPos(int leaderCardPos) {
        this.leaderCardPos = leaderCardPos;
    }

    /**
     * @see SpecialAbility#activate(Player)
     */
    @Override
    public void activate(Player p) {
        p.getDashboard().activateExtraDeposit(leaderCardPos);
    }

    /**
     * Method to get the type of this special ability.
     *
     * @return the type of this special ability.
     */
    public SpecialAbilityType getType() {
        return SpecialAbilityType.WAREHOUSE_EXTRA_SPACE;
    }

    /**
     * Method to get the type of the stored resource.
     *
     * @return the type of the stored resource.
     */
    public Resource getStoredResource() {
        return storedResource;
    }

    /**
     * To string method of the class.
     *
     * @return a string representation of the object.
     */
    public String toString() {

        String result = "";

        result += "SA=WES;";

        result += "R=" + storedResource.toString() + ";";

        return result;

    }

}
