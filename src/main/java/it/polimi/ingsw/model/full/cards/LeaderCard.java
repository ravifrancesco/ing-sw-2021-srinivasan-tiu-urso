package it.polimi.ingsw.model.full.cards;

import it.polimi.ingsw.model.full.specialAbilities.SpecialAbility;
import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.full.table.Resource;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The class represents a Leader Card of the game.
 * The object memorizes the state of a Leader Card. The state includes:
 * The id.
 * The victory points.
 * The cost in terms of banner.
 * The cost in terms of resources.
 * The special ability of the card.
 */

public class LeaderCard implements Card, Serializable {

    private final int id;
    private final int victoryPoints;
    private final Map<Banner, Integer> bannerCost;
    private final Map<Resource, Integer> resourceCost;
    private final SpecialAbility specialAbility;

    /**
     * The constructor for a LeaderCard object.
     * It initializes the id, the victory points, the resource cost, the banner cost,
     * and the special ability.
     *
     * @param id             represents the unique id of the card.
     * @param victoryPoints  represents the victory points given by the card.
     * @param bannerCost     represents the banner cost to buy the card.
     * @param resourceCost   represents the resource cost to buy the card.
     * @param specialAbility represents the special ability of the card.
     */
    public LeaderCard(int id, int victoryPoints, Map<Banner, Integer> bannerCost, Map<Resource, Integer> resourceCost, SpecialAbility specialAbility) {
        this.id = id;
        this.victoryPoints = victoryPoints;
        this.bannerCost = bannerCost.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.resourceCost = resourceCost.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.specialAbility = specialAbility;
    }


    /**
     * Allows to activate the special ability of the leader card.
     *
     * @param p represents the player.
     */
    @Override
    public void activate(Player p) {
        specialAbility.activate(p);
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
     * Getter for the victory points.
     *
     * @return the victory points given by the card.
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Getter for bannerCost.
     *
     * @return the banner cost of the card.
     */
    public Map<Banner, Integer> getBannerCost() {
        return bannerCost.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
     * Getter for specialAbility.
     *
     * @return the special ability of the card.
     */
    public SpecialAbility getSpecialAbility() {
        return specialAbility;
    }

    /**
     * To string method of the class.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        String result = "";
        result += "ID=" + id + ";VP=" + victoryPoints + ";";

        result += bannerCost.keySet().stream()
                .map(key -> key.getColor().name() + ":" + key.getLevel() + ":" + bannerCost.get(key))
                .collect(Collectors.joining(",", "BC=", ";"));

        result += resourceCost.keySet().stream()
                .map(key -> key.name() + ":" + resourceCost.get(key))
                .collect(Collectors.joining(",", "RC=", ";"));

        result += specialAbility.toString();
        return result;
    }

    /**
     * Allows to know if the card is playable.
     *
     * @param playerBanners   all the banner of the player.
     * @param playerResources all the resources of the player.
     * @return true if the card is playable with the given banners, false otherwise.
     */
    public boolean isPlayable(Map<Banner, Integer> playerBanners, Map<Resource, Integer> playerResources) {
        Map<Banner, Integer> playerBannersCopy = playerBanners.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<Banner, Integer> bannerCostCopy = bannerCost.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<Resource, Integer> playerResourcesCopy = playerResources.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<Resource, Integer> resourceCostCopy = resourceCost.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        bannerCostCopy.entrySet().forEach(e -> playerBannersCopy.entrySet().stream()
                .filter(e2 -> e.getKey().equalsColor(e2.getKey()) && e.getKey().equalsLevel(e2.getKey()))
                .forEach(e2 -> {
                    int diff = e.getValue() - e2.getValue();
                    e.setValue(Math.max(diff, 0));
                    e2.setValue(diff < 0 ? -diff : 0);
                }));

        resourceCostCopy.entrySet().forEach(e -> playerResourcesCopy.entrySet().stream()
                .filter(e2 -> e.getKey() == e2.getKey())
                .forEach(e2 -> {
                    int diff = e.getValue() - e2.getValue();
                    e.setValue(Math.max(diff, 0));
                    e2.setValue(diff < 0 ? -diff : 0);
                }));

        return bannerCostCopy.entrySet().stream().allMatch(e -> e.getValue() == 0) && resourceCostCopy.entrySet().stream().allMatch(e -> e.getValue() == 0);
    }

    /**
     * Equals method for the class.
     *
     * @param o the other leader card to compare.
     * @return true if the two cards have the same id, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeaderCard that = (LeaderCard) o;

        return id == that.id;
    }


}
