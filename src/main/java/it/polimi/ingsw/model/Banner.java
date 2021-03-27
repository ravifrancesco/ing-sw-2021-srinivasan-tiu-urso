package it.polimi.ingsw.model;

/**
 * The class represents a Banner of the game.
 * Each banner has a color (GREEN, YELLOW, BLUE or PURPLE)
 * and a level (1, 2 or 3)
 */

public class Banner {

	String color;
	int level;

	/**
	 * The constructor for a Banner object.
	 * It initializes the color and the level.
	 * @param color represents the color of the banner
	 * @param level represent the level of the banner
	 */
	Banner(String color, int level){
		this.color=color;
		this.level=level;
	}

	/**
	 * Getter for the color
	 * @return the color of the banner
	 */

	public String getColor() {
		return color;
	}

	/**
	 * Getter for the level
	 * @return the level of the banner
	 */
	public int getLevel() {
		return level;
	}

}