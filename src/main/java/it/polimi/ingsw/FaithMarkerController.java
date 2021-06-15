package it.polimi.ingsw;

import it.polimi.ingsw.model.Resource;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class FaithMarkerController {

    @FXML
    private ImageView faithMarker;

    private int position;

    private Slot faithSlots[];

    public void assignSlots(Slot[] slots) {
        this.faithSlots = slots;
    }

    public void createItem() {
        String name = "";
        File file = new File("src/main/resources/png/faith_marker.png");
        Image image = new Image(file.toURI().toString());
        faithMarker = new ImageView(image);
        faithMarker.setFitHeight(35);
        faithMarker.setFitWidth(35);
        faithMarker.setX(faithSlots[0].getX() + 5);
        faithMarker.setY(faithSlots[0].getY() + 5);
        position = 0;
    }

    public void moveFaithMarker(int position) {
        faithMarker.setX(faithSlots[position].getX() + 5);
        faithMarker.setY(faithSlots[position].getY() + 5);
        this.position = position;
    }

    public ImageView getItem() {
        return faithMarker;
    }

    public int getPosition() {
        return position;
    }
}
