package it.polimi.ingsw.model.full.cards;

import java.io.Serializable;

/**
 * The class represents a Banner of the game.
 * Each banner has a color (GREEN, YELLOW, BLUE or PURPLE)
 * and a level (1, 2 or 3).
 */

public class Banner implements Serializable {

    private final BannerEnum color;
    private final int level;

    /**
     * The constructor for a Banner object.
     * It initializes the color and the level.
     *
     * @param color represents the color of the banner.
     * @param level represent the level of the banner.
     */
    public Banner(BannerEnum color, int level) {
        this.color = color;
        this.level = level;
    }

    /**
     * Getter for the color.
     *
     * @return the color of the banner.
     */
    public BannerEnum getColor() {
        return color;
    }

    /**
     * Getter for the level.
     *
     * @return the level of the banner.
     */
    public int getLevel() {
        return level;
    }

    /**
     * To string method of the class.
     *
     * @return a string representation of the object.
     */
    public String toString() {
        return "BA=" + color + ":" + level + ";";
    }

    /**
     * Compares the color of two banners.
     *
     * @param b the other banner to compare.
     * @return true if the two banners have the same color, false otherwise.
     */
    public boolean equalsColor(Banner b) {
        return this.color.name().equals(b.getColor().name());
    }

    /**
     * Compares the level of two banners.
     *
     * @param b the other banner to compare.
     * @return true if the two banners have the same level, false otherwise.
     */
    public boolean equalsLevel(Banner b) {
        return this.level == b.getLevel();
    }

    /**
     * Compares the color and the level of two banners.
     *
     * @param b the other banner to compare.
     * @return true if the two banners are equals, false otherwise.
     */
    public boolean equals(Banner b) {
        return equalsColor(b) && equalsLevel(b);
    }

    /**
     * Compares the level of two banners.
     *
     * @param b the other banner to compare.
     * @return true if this banner is a lower level than b level, false otherwise.
     */
    public boolean isOneLess(Banner b) {
        return this.level == b.getLevel() - 1;
    }

}