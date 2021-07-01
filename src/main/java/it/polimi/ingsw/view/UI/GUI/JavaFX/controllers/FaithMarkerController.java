package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.view.UI.GUI.JavaFX.utils.Slot;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class FaithMarkerController {

    double increment;
    @FXML
    private ImageView faithMarker;
    private Slot[] faithSlots;

    /**
     * Method to assign the slots of the faith track
     * @param slots the slots
     */
    public void assignSlots(Slot[] slots) {
        this.faithSlots = slots;
    }

    /**
     * Method to create a faith marker
     */
    public void createItem() {
        Image image = new Image(this.getClass().getResourceAsStream("/png/table/faith_marker.png"));
        faithMarker = new ImageView(image);
        increment = 2.5;
        faithMarker.setFitHeight(35);
        faithMarker.setFitWidth(35);
        faithMarker.setX(faithSlots[0].getX() + increment);
        faithMarker.setY(faithSlots[0].getY() + increment);
    }

    /**
     * Method to create Lorenzo's faith marker
     */
    public void createLorenzoItem() {
        Image image = new Image(this.getClass().getResourceAsStream("/png/singlePlayer/lorenzo_faith_marker.png"));
        faithMarker = new ImageView(image);
        increment = 7.5;
        faithMarker.setFitHeight(25);
        faithMarker.setFitWidth(25);
        faithMarker.setX(faithSlots[0].getX() + increment);
        faithMarker.setY(faithSlots[0].getY() + increment);
    }

    /**
     * Method to move the faith marker
     * @param position the position of the faith marker
     */
    public void moveFaithMarker(int position) {
        faithMarker.setX(faithSlots[position].getX() + increment);
        faithMarker.setY(faithSlots[position].getY() + increment);
    }

    /**
     * Getter for the item
     * @return the faith marker
     */
    public ImageView getItem() {
        return faithMarker;
    }

    /**
     * Method to hide the faith marker
     */
    public void hide() {
        faithMarker.setVisible(false);
    }

    /**
     * Method to show the faith marker
     */
    public void show() {
        faithMarker.setVisible(true);
    }
}
