package it.polimi.ingsw.view.UI.GUI.JavaFX.utils;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Slot {

    private final Rectangle rectangle;

    private final double x;
    private final double y;

    private final double width;
    private final double height;

    private boolean empty;

    /**
     * Constructor for a slot
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width of the slot
     * @param height the height of the slot
     */
    public Slot(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.empty = true;
        rectangle = new Rectangle(this.x, this.y, this.width, this.height);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.TRANSPARENT);
    }

    /**
     * Getter for the X
     * @return the X
     */
    public double getX() {
        return x;
    }

    /**
     * Getter for the Y
     * @return the Y
     */
    public double getY() {
        return y;
    }

    /**
     * Getter for the width
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Getter for the height
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Method to check if a point is in the slot
     * @param x the x point
     * @param y the y point
     * @return true if the point belongs to the slot, false otherwise
     */
    public boolean isPointInSlot(double x, double y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }

    /**
     * Method to fill the slot
     */
    public void filLSlot() {
        empty = false;
    }

    /**
     * Method to free the slot
     */
    public void freeSlot() {
        empty = true;
    }

    /**
     * Method to know if the slot is empty
     * @return true if the slot is empty, false otherwise
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * Method to get the rectangle
     * @return the rectangle slot
     */
    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * Method to set the rectangle stroke
     * @param color the color to be setted
     */
    public void setStroke(Color color) {
        rectangle.setStroke(color);
    }
}
