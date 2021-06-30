package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.view.UI.GUI.JavaFX.utils.Slot;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class FaithMarkerController {

    @FXML
    private ImageView faithMarker;

    private Slot[] faithSlots;

    double increment;

    public void assignSlots(Slot[] slots) {
        this.faithSlots = slots;
    }

    public void createItem() {
        File file = new File("src/main/resources/png/table/faith_marker.png");
        Image image = new Image(file.toURI().toString());
        faithMarker = new ImageView(image);
        increment = 2.5;
        faithMarker.setFitHeight(35);
        faithMarker.setFitWidth(35);
        faithMarker.setX(faithSlots[0].getX() + increment);
        faithMarker.setY(faithSlots[0].getY() + increment);
    }

    public void createLorenzoItem() {
        File file = new File("src/main/resources/png/singlePlayer/lorenzo_faith_marker.png");
        Image image = new Image(file.toURI().toString());
        faithMarker = new ImageView(image);
        increment = 7.5;
        faithMarker.setFitHeight(25);
        faithMarker.setFitWidth(25);
        faithMarker.setX(faithSlots[0].getX() + increment);
        faithMarker.setY(faithSlots[0].getY() + increment);
    }

    public void moveFaithMarker(int position) {
        faithMarker.setX(faithSlots[position].getX() + increment);
        faithMarker.setY(faithSlots[position].getY() + increment);
    }

    public ImageView getItem() {
        return faithMarker;
    }

    public void hide() {
        faithMarker.setVisible(false);
    }

    public void show() {
        faithMarker.setVisible(true);
    }
}
