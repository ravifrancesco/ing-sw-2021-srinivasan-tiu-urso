package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.view.UI.GUI.GUI;
import it.polimi.ingsw.view.UI.GUI.JavaFX.utils.Slot;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class FaithMarkerController {

    private GUI gui;

    @FXML
    private ImageView faithMarker;

    private int position;

    private Slot[] faithSlots;

    double increment;

    public void assignSlots(Slot[] slots) {
        this.faithSlots = slots;
    }

    public void createItem() {
        File file = new File("src/main/resources/png/faith_marker.png");
        Image image = new Image(file.toURI().toString());
        faithMarker = new ImageView(image);
        increment = 2.5;
        faithMarker.setFitHeight(35);
        faithMarker.setFitWidth(35);
        faithMarker.setX(faithSlots[0].getX() + increment);
        faithMarker.setY(faithSlots[0].getY() + increment);
        position = 0;
    }

    public void createLorenzoItem() {
        File file = new File("src/main/resources/png/lorenzo_faith_marker.png");
        Image image = new Image(file.toURI().toString());
        faithMarker = new ImageView(image);
        increment = 7.5;
        faithMarker.setFitHeight(25);
        faithMarker.setFitWidth(25);
        faithMarker.setX(faithSlots[0].getX() + increment);
        faithMarker.setY(faithSlots[0].getY() + increment);
        position = 0;
    }

    public void moveFaithMarker(int position) {
        faithMarker.setX(faithSlots[position].getX() + increment);
        faithMarker.setY(faithSlots[position].getY() + increment);
        this.position = position;
    }

    public ImageView getItem() {
        return faithMarker;
    }

    public int getPosition() {
        return position;
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public void hide() {
        faithMarker.setVisible(false);
    }

    public void show() {
        faithMarker.setVisible(true);
    }
}
