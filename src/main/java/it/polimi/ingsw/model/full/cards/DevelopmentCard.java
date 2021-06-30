package it.polimi.ingsw.model.full.cards;

import it.polimi.ingsw.model.full.specialAbilities.ProductionPower;
import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.full.table.Resource;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The class represents a Development Card of the game.
 * The object memorizes the state of a Development Card. The state includes:
 * The id.
 * The victory points.
 * The resource cost to buy the card.
 * The production power.
 * The banner of the card.
 */

public class DevelopmentCard implements Card, Serializable {

    private final int id;
    private final int victoryPoints;
    private final Map<Resource, Integer> resourceCost;
    private final ProductionPower productionPower;
    private final Banner banner;

    /**
     * The constructor for a Development Card object.
     *
     * @param id              represents the unique id of the card.
     * @param victoryPoints   represents the victory points given by the card.
     * @param resourceCost    represents the resource cost to buy the card.
     * @param productionPower represents the production power of the card.
     * @param banner          represents the banner of the card.
     */
    public DevelopmentCard(int id, int victoryPoints, Map<Resource, Integer> resourceCost, ProductionPower productionPower, Banner banner) {
        this.id = id;
        this.victoryPoints = victoryPoints;
        this.resourceCost = resourceCost.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.productionPower = productionPower;
        this.banner = banner;
    }

    /**
     * Allows to activate the production power.
     *
     * @param p represents the player.
     */
    @Override
    public void activate(Player p) {
        productionPower.activate(p);
    }

    /**
     * Getter for the id.
     *
     * @return the id of the card.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for victory points.
     *
     * @return the victory points given by the card.
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Getter for resourceCost.
     *
     * @return the resource cost of the card.
     */
    public Map<Resource, Integer> getResourceCost() {
        return resourceCost.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Getter for productionPower.
     *
     * @return the production power of the card.
     */
    public ProductionPower getProductionPower() {
        return productionPower;
    }

    /**
     * Getter for banner.
     *
     * @return the banner of the card.
     */
    public Banner getBanner() {
        return banner;
    }

    /**
     * To string method of the class.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        String result = "";

        result += "ID=" + id + ";VP=" + victoryPoints + ";" + banner.toString();

        result += resourceCost.keySet().stream()
                .map(key -> key + ":" + resourceCost.get(key))
                .collect(Collectors.joining(",", "RC=", ";"));

        result += productionPower.toString();

        return result;
    }

    /**
     * Allows to reset the production power of the card (in order to not activate the same production power twice in one turn).
     */
    public void resetProductionPower() {
        this.productionPower.reset();
    }

    /**
     * Equals method for the class.
     *
     * @param o the other development card to compare.
     * @return true if the two cards have the same id, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DevelopmentCard that = (DevelopmentCard) o;

        return id == that.id;
    }
}
