package it.polimi.ingsw.model;

/**
 * The class represents a Banner of the game.
 * Each banner has a color (GREEN, YELLOW, BLUE or PURPLE)
 * and a level (1, 2 or 3)
 */

public class Banner {

	BannerEnum color;
	int level;

	/**
	 * The constructor for a Banner object.
	 * It initializes the color and the level.
	 * @param color represents the color of the banner
	 * @param level represent the level of the banner
	 */
	Banner(BannerEnum color, int level){
		this.color=color;
		this.level=level;
	}

	/**
	 * Getter for the color
	 * @return the color of the banner
	 */

	public BannerEnum getColor() {
		return color;
	}

	/**
	 * Getter for the level
	 * @return the level of the banner
	 */
	public int getLevel() {
		return level;
	}

	/** To string method of the class
	 * @return a string representation of the object
	 */

	public String toString(){
		return "BA=" + color + ":" + level + ";";
	}

	/**
	 * Compares the color of two banners
	 * @param b the other banner to compare
	 * @return true if the two banners have the same color
	 */

	public boolean equalsColor(Banner b){
		return this.getColor()==b.getColor();
	}

	/**
	 * Compares the level of two banners
	 * @param b the other banner to compare
	 * @return true if the two banners have the same level
	 */

	public boolean equalsLevel(Banner b){
		return this.getLevel()==b.getLevel();
	}

}